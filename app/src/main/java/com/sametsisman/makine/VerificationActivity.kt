package com.sametsisman.makine

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_verification.*
import java.util.concurrent.TimeUnit

class VerificationActivity : AppCompatActivity() {
    private var mCallback : PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private var mVerificationId : String? = null
    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        auth = Firebase.auth


        mVerificationId = intent.getStringExtra("verificationId")

        resendCodeTextView.setOnClickListener {

            mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(applicationContext,e.message, Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(newVerificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    mVerificationId = newVerificationId
                }
            }
        }
    }

    fun submit(view : View){
        val code = codeEditText.text.toString().trim()

        if(TextUtils.isEmpty(code)){
            Toast.makeText(applicationContext,"Please enter verification code...",Toast.LENGTH_SHORT).show()
        }else{
            verifyPhoneNumberWithCode(mVerificationId,code)
        }
    }

    private fun verifyPhoneNumberWithCode(verificationId : String? , code:String){


        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                val phone = auth.currentUser!!.phoneNumber
                Toast.makeText(this,"Logging in as $phone",Toast.LENGTH_SHORT).show()

                //start activity
                val intent = Intent(this@VerificationActivity,HomeActivity::class.java)
                startActivity(intent)
                finish()

            }.addOnFailureListener{
                Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            }
    }
}