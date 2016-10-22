package com.example.darakt.japronto.REST;

import com.example.darakt.japronto.REST.models.Area;
import com.example.darakt.japronto.REST.models.Client;
import com.example.darakt.japronto.REST.models.Order;
import com.example.darakt.japronto.REST.models.test;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by darakt on 23/09/16.
 */

public interface ApiService {

    @GET("japronto/api/{client}")
    Call<Client> connect(@Path("client") String user);

    @POST ("japronto/api/client/new/")
    Call<Client> createUser(@Body Client client);

    @GET("japronto/api/foods/")
    Call<test> getFood();

    @GET("japronto/api/restaurant/near")
    Call<List<Area>> getRestNear(@Query("lat") String lat, @Query("lng") String lng);

    @POST("japronto/api/order")
    Call<Order> createOrder(@Body Order order);

    @GET("japronto/api/order/{client}")
    Call<List<Order>> getMyOrders(@Path("client") String user);

}
