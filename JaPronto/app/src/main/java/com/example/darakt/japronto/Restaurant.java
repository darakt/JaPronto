package com.example.darakt.japronto;

import com.example.darakt.japronto.REST.models.Dish;
import com.example.darakt.japronto.REST.models.User;
import com.example.darakt.japronto.REST.models.UsersResponse;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * Created by darakt on 11/10/16.
 */

public class Restaurant  implements Serializable{
    public User chef= new User();
    public List<Dish> menu = new Vector<Dish>();

    public Restaurant(User chef, List<Dish> menu) {
        this.chef = chef;
        this.menu = menu;
    }

    public User getChef() {
        return chef;
    }

    public void setChef(User chef) {
        this.chef = chef;
    }

    public List<Dish> getMenu() {
        return menu;
    }

    public void setMenu(List<Dish> menu) {
        this.menu = menu;
    }
}
