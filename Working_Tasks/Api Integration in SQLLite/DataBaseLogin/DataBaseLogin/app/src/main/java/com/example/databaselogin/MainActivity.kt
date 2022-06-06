package com.example.databaselogin


import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_register.*


class MainActivity : AppCompatActivity() {
    var LogInButton: Button? = null
    lateinit var RegisterButton: Button
    var Email: EditText? = null
    var Password: EditText? = null
    var EmailHolder: String? = null
    var PasswordHolder: String? = null
    var sqLiteDatabaseObj: SQLiteDatabase? = null
    var sqLiteHelper: SQLiteHelper? = null
    var cursor: Cursor? = null
    var TempPassword = "NOT_FOUND"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LogInButton = findViewById<View>(R.id.buttonLogin) as Button
        RegisterButton = buttonRegister
        Email = findViewById<View>(R.id.editEmail) as EditText
        Password = findViewById<View>(R.id.editPassword) as EditText
        sqLiteHelper = SQLiteHelper(this)

        //Adding click listener to log in button.
        LogInButton!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {

                // Calling EditText is empty or no method.
                CheckEditTextStatus()

                // Calling login method.
                LoginFunction()
            }
        })
        RegisterButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
        })
    }
        // Adding click listener to register button.
        /* RegisterButton!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {

                // Opening new user registration activity using intent on button click.
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        })
    }
*/
        // Login function starts from here.
        /* fun LoginFunction() {
        if (EditTextEmptyHolder!!) {

            // Opening SQLite database write permission.
            sqLiteDatabaseObj = sqLiteHelper!!.writableDatabase

            // Adding search email query to cursor.
            cursor = sqLiteDatabaseObj.query(
                SQLiteHelper.TABLE_NAME,
                null,
                " " + SQLiteHelper.Table_Column_2_Email + "=?",
                arrayOf<String>(EmailHolder),
                null,
                null,
                null
            )
            while (cursor.moveToNext()) {
                if (cursor.isFirst()) {
                    cursor.moveToFirst()

                    // Storing Password associated with entered email.
                    TempPassword =
                        cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_3_Password))

                    // Closing cursor.
                    cursor.close()
                }
            }

            // Calling method to check final result ..
            CheckFinalResult()
        } else {

            //If any of login EditText empty then this block will be executed.
            Toast.makeText(
                this@MainActivity,
                "Please Enter UserName or Password.",
                Toast.LENGTH_LONG
            ).show()
        }
    }*/

        // Checking EditText is empty or not.
        fun CheckEditTextStatus() {

            // Getting value from All EditText and storing into String Variables.
            EmailHolder = Email!!.text.toString()
            PasswordHolder = Password!!.text.toString()

            // Checking EditText is empty or no using TextUtils.
                if (TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)) {
                    Toast.makeText(this@MainActivity, "enter the valid Email format", Toast.LENGTH_LONG).show()

                } else {
                    checkUser()
                    Toast.makeText(this@MainActivity, "enter the valid Email format", Toast.LENGTH_LONG).show()

                }
        }

        // Checking entered password from SQLite database email associated password.
      /*  fun CheckFinalResult() {
            if (TempPassword.equals(PasswordHolder, ignoreCase = true)) {
                Toast.makeText(this@MainActivity, "Login Successful", Toast.LENGTH_LONG).show()

                // Going to Dashboard activity after login success message.
                val intent = Intent(this@MainActivity, DashboardActivity::class.java)

                // Sending Email to Dashboard Activity using intent.
                intent.putExtra(UserEmail, EmailHolder)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "UserName or Password is Wrong, Please Try Again.",
                    Toast.LENGTH_LONG
                ).show()
            }
            TempPassword = "NOT_FOUND"
        }*/

        /* companion object {
        const val UserEmail = ""
    }*/
    }
