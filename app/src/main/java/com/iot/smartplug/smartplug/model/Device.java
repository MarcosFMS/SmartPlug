package com.iot.smartplug.smartplug.model;

import java.util.Calendar;

/**
 * Created by Marcos on 21/03/2017.
 */

public class Device {

    private int id_device;

    private String id;
    private String ip;
    private boolean on;
    /*
    private Calendar lastTimeon;
    private Calendar lastTimeoff;*/
    private String name;

    public Device(String nome, int id, String ip, boolean on, Calendar lastTimeon, Calendar lastTimeoff) {
        this.name = nome;
        this.id_device = id;
        this.ip = ip;
        this.on = on;
        /*
        this.lastTimeon = lastTimeon;
        this.lastTimeoff = lastTimeoff;*/
    }

    public Device() {
    }

    /*
    public Calendar getLastTimeoff() {
        return lastTimeoff;
    }

    public void setLastTimeoff(Calendar lastTimeoff) {
        this.lastTimeoff = lastTimeoff;
    }

    public Calendar getLastTimeon() {
        return lastTimeon;
    }

    public void setLastTimeon(Calendar lastTimeon) {
        this.lastTimeon = lastTimeon;
    }
    */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id_device;
    }

    public void setId(int id) {
        this.id_device = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public String toString(){
        return this.getName();
    }

}
