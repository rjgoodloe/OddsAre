package com.android.oddsare.fragment

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.oddsare.R
import com.android.oddsare.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_create_with_email.*
import kotlinx.android.synthetic.main.fragment_create_with_email.view.*
import model.User


class CreateWithEmailFragment(context: Context) : Fragment() {

    private val parentContext = context
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference


        val view: View = inflater.inflate(R.layout.fragment_create_with_email, container, false)
        view.b_sign_up.setOnClickListener {
            createAccount(et_email.text.toString(), et_password.text.toString())
        }

        return view
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

//        showProgressDialog()

        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(Activity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val newUser = User(et_name.text.toString(),et_username.text.toString(), et_email.text.toString())
                    writeNewUser(auth.currentUser!!.uid, newUser)

                    val intent = Intent(parentContext, MainActivity::class.java)
                    startActivity(intent)
                    (parentContext as Activity).finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(parentContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }

                // [START_EXCLUDE]
//                hideProgressDialog()
                // [END_EXCLUDE]
            }
        // [END create_user_with_email]
    }

    private fun writeNewUser(userId : String, user : User) {
        database.child("users").child(userId).setValue(user)
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = et_email.text.toString()
        if (TextUtils.isEmpty(email)) {
            et_email.error = "Required."
            valid = false
        } else {
            et_email.error = null
        }

        val password = et_password.text.toString()
        if (TextUtils.isEmpty(password)) {
            et_password.error = "Required."
            valid = false
        } else {
            et_password.error = null
        }

        val reenterPassword = et_reenter_password.text.toString()
        when {
            TextUtils.isEmpty(password) -> {
                et_reenter_password.error = "Required."
                valid = false
            }
            password != reenterPassword -> {
                et_reenter_password.error = "Passwords do not match."
                et_password.error = "Passwords do not match."
                valid = false
            }
            else -> et_reenter_password.error = null
        }

        return valid
    }

}