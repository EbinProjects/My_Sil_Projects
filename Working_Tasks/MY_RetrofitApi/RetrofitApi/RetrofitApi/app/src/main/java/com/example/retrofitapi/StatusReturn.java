package com.example.retrofitapi;

import com.google.gson.annotations.SerializedName;

public class StatusReturn{

	@SerializedName("Status")
	private int status;

	@SerializedName("Message")
	private String message;

	public int getStatus(){
		return status;
	}

	public String getMessage(){
		return message;
	}
}