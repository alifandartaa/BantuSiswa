package com.example.bantusiswa.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bantusiswa.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        val TAG = LoginActivity.javaClass.simpleName
    }

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val tvGoToRegis: TextView = findViewById(R.id.tv_register)
        val btnGoToHome: Button = findViewById(R.id.btn_login_enter)
        tvGoToRegis.setOnClickListener(this)
        btnGoToHome.setOnClickListener(this)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            if (currentUser.isEmailVerified) {
                val homeIntent = Intent(this@LoginActivity, MapsActivity::class.java)
                startActivity(homeIntent)
            }
        }

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.tv_register -> {
                    val registerIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(registerIntent)
                }
                R.id.btn_login_enter -> {
                    auth.signInWithEmailAndPassword(
                            et_login_email.text.toString(),
                            et_login_password.text.toString()
                        )
                        .addOnCompleteListener(this) {
                            if (it.isSuccessful) {
                                Log.d(TAG, "signInWithEmail:success")
                                val user = auth.currentUser
                                if (user != null) {
                                    if (user.isEmailVerified) {
                                        val homeIntent =
                                            Intent(this@LoginActivity, MapsActivity::class.java)
                                        startActivity(homeIntent)
                                    } else {
                                        Toast.makeText(
                                            baseContext,
                                            "Not Verified",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }else{
                                Toast.makeText(baseContext, "Authentication Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }
}
