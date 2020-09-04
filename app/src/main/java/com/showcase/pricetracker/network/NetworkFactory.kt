package com.showcase.pricetracker.network

import com.showcase.pricetracker.usecase.NetworkGateway
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A self-contained factory for Network module.
 * Use this factory to get a single point of entrance to the network layer.
 */
object NetworkFactory {

    private const val baseUrl = "https://api.tickertape.in"

    fun createGateway(): NetworkGateway {
        return RemoteGateway(getRemoteService())
    }

    private fun getRemoteService(): TickerTapeService {
        val retrofit = retrofit(
            okHttpClient(httpLoggingInterceptor()),
            gsonFactoryConverter()
        )

        return retrofit.create(TickerTapeService::class.java)
    }

    private fun retrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }


    private fun okHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private fun gsonFactoryConverter(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }
}