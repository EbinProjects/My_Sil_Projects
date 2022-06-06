package com.example.retrofitapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
  Button sign;
  TextInputEditText user,password;
    String DisplayName="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        sign=findViewById(R.id.containedButton);
        user=findViewById(R.id.username);
        password=findViewById(R.id.password);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(user.getText().toString())||TextUtils.isEmpty(password.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Provide Details", Toast.LENGTH_SHORT).show();
                }else {
                    lonin();
                }
            }
        });
    }

    private void lonin() {
        LoginRequest loginRequest=new LoginRequest();
        loginRequest.setUsername(user.getText().toString());
        loginRequest.setPassword(password.getText().toString());
        Call<Responsess> loginResponseCall=ApiClient.getUserClass().UserLogin(loginRequest);

       loginResponseCall.enqueue(new Callback<Responsess>() {
           @Override
           public void onResponse(Call<Responsess> call, Response<Responsess> response) {
               if(response.isSuccessful()){
                   Responsess response1=response.body();
                   ResponseData responseData=response1.getResponseData();
                   StatusReturn statusReturn=response1.getStatusReturn();
                   String TokenID=responseData.getTokenID().toString();
                   String Status=responseData.getStatus().toString();
                   int UserID=responseData.getUserID();
                    DisplayName=responseData.getDisplayName().toString();
                   String Message=responseData.getMessage().toString();
                   int Jobnumber=responseData.getJobNumber();
                   int StaoreId=responseData.getStoreID();
                   String storeCode=responseData.getStoreCode().toString();
                   String storeName=responseData.getStoreName().toString();
                   String pickerCode=responseData.getPickerCode().toString();
                   String finCode=responseData.getFinCode().toString();
                   String EmplayeTyepe=responseData.getEmployeeType().toString();
                   String ServerTime=responseData.getServerTime().toString();
//                   String royundOf=responseData.getRoundOff().toString();
//                   int roundofLimit=responseData.getRoundOffLimit();
                   Add(TokenID,Status,UserID,DisplayName,Message,Jobnumber,StaoreId,storeName,storeCode,pickerCode,finCode,EmplayeTyepe,ServerTime);

                   Log.e("message", "abi==> "+ response1 );



               }



           }

           @Override
           public void onFailure(Call<Responsess> call, Throwable t) {
               Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
               Log.e("message", "abin==> "+ t.getLocalizedMessage() );
           }
       });
    }

    private void Add(String tokenID, String status, int userID, String displayName, String message, int jobnumber, int staoreId, String storeName, String storeCode, String pickerCode, String finCode, String emplayeTyepe, String serverTime) {
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                ResponseData task = new ResponseData();
                task.setTokenID(tokenID);
                task.setStatus(status);
                task.setUserID(userID);
                task.setDisplayName(displayName);
                task.setMessage(message);
                task.setJobNumber(jobnumber);
                task.setStoreID(staoreId);
                task.setStoreCode(storeCode);
                task.setStoreName(storeName);
                task.setFinCode(finCode);
                task.setPickerCode(pickerCode);
//                task.getRoundOff(royundOf);
                task.setEmployeeType(emplayeTyepe);
//                task.setRoundOffLimit(roundofLimit);
                task.setServerTime(serverTime);



                //adding to database


                DBClient.getInstance(getApplicationContext()).getAppDatabase()
                        .noteDao()
                        .insert(task);


                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();

                startActivity(new Intent(getApplicationContext(),MainActivity.class).putExtra("data",displayName));

                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();




            }
        }


        SaveTask st = new SaveTask();
        st.execute();


    }
    public void onBackPressed() {
        finish();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    }

