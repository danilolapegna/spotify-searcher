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

## Persistent storage

All the necessary data, and no more data than that, is persisted in cache via [Realm](https://realm.io/) 

This also allows the app to be fully reactive, as UI can be subscribed to realm changes.

Also, in case of no network, in case an object is already available on Realm, it will be pre-emptively loaded.

There's also a quick check such that if the object is older than a day, or has some incomplete fields, the app will try reloading it.



