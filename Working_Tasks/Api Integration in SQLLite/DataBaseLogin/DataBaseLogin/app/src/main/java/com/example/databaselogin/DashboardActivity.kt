package com.example.databaselogin


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class DashboardActivity : AppCompatActivity() {
    var EmailHolder: String? = null
    var Email: TextView? = null
    var LogOUT: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        Email = findViewById<View>(R.id.textView1) as TextView
        LogOUT = findViewById<View>(R.id.button1) as Button
        val intent = intent

        // Receiving User Email Send By MainActivity.
      //  EmailHolder = intent.getStringExtra(MainActivity.UserEmail)

        // Setting up received email to TextView.
        Email!!.text = Email!!.text.toString() + EmailHolder

        // Adding click listener to Log Out button.
        LogOUT!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {

                //Finishing current DashBoard activity on button click.
                finish()
                Toast.makeText(this@DashboardActivity, "Log Out Successful", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
}