package com.example.firstkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firstkotlin.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var isGenderSelected: Boolean = false
    var selectedText = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        radioGrp.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { rGroup, checkedId ->
            val radioButtonID: Int = radioGrp.getCheckedRadioButtonId()
            val radioButton: View = radioGrp.findViewById(radioButtonID)
            val idx: Int = radioGrp.indexOfChild(radioButton)
            val r = radioGrp.getChildAt(idx) as RadioButton
            selectedText = r.text.toString()
            isGenderSelected = true

            println(idx) //For print Id
            println(selectedText) //For print Text
        })
        btnSubmit.setOnClickListener(View.OnClickListener {
            when {
                etName.text.isEmpty() -> {
                    Toast.makeText(this, "Please enter your name", Toast.LENGTH_LONG).show()
                }

                etEmail.text.isEmpty() -> {
                    Toast.makeText(this, "Please enter your email", Toast.LENGTH_LONG).show()
                }

                !isValidEmail(etEmail.text.toString()) -> {
                    Toast.makeText(this, "Plaese enter a valid email address", Toast.LENGTH_LONG)
                        .show()

                }
                etPhone.text.isEmpty() -> {
                    Toast.makeText(this, "Please enter your Phone Number", Toast.LENGTH_LONG)
                        .show();

                }
                etPhone.text.toString().length < 6 || etPhone.text.toString().length > 12 -> {
                    Toast.makeText(this, "Phone number have 10 digits", Toast.LENGTH_LONG).show()
                }
                !isGenderSelected -> {
                    Toast.makeText(this, "Please select your Gender", Toast.LENGTH_LONG).show()
                }
                !cbTerms.isChecked -> {
                    Toast.makeText(this, "Please select our terms and condition", Toast.LENGTH_LONG)
                        .show()
                }

                else -> {

                    Toast.makeText(this, "User Registered successfully", Toast.LENGTH_LONG).show();
                    val intent = Intent(this@MainActivity, ResultActivity::class.java)
                    intent.putExtra("name", etName.text.toString())
                    intent.putExtra("email", etEmail.text.toString())
                    intent.putExtra("phone number", etPhone.text.toString())
                    intent.putExtra("gender", selectedText)
                    startActivity(intent)

                }
            }
        })
    }
}

private fun isValidEmail(email: String): Boolean {
    return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
