# Retrofit
exemplo consumindo uma api com uma  lista de clubes 
0. Set Up
This assumes that you have your development setup ready for Kotlin. In Android Studio 3.0 (Preview), there is now a native support for Kotlin, without manually installing plugins. Check out https://developer.android.com/studio/preview/index.html to install the preview. However, if you don’t want to install the new Android Studio Preview (I recommend you sideload it with your stable version), you can set up your current Android Studio using this guide.

1. Add Dependencies
To use Retrofit, you need to add the dependencies to the app-module build.gradle file:

dependencies {
    // retrofit
    compile "com.squareup.retrofit2:retrofit:2.3.0"
    compile "com.squareup.retrofit2:adapter-rxjava2:2.3.0"
    compile "com.squareup.retrofit2:converter-gson:2.3.0"

    // rxandroid
    compile "io.reactivex.rxjava2:rxandroid:2.0.1"
}


2. Create Data Classes
Typically, the next step is to create the data classes which are POJOs (Plain Old Java Objects) that will represent the responses of the API calls we’re going to make.

With the Github API we’re considering in this post, we will have users as entities as well as other metadata about the search results.

A typical Java class that will hold the User data will look like we have in this Github gist. For convenience of the readers, I won’t post the class here, but it is a 154-line Java class, describing the User entity.

Here is one of the wins for Kotlin - it is much less verbose than Java. We can reproduce the same class in a readable format in less than 20 lines.


Data Classes in Kotlin
We have this concise version of the User entity with Kotlin, thanks to what is called data class in Kotlin. Data classes in Kotlin are classes that are designed specifically for classes that do nothing but hold data.

The Kotlin compiler automatically helps us with implementing equals(), hashCode() and toString() methods on the class and that makes the code even shorter, because we don’t need to do that on our own.

We can override the “default” implementation of any of these methods by defining the method.

A nifty feature is that we can create our search results in one single Kotlin file - say SearchResponse.kt. Our final search response class will contain all the related data classes and look like we have below:


3. Create the API Service Interface
Next step as we usually do in Java is to create the API interface which we will use to make requests and get responses via retrofit.

Typically in Java, I like to create a convenience “factory” class that creates the API service when it is needed and I would do something like this:



------------------------example  is one Adapter interface class -----------------------------------------------------------------------



public interface GithubApiService {

    @GET("search/users")
    Observable<Result> search(@Query("q") String query, @Query("page") int page, @Query("per_page") int perPage);

    /**
     * Factory class for convenient creation of the Api Service interface
     */
    class Factory {

        public static GithubApiService create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.github.com/")
                    .build();

            return retrofit.create(GithubApiService.class);
        }
    }
}



To use this interface, we then make calls like:

GithubApiService apiService = GithubApiService.Factory.create();
apiService.search(/** search parameters go in here **/);



interface GithubApiService {

    @GET("search/users")
    fun search(@Query("q") query: String,
               @Query("page") page: Int,
               @Query("per_page") perPage: Int): Observable<Result>

    /**
     * Companion object to create the GithubApiService
     */
    companion object Factory {
        fun create(): GithubApiService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.github.com/")
                    .build()

            return retrofit.create(GithubApiService::class.java);
        }
    }
}




Note that we didn’t have to use the “name” of the companion object to reference the method, we only used the class name as the qualifier.

Singletons and Companion Objects in Kotlin
To understand what companion objects in Kotlin is, we must first understand what object declarations are in Kotlin. An object declaration in Kotlin is the way a singleton is made in Kotlin.

Singletons in Kotlin is as simple as declaring an object and qualifying it with a name. For example:


Usage for the above object declaration is:

val repository = SearchRepositoryProvider.provideSearchRepository();
With this, we have been able to create a provider that will provide us with a repository instance (that will help us connect to the Github API via the GithubApiService).

The object declaration is lazily initialized when accessed the first time - the same way a Singleton works.

Companion objects however, are a type of object declaration that is qualified with the companion keyword. Companion objects can be likened to static methods or fields in Java. In fact, if you are referencing a companion object from Java, it would appear as a static method or field.

The companion object is what is used in the GithubApiService.kt Kotlin version above.

4. Create Repository
Since we are trying to abstract our processes as much as possible (while leaving it as simple as possible), we can create a simple repository that handles calling the GithubApiService and builds the query string.

The query string matching our specification for this demo app (to find Java developers in Lagos) using the Github API is location:Lagos+language:Java, so we will create a method in the repository that will allow us build this string while taking the location and language as parameters.

Our search repository will look like this:

class SearchRepository(val apiService: GithubApiService) {
    fun searchUsers(location: String, language: String): Observable<Result> {
        return apiService.search(query = "location:$location+language:$language")
    }
}
String Templates in Kotlin
In the block of code above, we have used a feature of Kotlin called “String templates” to build our query string. String templates start with the dollar sign - $ and the value of the variable following it is concatenated with the rest of the string. This is a similar feature to String interpolation in groovy.

5. Make Request and Observe API response using RxJava
Now that we have configured our response classes, our repository interface to help us make the request, we can now make the request and retrieve the API response using RxJava. This step is similar to how we would do it in Java. In Kotlin code, it looks like this:

val repository = SearchRepositoryProvider.provideSearchRepository()
repository.searchUsers("Lagos", "Java")
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe ({
            result ->
            Log.d("Result", "There are ${result.items.size} Java developers in Lagos")
        }, { error ->
            error.printStackTrace()
        })
With this we have made our request and we can retrieve the response and do whatever we want with it.

