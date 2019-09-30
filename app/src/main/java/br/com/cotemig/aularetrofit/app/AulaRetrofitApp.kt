package br.com.cotemig.aularetrofit.app

import android.app.Application

class AulaRetrofitApp : Application {

    constructor() : super()

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        val PROD = false
        val URL_API_PROD: String = "http://apiteste.fourtime.com/api/"
        val URL_API_DEV: String = "http://apiteste.fourtime.com/api/"
        val URL_API = if(PROD) URL_API_PROD else URL_API_DEV

        private var instance: AulaRetrofitApp? = null

        fun getInstance(): AulaRetrofitApp? {
            return instance
        }


    }

}