package com.example.jskotlin.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jskotlin.R
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val name = intent.getStringExtra("name")
        val email= intent.getStringExtra("email")
        val phone = intent.getStringExtra("phone number")
        val gender = intent.getStringExtra("gender")



        tvName.text=name.toString()
        tvEmail.text=email.toString()
        tvPhoneNumber.text=phone.toString()
        tvGender.text=gender.toString()
    }
}