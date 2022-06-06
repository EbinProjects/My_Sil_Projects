package com.example.retrofitapi;


import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
private static Retrofit getRetrofit(){
    HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient okHttpClient=new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
    Retrofit retrofit=new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("http://124.124.79.123:1892/Choithrams/").client(okHttpClient).build();
return retrofit;
}
public  static userclass getUserClass(){
    userclass userclass=getRetrofit().create(com.example.retrofitapi.userclass.class);
    return userclass;

}
}
