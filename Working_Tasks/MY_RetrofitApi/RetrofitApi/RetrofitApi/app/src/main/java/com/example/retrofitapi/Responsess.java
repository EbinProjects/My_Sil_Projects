package com.example.retrofitapi;

import com.google.gson.annotations.SerializedName;

public class Responsess {

	@SerializedName("ResponseData")
	private ResponseData responseData;

	@SerializedName("StatusReturn")
	private StatusReturn statusReturn;

	public ResponseData getResponseData(){
		return responseData;
	}

	public StatusReturn getStatusReturn(){
		return statusReturn;
	}
}