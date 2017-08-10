package com.zack.tinga.applicationtest;

/**
 * Created by admin on 2017/07/31.
 */

public class Skill {

    private String name, id;
    private int hobbieRating;

    public Skill() {
    }

    public Skill(String id, String name, int hobbieRating) {
        this.name = name;
        this.id = id;
        this.hobbieRating = hobbieRating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHobbieRating() {
        return hobbieRating;
    }

    public void setHobbieRating(int hobbieRating) {
        this.hobbieRating = hobbieRating;
    }
}
