# Price Tracker

This is an attempt cloning a stock tracking application's two usecase namely,

* Streak live stock quotation from a remote server

* View the streamed stock quotation in a line chart

## - Architectural Layers
- **Network** Responsible for remote server access
- **Usecase** Exposes api for the above mentioned core usage of the application(houses the BL)
- **ViewModel** Responsible for handling the logic behind how the data is displayed in the view
- **View** The UI layer coupled with Android Framework


## - Built With
- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Rx-Java](https://github.com/ReactiveX/RxJava) - For composing asynchronous and event-based programs by using observable sequences.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Data objects that notify views when the underlying database changes.
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes.
- [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
- [OkHttp](http://square.github.io/okhttp/) - HTTP client that's efficient by default: HTTP/2 support allows all requests to the same host to share a socket
- [Mockito](http://site.mockito.org/) - Most popular mocking framework for Java/kotlin.

This application also leverages the benefits of TDD to achieve 92% code coverage for Business Logic.