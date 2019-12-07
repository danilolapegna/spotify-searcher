# Spotify Searcher

This is a sample app, using Rx + Realm + MVVM + Material design guidelines in order to provide a search sample for artists and tracks on Spotify.

Every artist, and track, has also its own very simple page with a few info, and the possibility to reach the equivalent one on Spotify.

## Networking

No Retrofit was used for networking, so as a matter of exercise, a custom library wrapping OkHttp, and using Rx for requests, was implemented.

The library is in a separate module, `library_rest`, and can be easily used by invoking the class `RxApiClientRequestBuilder` and implementing a `Builder` pattern, for the api client first and then for the final request itself, which will emit a `Single<ResponseClass>`. For example:

To create, for example, a client with header and base url:

```
val client = RxApiClient.Builder()
                .baseUrl("https://api.spotify.com/v1/")
                .header("Content-type", "application/json")
                .build()
```

And to get a Rx search request via this client:

```
RxApiClientRequestBuilder<T>()
                .client(client)
                .responseClass(SearchResponse::class.java)
                .additionalUrl("search")
                .queryParameter("query", query)
                .queryParameter("type","artist,tracks")
                .build()
```

Which then you can subscribe to by simply using the regular `Single<SearchResponse>.subscribe` method. 

Any Http method (GET/POST/PUT/PATCH/DELETE) is fully supported. But where not explicitly defined in the request, the app assumes and defaults to GET.

### Unit tests

Library is unit-tested with Dagger + Mockito. Two components, in particular, are tested for now: 

- `OkHttpRequestExecutor`, the base component we use to execute requests. In this test a test Dagger module injects a mock `OkHttpClient` and a mock `OkHttpRequestProvider` and verifies the executor still does its job.

- `RxApiClient`, the single Rx generator as from above. In this test a test Dagger module injects a mock `BaseRestRequestExecutor` and verifies the client still returns a valid `Single<Response>`.


## Persistent storage

All the necessary data, and no more data than that, is persisted in cache via [Realm](https://realm.io/) 

This also allows the app to be fully reactive, as UI can be subscribed to realm changes.

Also, in case of no network, in case an object is already available on Realm, it will be pre-emptively loaded.

There's also a quick check such that if the object is older than a day, or has some incomplete fields, the app will try reloading it.

## Architecture

The one used here is a "pure" MVVM, where each factory was separately created, by extending the default one, in order to allow passing a parameter.

No `LiveData` was used, as Rx and Realm both already fulfilled the purpose of generating fully observable sets of mutable data.

## Functionality

- Login screen: a very plain and simple screen made in a `LoginActivity`, allowing authentication via the Spotify SDK

- Search screen: made in a `SearchActivity` + `SearchFragment`, it is where most of the magic happens. An api call is made automatically for the last search term typed. Artists and tracks for that term are returned, each of them is recognisable thanks to an icon (implemented via `VectorDrawable`), and each of them is persisted onto realm only on click, to avoid engulfing the app persistent storage with MBs that will never be used. Some progress is shown and a "no results" text is displayed in case the search returns no results. Also, a toast is displayed in case of error. 

- Artist screen: made in a `ArtistActivity` + `ArtistFragment`, is a detail page with the artist info. Made with a `CollapsingToolbarLayout`, it loads the artist from Realm first, and if the data is complete and up-to-date, doesn't perform any further api call. Also, gives the possibility to reach the Spotify page for the artist.

- Track screen: made in a `TrackActivity` + `TrackFragment`, a detail page with the track info. Also made with a `CollapsingToolbarLayout`, it loads the track from Realm first, and if the data is complete and up-to-date, doesn't perform any further api call. Displays the artist list for that track and allows to reach the in-app page on click. Also, displays the track duration and gives the possibility to reach the Spotify page for the track, by both a clickable `Spannable` and a (by simplicity) play button.

## Other to-be-noted

- A kotlin extension `UIExtensions.kt` was implemented as a convenience to allow quicker operations on activities/fragments

- Picasso was used as a default image loading library. A placeholder is displayed while loading and kept in case of error.

- [Material search bar](https://github.com/mancj/MaterialSearchBar) was used for the main search screen.

- Proximanova font was added for a more pleasant user experience

- Last api call, wrapped in a closure, is kept on hold in case requested during a moment of no network. The whole app is constantly aware of the current network state through the implementation of a Rx-based `ConnectionStateMonitor` component, and hence the app will be re-executed once the connection is back.

## Could-be-added/To-be-improved

This is the list of the "limitations" I'm aware of, and that were kept as such as a matter of simplicity, and in order to get this done within a reasonable amount of time. By the way there's also no reason to believe these feats won't be added in future. 

- Authentication is made without a refresh token, which means it expires usually after 30 minutes of use. We'd need probably to implement a full authentication flow with longer-living token mechanism. By the way doing this without a backend would imply hardcoding a `client_secret` into the mobile app, and that's absolutely unsafe and not recommended. For this reason we'll get to this 30 minutes of usage compromise. Expiration is gracefully handled anyway, and the user is each time prompted to log in again.

- For now my Rx client/library only supports `json` body as a MediaType, so different kind of media (simply by adding more values and converters to the enum), like images or video, can be added in future to the `BodyType` `enum class`.

- Artist page feels a bit empty. Some more, interesting info could be added? Maybe an albums query to be bound to a `RecyclerView`?

- A side `DrawerLayout` with search filters? (search only artists, search only tracks, sort by...)

- Some pagination, maybe with lazy loading, could be added to the main search in order to add a few more results per string. By the way, as far as I could see, for now, a reasonable set of relevant results is always displayed within the first 20.

- Api call kept on hold when there's no network: probably a larger queue could be implemented + a mechanism to make sure the app isn't re-executed in case it isn't "useful" anymore for the current screen. By the way, even in this case, the compromise serves the purpose.
