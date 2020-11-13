package com.example.smartfarm;

public class Item {
    String id;
    String temp;
    String humid;
    String light;
    String soil;


    public Item() {
    }

    public Item(String temp, String humid, String light, String soil) {
        this.temp = temp;
        this.humid = humid;
        this.light = light;
        this.soil = soil;
    }

    public Item(String id, String temp, String humid, String light, String soil) {
        this.id = id;
        this.temp = temp;
        this.humid = humid;
        this.light = light;
        this.soil = soil;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getHumid() {
        return humid;
    }

    public void setHumid(String humid) {
        this.humid = humid;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getSoil() {
        return soil;
    }

    public void setSoil(String soil) {
        this.soil = soil;
    }





}
