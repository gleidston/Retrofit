package br.com.cotemig.aularetrofit.services

import br.com.cotemig.aularetrofit.app.AulaRetrofitApp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInitializer {

    companion object {
        private val okHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().also { it -> it.level = HttpLoggingInterceptor.Level.BODY })
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()
        }

    }


    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(AulaRetrofitApp.URL_API)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun serviceTime(): ServiceTime {
        return retrofit.create(ServiceTime::class.java)
    }

//    fun serviceJogadores(): ServiceJogadores {
//        return retrofit.create(ServiceJogadore::class.java)
//    }


}