package br.com.cotemig.aularetrofit.services

import br.com.cotemig.aularetrofit.model.ListTime
import retrofit2.Call
import retrofit2.http.GET


interface ServiceTime {

    @GET("time/lista")
    fun getTime(): Call<ListTime>

//    @POST("time")
//    fun inserirTime(@Body time: Time): Call<Void>

//    @POST("auth")
//    fun auth(@Body user: User): Call<User>

}