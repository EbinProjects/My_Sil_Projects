package com.example.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Person implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "Email")
    private String Email;

    @ColumnInfo(name = "Fullname")
    private String Fullname;

    @ColumnInfo(name = "Username")
    private String Username;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "Data")
    private String Data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getData() {
        return Data;

    }
    public void setData(String Data){
        this.Data=Data;
    }
}



