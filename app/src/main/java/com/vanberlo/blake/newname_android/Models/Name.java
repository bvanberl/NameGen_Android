package com.vanberlo.blake.newname_android.Models;

import com.vanberlo.blake.newname_android.Enumerations.Gender;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * A Realm Object class containing name information
 */
public class Name extends RealmObject {

    @PrimaryKey
    private long id;

    private String name;
    private int gender;

    /**
     * Default constructor for Name object
     */
    public Name(){
        this.setName("");
        this.setGender(Gender.FEMALE);
    }

    /**
     * Overloaded constructor for name object
     * @param id - a unique identifier for the newly generated Name object
     * @param name - the string value of the name
     * @param gender - the gender of the name
     */
    public Name(long id, String name, Gender gender){
        this.setId(id);
        this.setName(name);
        this.setGender(gender);
    }

    /**
     * Creates a temporary name object
     * @param name - the string value of the name
     * @param gender - the gener of the name
     */
    public Name(String name, Gender gender){
        this.setId(-1);
        this.setName(name);
        this.setGender(gender);
    }

    /**
     * Below are getters and setters for the Name object's attributes
     */
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
        this.gender = gender.ordinal(); // Store the ordinal value of the gender for the database
    }

    public Gender getGender(){
        return Gender.values()[this.gender]; // Get gender enum from ordinal stored in database
    }
}
