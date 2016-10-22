package com.example.darakt.japronto.REST.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * Created by darakt on 20/09/16.
 */

public class UsersResponse {
    List<Client> somes;

    public List<Client> getSomes() {
        return somes;
    }

    public static UsersResponse parseJSON(String response){
        Gson gson = new GsonBuilder().create();
        UsersResponse usersResponse = gson.fromJson(response, UsersResponse.class);
        return usersResponse;
    }

}
