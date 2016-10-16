package com.example.darakt.japronto.REST.models;

import java.io.Serializable;

/**
 * Created by darakt on 20/09/16.
 */


public class User implements Serializable{
    int id = 0;
    String pseudo = "";
    String password = "";
    String name = "";
    String surname = "";
    String description = "";
    String phone = "";
    String mail = "";
    String CPF = "";
    int status = 0;
    private static final long serialVersionUID = 46597646;

    public User(String pseudo, String password, String name, String surname, String description, String phone, String mail, String CPF) {
        this.pseudo = pseudo;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.description = description;
        this.phone = phone;
        this.mail = mail;
        this.CPF = CPF;
        this.status = 1;
    }
    public User(){
        this.pseudo = null;
        this.password = null;
        this.name = null;
        this.surname = null;
        this.phone = null;
        this.mail = null;
        this.CPF = null;
        this.status = 1;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() { return password; }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

    public int getId() {
        return id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getName() {
        return name;
    }

    public String getCPF() {
        return CPF;
    }

    public int getstatus() {
        return status;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
