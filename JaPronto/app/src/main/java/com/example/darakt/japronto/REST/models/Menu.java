package com.example.darakt.japronto.REST.models;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by darakt on 20/09/16.
 */

public class Menu implements Serializable{
    List<Dish> dishes;
    private static final long serialVersionUID = 465948746;

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public Menu(){
        dishes = new ArrayList<Dish>();
    }

    public Menu(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public Dish[] convertToArray(){
        Dish[] d = this.dishes.toArray(new Dish[this.dishes.size()]);
        return d;
    }
    public void add(Dish dish){
        dishes.add(dish);
    }
}
