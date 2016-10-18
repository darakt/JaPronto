package com.example.darakt.japronto.REST;

import com.example.darakt.japronto.REST.models.Area;
import com.example.darakt.japronto.REST.models.AreaResponse;
import com.example.darakt.japronto.REST.models.Order;
import com.example.darakt.japronto.REST.models.User;
import com.example.darakt.japronto.REST.models.test;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("japronto/api/restaurant/near")
    Call<List<Area>> getRestNear(@Query("lat") String lat, @Query("lng") String lng);

    @POST("japronto/api/order")
    Call<Order> createOrder(@Body Order order);

    @GET("japronto/api/order/{user}")
    Call<List<Order>> getMyOrders(@Path("user") String user);

}
