package com.example.repositorytask.apibridge;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class is used to initialise retrofit
 */
public class RetrofitClient {

    private static Retrofit retrofit = null;
    Context context;

    /**
     * This method is used to initialise the Retrofit client
     * @param baseUrl Base url for the retrofit client initialization
     * @return
     */
    public static Retrofit getClient(String baseUrl) {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }
}
