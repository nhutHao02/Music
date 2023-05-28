package com.example.musicapp;

import java.io.Serializable;
import java.util.List;

public class PlayList implements Serializable {
    private String name;
    private int size;
    private  String img;
    private List<Song>  list ;

    public PlayList() {

    }
    public PlayList(String name) {
        this.name = name;

    }

    public PlayList(String name, int size, String img, List<Song> list) {
        this.name = name;
        this.size = size;
        this.img = img;
        this.list = list;
    }

    public PlayList(String name, int size, String img) {
        this.name = name;
        this.size = size;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<Song> getList() {
        return list;
    }

    public void setList(List<Song> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PlayList{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", img='" + img + '\'' +
                '}';
    }
}
