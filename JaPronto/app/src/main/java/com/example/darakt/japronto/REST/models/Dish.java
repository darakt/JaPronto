package com.example.darakt.japronto.REST.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Created by darakt on 20/09/16.
 */

public class Dish implements Serializable{
    int id=0;
    int id_chef=0;
    String name="";
    String image;
    String description="";
    int disponibility;

    public Dish(int id, int id_chef, String name, String image, String description, int disponibility) {
        this.id = id;
        this.id_chef = id_chef;
        this.name = name;
        this.image = image;
        this.description = description;
        this.disponibility = disponibility;
    }

    public int getId() {
        return id;
    }

    public int getId_chef() {
        return id_chef;
    }

    public String getName() {
        return name;
    }


    /*
    public String getImageBase64(){
        //TODO: implement conversion
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public void setImageBase64(String base64String){
        //TODO: implement conversion
        byte[] imageAsBytes = Base64.decode(base64String.getBytes(), Base64.DEFAULT);
        this.image = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

    }
    */

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setId_chef(int id_chef) {
        this.id_chef = id_chef;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setDescription(String description) {
        this.description = description;
    }
}
