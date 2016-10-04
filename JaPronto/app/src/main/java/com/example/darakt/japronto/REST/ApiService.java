package com.example.darakt.japronto.REST;

import com.example.darakt.japronto.REST.models.User;
import com.example.darakt.japronto.REST.models.test;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by darakt on 23/09/16.
 */

public interface ApiService {

    @GET("japronto/api/{user}")
    Call<User> connect(@Path("user") String user);

    @POST ("japronto/api/user/new/")
    Call<User> createUser(@Body User user);

    @GET("japronto/api/foods/")
    Call<test> getFood();

}
