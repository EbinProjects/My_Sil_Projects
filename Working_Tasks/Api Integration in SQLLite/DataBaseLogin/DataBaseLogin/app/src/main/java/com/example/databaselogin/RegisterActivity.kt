package com.example.databaselogin


import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.regex.Matcher
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity() {
    var Email: EditText? = null
    var Password: EditText? = null
    var Name: EditText? = null
    var Register: Button? = null
    var NameHolder: String? = null
    var EmailHolder: String? = null
    var PasswordHolder: String? = null
    var EditTextEmptyHolder: Boolean? = null
 lateinit var sqLiteDatabaseObj: SQLiteDatabase
    var SQLiteDataBaseQueryHolder: String? = null
    var sqLiteHelper: SQLiteHelper? = null
    var cursor: Cursor? = null
    var F_Result = "Not_Found"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        Register = findViewById<View>(R.id.buttonRegister) as Button
        Email = findViewById<View>(R.id.editEmail) as EditText
        Password = findViewById<View>(R.id.editPassword) as EditText
        Name = findViewById<View>(R.id.editName) as EditText
        sqLiteHelper = SQLiteHelper(this)

        // Adding click listener to register button.
        Register!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                // Checking EditText is empty or Not.
                CheckEditTextStatus()
            }
        })
    }

    // Empty edittext after done inserting process method.
    fun EmptyEditTextAfterDataInsert() {
        Name!!.text.clear()
        Email!!.text.clear()
        Password!!.text.clear()
    }

    // Method to check EditText is empty or Not.
    fun CheckEditTextStatus() {

        // Getting value from All EditText and storing into String Variables.
        NameHolder = Name!!.text.toString()
        EmailHolder = Email!!.text.toString()
        PasswordHolder = Password!!.text.toString()
        EditTextEmptyHolder =
            (TextUtils.isEmpty(NameHolder) || TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(
                PasswordHolder
            ))

        if (EditTextEmptyHolder as Boolean) {
            Toast.makeText(
                this@RegisterActivity,
                "Please Fill All The Required Fields.",
                Toast.LENGTH_LONG
            ).show()
        }else{
     emailAndPAssword(EmailHolder!!, PasswordHolder!!, NameHolder!!);
        }
    }

    private fun emailAndPAssword(emailHolder: String, passwordHolder: String,nameHolder: String) {
    if ( !Patterns.EMAIL_ADDRESS.matcher(emailHolder).matches()) {
        Toast.makeText(this@RegisterActivity, "enter the valid Email format", Toast.LENGTH_LONG)
            .show()
//        if (passwordHolder.length > 8 && !isValidPassword(passwordHolder)) {
//            Toast.makeText(
//                this@RegisterActivity,
//                "please set powerful password min 8 characters",
//                Toast.LENGTH_LONG
//            ).show()
//    }
    }
    else{
        insertionandEmailcheck(emailHolder, passwordHolder, nameHolder);
    }
    }

    private fun insertionandEmailcheck(emailHolder: String, passwordHolder: String, nameHolder: String) {
        val employee = employeee(nameHolder, emailHolder, passwordHolder)
        if (emailcheck(employee)) {
            Toast.makeText(this@RegisterActivity, "this email is already exits", Toast.LENGTH_LONG)
                .show()

        } else {
            val insertId = sqLiteHelper!!.addHandler(employee)
            if (insertId == -1L) {
                Toast.makeText(
                    this@RegisterActivity,
                    "this email is already exits",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                EmptyEditTextAfterDataInsert()
                Toast.makeText(this@RegisterActivity, "added", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun emailcheck(employeee: employeee): Boolean {
        return sqLiteHelper!!.EmailHandler(employeee.Email.toString())

    }



    fun isValidPassword(password: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }

}