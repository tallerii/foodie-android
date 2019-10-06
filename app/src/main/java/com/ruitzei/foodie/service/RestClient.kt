package com.ruitzei.foodie.service

import com.google.gson.GsonBuilder
import com.ruitzei.foodie.model.UserData
import com.ruitzei.foodie.ui.Config
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Created by ruitzei on 3/17/18.
 */
object RestClient {
    private val TAG = RestClient::class.java.simpleName

    fun createPublicApi(): ApiCalls {
        // Adding the authorization headers to the request.
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()

            val request: Request
            if (UserData.token.isNullOrEmpty()) {
                request = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .method(original.method(), original.body())
                    .build()
            } else {
                request = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Token ${UserData.token}")
                    .method(original.method(), original.body())
                    .build()
            }

            chain.proceed(request)
        }

//        httpClient.readTimeout(60L, TimeUnit.SECONDS);
//        httpClient.connectTimeout(60, TimeUnit.SECONDS);

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        httpClient.addInterceptor(loggingInterceptor)

        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .setLenient()
                .create()


        val retrofit = Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(Config.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()

        return retrofit.create(ApiCalls::class.java)
    }
}
