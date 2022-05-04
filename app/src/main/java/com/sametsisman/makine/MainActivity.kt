package com.sametsisman.makine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun login(view : View){

    }

    fun signUp(view: View){
        val mainToSignUpIntent = Intent(this,SignUpActivity::class.java)
        startActivity(mainToSignUpIntent)
    }
}