package com.example.retrofitapi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface userclass {
@POST("LoginActivity")
Call<Responsess> UserLogin(@Body LoginRequest loginRequest);

}
