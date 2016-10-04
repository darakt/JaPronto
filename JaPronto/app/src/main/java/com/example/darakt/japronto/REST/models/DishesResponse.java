package com.example.darakt.japronto.REST.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * Created by darakt on 20/09/16.
 */

public class DishesResponse {
    List<Dish> menu;

    public DishesResponse(List<Dish> menu) {
        this.menu = menu;
    }

    public static DishesResponse parseJSON(String response){
        Gson gson = new GsonBuilder().create();
        DishesResponse dishesResponse = gson.fromJson(response, DishesResponse.class);
        return dishesResponse;
    }
}
