package com.example.loginsqlabin;



import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity
public class Task implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "personName")
    private String personName;

    @ColumnInfo(name = "PhoneNumber")
    private String PhoneNumber;
    @ColumnInfo(name = "Place")
    private String place;

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }

    public int getId() {
        return id;
    }

    public String getPersonName() {
        return personName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
    /*
     * Getters and Setters
     * */

}

