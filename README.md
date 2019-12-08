# Spotify Searcher

This is a sample app, using Rx + Dagger + Realm + MVVM + Material design guidelines in order to provide a search sample for artists and tracks on Spotify.

Every artist, and track, has also its own very simple page with a few info, and the possibility to reach the equivalent one on Spotify.

## Networking

No Retrofit was used for networking, so as a matter of exercise, a custom library wrapping OkHttp, and using Rx for requests, was implemented.

The library is in a separate module, `library_rest`, and can be easily used by invoking the class `RxApiClientRequestBuilder` and applying a `Builder` pattern, for the api client first and then for the final request itself, which will emit a RxJava `Single<ResponseClass>` object. For example:

To create a client with header and base url:

```
val spotifyClient = RxApiClient.Builder()
                .baseUrl("https://api.spotify.com/v1/")
                .header("Content-type", "application/json")
                .build()
```

And to get a Rx search request via this client:

```
RxApiClientRequestBuilder<T>()
                .client(spotifyClient)
                .requestMethod(RequestMethod.GET)
                .responseClass(SearchResponse::class.java)
                .additionalUrl("search")
                .queryParameter("query", query)
                .queryParameter("type","artist,tracks")
                .build()
```

Which then you can subscribe to by simply using the RxJava `Single<SearchResponse>.subscribe` method. 

Any Http method (GET/POST/PUT/PATCH/DELETE) is fully supported. But where not explicitly defined in the request, the app assumes and defaults to GET.

### Unit tests

Library is unit-tested with Dagger + Mockito. The two most important components are unit-tested for now: 

- `OkHttpRequestExecutor`, the base component we use to execute requests. In this test a test Dagger module injects a mock `OkHttpClient` and a mock `OkHttpRequestProvider` and verifies the executor still does its job.

- `RxApiClient`, the single Rx generator as from above. In this test a test Dagger module injects a mock `BaseRestRequestExecutor` and verifies the client still returns a valid `Single<Response>`.


## Persistent storage

All the necessary data, and no more data than that, is persisted in cache via [Realm](https://realm.io/) 

This also allows the app to be fully reactive, as UI can be subscribed to realm changes.

Also, in case of no network, and in case an object is already available on Realm, it will be pre-emptively loaded, so that the user experience is seamless, even in case of bad/no connection.

There's also a quick check such that if the object is older than a day, or has some incomplete fields, the app will try reloading it. This way 1) No stale data will be persisted, and 2) No useless API calls are performed in case we have a "young" object to display already.

## Architecture

The one used here is a "pure" MVVM, where each factory was separately created, by extending the default one, in order to allow passing a parameter.

No `LiveData` was used, as Rx and Realm both already fulfilled the purpose of generating fully observable sets of mutable data.

## Functionality

- Login screen: a very plain and simple screen made in a `LoginActivity`, allowing authentication via the Spotify SDK

- Search screen: made in a `SearchActivity` + `SearchFragment`, it is where most of the magic happens. An api call is made automatically for the last search term typed. A delay of 500ms is added, so that we avoid to make multiple calls while typing. 

A result set of the first 20 Artists and tracks for that term are returned, each of them is recognisable thanks to an icon (implemented via `VectorDrawable`), and each of them is persisted onto realm only on click, to avoid engulfing the app persistent storage with MBs that will never be used. Some progress is shown and a "no results" text is displayed in case the search returns no results. Also, a toast is displayed in case of error. 

- Artist screen: made in a `ArtistActivity` + `ArtistFragment`, is a detail page with the artist info. Made with a `CollapsingToolbarLayout`, it loads the artist from Realm first, and if the data is complete and up-to-date, doesn't perform any further api call. Also, gives the possibility to reach the Spotify page for the artist.

- Track screen: made in a `TrackActivity` + `TrackFragment`, a detail page with the track info. Also made with a `CollapsingToolbarLayout`, it loads the track from Realm first, and if the data is complete and up-to-date, doesn't perform any further api call. Displays the artist list for that track and allows to reach the in-app page on click. Also, displays the track duration and gives the possibility to reach the Spotify page for the track, by both a clickable `Spannable` and a (by simplicity, even tho I realise it may be slightly misleading from a user point of view) play button.

## Other to-be-noted

- A kotlin extension `UIExtensions.kt` was implemented as a convenience to allow quicker operations on activities/fragments

- Picasso was used as a default image loading library. A placeholder is displayed while loading and kept in case of error.

- App is meant to be used on portrait mode, as that's where it looks best. By the way, for the sake of "proving" that it handles lifecycle on rotation well, rotation isn't locked. In fact all components are lifecycle-aware, so caching is done when needed, the needed state is saved and subscriptions are disposed when needed.

- [Material search bar](https://github.com/mancj/MaterialSearchBar) was used for the main search screen, for the sake of giving some pleasant material design experience.

- Proximanova font was added for a more pleasant user experience

- Last executed api call is kept on hold in case requested during a moment of no network. 
The whole app is in fact constantly aware of the current network state through the implementation of a Rx-based `ConnectionStateMonitor` component. This component has its own `Observable<ConnectionState>`, that hence we can `takeFirst -> flatmap` `to our Single<ApiResponse>`. This means that, in case of no network the UI will subscribe not just to the api call, but to a chain "Wait for network to be available -> Take result after then". Important is, if you reuse this functionality, not to show any progress in case the request is on hold, by checking network state again. UI is in fact completely unaware of what's happening "under the hood" and he just knows it subscribed to "something", whether is a network call or a call kept on hold until network is available. And the reason of that is that both things can equally be disposed in lifecycle, when needed.

## Could-be-added/To-be-improved

This is the list of the "limitations" I'm aware of, and that were kept as such as a matter of simplicity, and in order to get this done within a reasonable amount of time. By the way there's also no reason to believe these feats won't be added in future. 

- Authentication is made without a refresh token, which means it expires usually after 30 minutes of use. We'd need probably to implement a full authentication flow with longer-living token mechanism. By the way doing this without a backend would imply hardcoding a `client_secret` into the mobile app, and that's absolutely unsafe and not recommended. For this reason we'll get to this 30 minutes of usage compromise. Expiration is gracefully handled anyway, and the user is each time prompted to log in again.

- For now my Rx client/library only supports `json` body as a MediaType for requests. This covers all the needed cases at the moment as we don't need to POST/PUT etc. any stream of bytes. But still, different kind of media (simply by adding more values and converters to the enum), like images or video, can be added in future to the `BodyType` `enum class`.

- Artist page feels a bit empty. Some more, interesting info could be added? Maybe an albums query to be bound to a `RecyclerView`?

- Migrate from `AppCompat` to `androidx` in order to use components as `LifecycleObserver`

- A side `DrawerLayout` with search filters? (search only artists, search only tracks, sort by...)

- Some pagination, maybe with lazy loading, could be added to the main search in order to add a few more results per string. By the way, as far as I could see, for now, a reasonable set of relevant results is always displayed within the first 20.
