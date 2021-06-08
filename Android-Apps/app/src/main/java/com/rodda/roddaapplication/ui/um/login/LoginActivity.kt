package com.rodda.roddaapplication.ui.um.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.rodda.roddaapplication.MainActivity
import com.rodda.roddaapplication.R
import com.rodda.roddaapplication.databinding.ActivityLoginBinding
import com.rodda.roddaapplication.supp.LoadingDialog
import com.rodda.roddaapplication.ui.um.register.RegistrerFormActivity
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var fbAuth: FirebaseAuth
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        fbAuth = FirebaseAuth.getInstance()
        loadingDialog = LoadingDialog(this)

        loginBinding.tvRegis.setOnClickListener(this)
        loginBinding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_regis -> {
                startActivity(Intent(this, RegistrerFormActivity::class.java))
            }
            R.id.btn_login -> {
                login()
            }
        }
    }

    private fun login(){
        val email = loginBinding.editUsername.text.toString()
        val password = loginBinding.editPassword.text.toString()

        if(email.isEmpty()){
            loginBinding.editUsername.error = "Required"
            return
        }
        if(password.isEmpty()){
            loginBinding.editPassword.error = "Required"
            return
        }

        loadingDialog.startDialog()

        fbAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    loadingDialog.dismissDialog()
                    startActivity(Intent(this, MainActivity::class.java))
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()

                } else {
                    loadingDialog.dismissDialog()
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }

    }

    override fun onStart() {
        super.onStart()
        if(fbAuth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
        exitProcess(-1)
    }
}