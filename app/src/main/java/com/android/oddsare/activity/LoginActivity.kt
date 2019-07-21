package com.android.oddsare.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.oddsare.R
import com.android.oddsare.fragment.SignInFragment

class LoginActivity : AppCompatActivity() {

    val fm = this.supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        fm.beginTransaction()
            .replace(R.id.login_frag_placeholder, SignInFragment())
            .commit()
    }



    companion object {
        private const val TAG = "LoginActivity"
    }
}