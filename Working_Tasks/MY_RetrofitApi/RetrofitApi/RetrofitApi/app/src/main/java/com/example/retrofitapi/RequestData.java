package com.example.retrofitapi;

import com.google.gson.annotations.SerializedName;

public class RequestData{

	@SerializedName("IsDevice")
	private int isDevice;

	@SerializedName("Username")
	private String username;

	@SerializedName("CustomerID")
	private int customerID;

	@SerializedName("Password")
	private String password;

	public int getIsDevice(){
		return isDevice;
	}

	public String getUsername(){
		return username;
	}

	public int getCustomerID(){
		return customerID;
	}

	public String getPassword(){
		return password;
	}
}