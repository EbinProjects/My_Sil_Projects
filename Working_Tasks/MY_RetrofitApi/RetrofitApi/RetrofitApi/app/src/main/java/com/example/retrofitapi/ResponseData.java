package com.example.retrofitapi;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class ResponseData{

	@PrimaryKey(autoGenerate = true)
	private int id;

	public int getId() {
		return id;
	}

	@ColumnInfo
	@SerializedName("Status")
	private String status;

	@ColumnInfo
	@SerializedName("Message")
	private String message;

	@ColumnInfo
	@SerializedName("StoreName")
	private String storeName;

	@ColumnInfo
	@SerializedName("ServerTime")
	private String serverTime;

	@ColumnInfo
	@SerializedName("JobNumber")
	private int jobNumber;

	@ColumnInfo
	@SerializedName("StoreCode")
	private String storeCode;
@ColumnInfo
	@SerializedName("PickerCode")
	private String pickerCode;

@ColumnInfo
	@SerializedName("EmployeeType")
	private String employeeType;



@ColumnInfo
	@SerializedName("StoreID")
	private int storeID;
@ColumnInfo
	@SerializedName("UserID")
	private int userID;

@ColumnInfo
	@SerializedName("FinCode")
	private String finCode;
@ColumnInfo
	@SerializedName("DisplayName")
	private String displayName;
@ColumnInfo
	@SerializedName("TokenID")
	private String tokenID;

	public void setId(int id) {
		this.id = id;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}

	public void setJobNumber(int jobNumber) {
		this.jobNumber = jobNumber;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public void setPickerCode(String pickerCode) {
		this.pickerCode = pickerCode;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}



	public void setStoreID(int storeID) {
		this.storeID = storeID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}



	public void setFinCode(String finCode) {
		this.finCode = finCode;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setTokenID(String tokenID) {
		this.tokenID = tokenID;
	}

	public String getStatus(){
		return status;
	}

	public String getMessage(){
		return message;
	}

	public String getStoreName(){
		return storeName;
	}

	public String getServerTime(){
		return serverTime;
	}

	public int getJobNumber(){
		return jobNumber;
	}

	public String getStoreCode(){
		return storeCode;
	}

	public String getPickerCode(){
		return pickerCode;
	}

	public String getEmployeeType(){
		return employeeType;
	}



	public int getStoreID(){
		return storeID;
	}

	public int getUserID(){
		return userID;
	}


	public String getFinCode(){
		return finCode;
	}

	public String getDisplayName(){
		return displayName;
	}

	public String getTokenID(){
		return tokenID;
	}
}