package com.example.darakt.japronto.REST.models;

import android.content.Intent;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by darakt on 13/10/16.
 */

public class Order implements Serializable {
    int id = 0;
    int id_chef = 0;
    String customer_pseudo = "" ;
    Chef chef = new Chef();
    Menu wanted = new Menu();
    Date for_the;
    String for_the_date, for_the_time = null;
    int state = 0;
    String lat = null;
    String lng = null;
    int total = 0;
    private static final long serialVersionUID = 465907646;

    public void clean(){
        wanted.clean();
        for_the_date = null;
        for_the_time = null;
        state = 0;
        //lat = "";
        //lng ="";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Chef getChef() {
        return chef;
    }

    public void setChef(Chef chef) {
        this.chef = chef;
    }

    public Menu getWanted() {
        return wanted;
    }

    public void setWanted(Menu wanted) {
        this.wanted = wanted;
    }

    public String getFor_the_date() {
        return for_the_date;
    }

    public void setFor_the_date(String for_the_date) {
        this.for_the_date = for_the_date;
    }

    public String getFor_the_time() {
        return for_the_time;
    }

    public void setFor_the_time(String for_the_time) {
        this.for_the_time = for_the_time;
    }

    public String getCustomer_pseudo() {
        return customer_pseudo;
    }

    public void setCustomer_pseudo(String customer_pseudo) {
        this.customer_pseudo = customer_pseudo;
    }

    public int getId_chef() {
        return id_chef;
    }

    public void setId_chef(int id_chef) {
        this.id_chef = id_chef;
    }

    public Date getFor_the() {
        return for_the;
    }

    public void setFor_the(Date for_the) {
        this.for_the = for_the;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
