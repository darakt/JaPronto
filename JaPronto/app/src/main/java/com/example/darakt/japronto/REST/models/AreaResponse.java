package com.example.darakt.japronto.REST.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * Created by darakt on 06/10/16.
 */

public class AreaResponse {
    List<Area> where;

    public AreaResponse(List<Area> menu) {
        this.where = where;
    }

    public static AreaResponse parseJSON(String response){
        Gson gson = new GsonBuilder().create();
        AreaResponse areaResponse = gson.fromJson(response, AreaResponse.class);
        return areaResponse;
    }
}
