package com.example.repositorytask.apibridge;

import com.example.repositorytask.model.RepositoryData;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


/**
 * This interface is used to send request and response using Retrofit2 API
 */
public interface APIService {

    @GET("repositories")
    Observable<Response<List<RepositoryData>>> repositories(@Query("spoken_language_code") String code);

}
