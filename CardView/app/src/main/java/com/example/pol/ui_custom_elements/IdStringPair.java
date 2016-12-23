package com.example.pol.ui_custom_elements;

/**
 * Created by pol on 23/12/2016.
 */

public class IdStringPair {
    public int id;
    public String description;

    public IdStringPair(){

    }

    public IdStringPair(int identifier, String desc){
        id = identifier;
        description = desc;
    }

    @Override
    public String toString() {
        return description + " @iD " + id;
    }
}
