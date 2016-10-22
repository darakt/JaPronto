package com.example.darakt.japronto.REST.models;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * Created by darakt on 11/10/16.
 */

public class Restaurant  implements Serializable{
    public Chef chef= new Chef();
    public List<Dish> menu = new Vector<Dish>();

    public Restaurant(Chef chef, List<Dish> menu) {
        this.chef = chef;
        this.menu = menu;
    }

    public Chef getChef() {
        return chef;
    }

    public void setChef(Chef chef) {
        this.chef = chef;
    }

    public List<Dish> getMenu() {
        return menu;
    }

    public void setMenu(List<Dish> menu) {
        this.menu = menu;
    }
}
