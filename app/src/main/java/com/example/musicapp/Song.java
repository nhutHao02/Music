package com.example.musicapp;

import java.io.Serializable;

public class Song implements Serializable {
    private String img;
    private String link;
    private String nameSong;
    private String author;

    public Song() {
    }

    public Song(String img, String link, String nameSong, String author) {
        this.img = img;
        this.link = link;
        this.nameSong = nameSong;
        this.author=author;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
