package com.example.musicapp;

import java.lang.reflect.Array;
import java.util.List;

public class Account {
    private String name;
    private String email;
    private String pass;
    private String sex;
    private String address;
    private String dob;
    private List<String> myPlaylist;
    private String history;
    private String admin;
    public  Account(){

    }
    public Account(String email, String pass,String name, String sex, String address, String dob) {
        this.email = email;
        this.name = name;
        this.pass = pass;
        this.sex = sex;
        this.address = address;
        this.dob = dob;
    }

    public Account(String name, String email, String pass, String sex, String address, String dob, List<String> myPlaylist, String history, String admin) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.sex = sex;
        this.address = address;
        this.dob = dob;
        this.myPlaylist = myPlaylist;
        this.history = history;
        this.admin=admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public List<String> getMyPlaylist() {
        return myPlaylist;
    }

    public String getHistory() {
        return history;
    }

    public void setMyPlaylist(List<String> myPlaylist) {
        this.myPlaylist = myPlaylist;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", pass='" + pass + '\'' +
                ", sex='" + sex + '\'' +
                ", address='" + address + '\'' +
                ", dob='" + dob + '\'' +
                '}';
    }
}
