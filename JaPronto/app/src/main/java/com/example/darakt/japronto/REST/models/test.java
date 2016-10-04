package com.example.darakt.japronto.REST.models;

/**
 * Created by darakt on 26/09/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class test{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pseudo")
    @Expose
    private String pseudo;

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The pseudo
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     *
     * @param pseudo
     * The pseudo
     */
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

}