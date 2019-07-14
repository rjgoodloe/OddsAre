package com.android.oddsare.fragment

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
import kotlinx.android.synthetic.main.fragment_sign_in.*
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.fragment_sign_in.view.*




class SignInFragment(context: Context) : Fragment(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private val parentContext = context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val view : View = inflater.inflate(R.layout.fragment_sign_in, container, false)

        view.b_sign_in.setOnClickListener(this)
        view.b_no_account.setOnClickListener(this)

        return view
    }

    // [START on_start_check_user]
    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null)
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val intent = Intent(parentContext, MainActivity::class.java)
            startActivity(intent)
        }
    }
    // [END on_start_check_user]


    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

//        showProgressDialog()

        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val intent = Intent(parentContext, MainActivity::class.java)
                    startActivity(intent)
                    activity!!.finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // [START_EXCLUDE]
//                if (!task.isSuccessful) {
//                    status.setText(R.string.auth_failed)
//                }
//                hideProgressDialog()
                // [END_EXCLUDE]
            }
        // [END sign_in_with_email]
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

        return valid
    }

    override fun onClick(v: View) {
        when (v) {
            b_sign_in -> {
                signIn(et_email.text.toString(), et_password.text.toString())
            }
            b_no_account -> {
                try {
                    val fragmentManager = (context as FragmentActivity).supportFragmentManager
                    fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.login_frag_placeholder, NewAccountFragment(parentContext))
                        .commit()

                } catch (e: ClassCastException) {
                    Log.e(TAG, "Can't get fragment manager")
                }


            }
        }
    }

    fun onBackPressed() {
    }


    companion object {
        private const val TAG = "SignInFragment"
    }
}