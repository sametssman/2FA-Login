package com.sametsisman.makine

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_verification.*
import java.util.concurrent.TimeUnit

class SignUpActivity : AppCompatActivity() {
    private var mCallback : PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private var forceResendingToken : PhoneAuthProvider.ForceResendingToken? = null
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth
        initDropdownMenu()

        createAccountButton.setOnClickListener {
            val email = emailSignUpEditText.text.toString()
            val phone = phoneNumberSignUpEditText.text.toString()
            val password = passwordSignUpEditText.text.toString()
            val confirmPassword = passwordConfirmSignUpEditText.text.toString()
            val company = companySignUpEditText.text.toString()
            val sector = companySectorSignUpText.text.toString()


            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phone)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(mCallback!!)          // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)

        }

        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(applicationContext,e.message, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                val intent = Intent(this@SignUpActivity,VerificationActivity::class.java)
                intent.putExtra("verificationId",verificationId)
                intent.putExtra("forceResendingToken",token)
                startActivity(intent)
            }
        }
    }



    fun initDropdownMenu(){
        val items = listOf("Agriculture","Animal supplies", "Construction", "Electronic","Food baverage", "Logistic","Service","Textile")
        val adapter = ArrayAdapter(baseContext, R.layout.list_item, items)
        (companySectorSignUpTextField.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }
}