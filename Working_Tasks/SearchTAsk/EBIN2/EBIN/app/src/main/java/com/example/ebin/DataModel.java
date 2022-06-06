package com.example.ebin;

public class DataModel {
    private String name;
    private String dob;

    public DataModel(String name, String dob) {
        this.name = name;
        this.dob = dob;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
