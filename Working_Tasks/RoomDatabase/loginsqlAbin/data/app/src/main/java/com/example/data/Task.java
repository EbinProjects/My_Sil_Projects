package com.example.data;

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

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "Country")
    private String Country;

    @ColumnInfo(name = "gender")
    private String gender;

    public String getCountry() {
        return Country;
    }

    public String getGender() {
        return gender;
    }

    public String getState() {
        return state;
    }

    public String getDistrict() {
        return district;
    }

    @ColumnInfo(name = "state")
    private String state;

    @ColumnInfo(name = "district")
    private String district;
    @ColumnInfo(name = "check")
    private String check;

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
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

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}

