package com.example.loginsqlabin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {
    EditText userName,Password;
    Button login,Reset;
    int counter=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        userName=findViewById(R.id.username);
        Password=findViewById(R.id.pass);
        login=findViewById(R.id.login);
        Reset=findViewById(R.id.reset);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText().toString().equals("admin@gmail.com") &&
                        Password.getText().toString().equals("admin")) {
                    Toast.makeText(getApplicationContext(),
                            "Welcome...",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(login.this,MainActivity.class));
                }else{
                    counter--;
                    Toast.makeText(getApplicationContext(), "Wrong Credentials"+counter+"attempts remaining",Toast.LENGTH_SHORT).show();





                    if (counter == 0) {
                        login.setEnabled(false);
                    }
                }
            }
        });

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName.setText("");
                Password.setText("");
            }
        });
    }
}
