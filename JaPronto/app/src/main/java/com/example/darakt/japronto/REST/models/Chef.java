package com.example.darakt.japronto.REST.models;

import java.io.Serializable;

/**
 * Created by darakt on 19/10/16.
 */

public class Chef extends Client implements Serializable {
    public String description;
    public String image;
    private static final long serialVersionUID = 469947646;

    public Chef(String pseudo, String password, String name, String surname, String phone, String mail, String CPF, String description, String image) {
        super(pseudo, password, name, surname, phone, mail, CPF);
        this.description = description;
        this.image = image;
    }

    public Chef(){
        super();
        description = null;
        image = null;
    }

    public String getURL(){
        String url = "http://a9a09068.ngrok.io/japronto/api/img/".concat(image);
        return url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
