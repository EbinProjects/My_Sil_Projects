package com.example.databaselogin

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class SQLiteHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(database: SQLiteDatabase) {
        val CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + Table_Column_ID + " INTEGER PRIMARY KEY, " + Table_Column_1_Name + " VARCHAR, " + Table_Column_2_Email + " VARCHAR, " + Table_Column_3_Password + " VARCHAR)"
        database.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    @SuppressLint("Recycle")
    fun EmailHandler(emailHolder: String): Boolean {
        var email:Boolean=false
        val db = this.writableDatabase
   val cursor=db.query(TABLE_NAME,
            null,
       " $Table_Column_2_Email=?",
            arrayOf(emailHolder),
            null,
            null,
            null
        )
        if (cursor.count==0){
        email=false}
        else{
            email=true
        }

return email
    }

    fun addHandler(employee: employeee): Long {
        val id: Long
        val values = ContentValues()
        values.put(Table_Column_1_Name,employee.Name)
        values.put(Table_Column_2_Email,employee.Email)
        values.put(Table_Column_3_Password,employee.password)
        val db = this.writableDatabase
        id = db.insert(TABLE_NAME, null, values)
        Log.e("TAG", "addHandler: $id" )

        db.close()
        return id

    }


    companion object {
        var DATABASE_NAME = "UserDataBase"
        const val TABLE_NAME = "UserTable"
        const val Table_Column_ID = "id"
        const val Table_Column_1_Name = "name"
        const val Table_Column_2_Email = "email"
        const val Table_Column_3_Password = "password"
    }
}