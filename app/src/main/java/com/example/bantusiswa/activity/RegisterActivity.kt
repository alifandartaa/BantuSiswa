package com.example.bantusiswa.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.bantusiswa.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        val TAG = RegisterActivity.javaClass.simpleName
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val btnDoRegis : Button = findViewById(R.id.btn_login_doregis)


        btnDoRegis.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.btn_login_doregis -> {

                    auth.createUserWithEmailAndPassword(et_regis_email.text.toString(), et_regis_password.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if(task.isSuccessful){
                                Log.d(TAG, "createUserWithEmail:success")
                                val user = auth.currentUser

                                user?.sendEmailVerification()
                                    ?.addOnCompleteListener(this){
                                        if(task.isSuccessful){
                                            Toast.makeText(baseContext, "Verification email sent to ${user.email}", Toast.LENGTH_SHORT).show()
                                        }else{
                                            Log.d(TAG, "sendEmailVerification", it.exception)
                                            Toast.makeText(baseContext,
                                                "Failed to send verification email.",
                                                Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }
                        }
//                    val loginIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
//                    startActivity(loginIntent)
                }
            }
        }
    }
}
