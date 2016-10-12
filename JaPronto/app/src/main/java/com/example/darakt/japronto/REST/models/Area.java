package com.example.darakt.japronto.REST.models;

import java.util.List;
import java.util.Vector;

/**
 * Created by darakt on 06/10/16.
 */

public class Area {
    public int id = 0;
    public int id_chef = 0;
    public String lat = "";
    public String lng = "";
    public String distance = "";
    public User chef = new User();

    public List<Dish> getMenu() {
        return menu;
    }

    public void setMenu(List<Dish> menu) {
        this.menu = menu;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public User getChef() {
        return chef;
    }

    public void setChef(User chef) {
        this.chef = chef;
    }

    public List<Dish> menu = new Vector<Dish>();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_chef() {
        return id_chef;
    }

    public void setId_chef(int id_chef) {
        this.id_chef = id_chef;
    }

    public String getLat() {
        return lat;
    }

    public double getLati() {
        return Double.parseDouble(this.lat);
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public double getLngi() {
        return Double.parseDouble(this.lng);
    }

    public void setLngi(String lng) {
        this.lng = lng;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
