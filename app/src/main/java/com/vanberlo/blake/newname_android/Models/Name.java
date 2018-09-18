package com.vanberlo.blake.newname_android.Models;

import com.vanberlo.blake.newname_android.Enumerations.Gender;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Name extends RealmObject {

    @PrimaryKey
    private long id;

    private String name;
    private int gender;

    public Name(){
        this.setName("");
        this.setGender(Gender.FEMALE);
    }

    public Name(long id, String name, Gender gender){
        this.setId(id);
        this.setName(name);
        this.setGender(gender);
    }

    public void setId(long id){
        this.id = id;
    }

    public long getId(){
        return this.id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setGender(Gender gender){
        this.gender = gender.ordinal(); // Store gender as its ordinal value
    }

    public Gender getGender(){
        return Gender.values()[this.gender]; // Create gender enum from ordinal stored in DB
    }
}
