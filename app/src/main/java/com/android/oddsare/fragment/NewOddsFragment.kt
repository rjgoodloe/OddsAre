package com.android.oddsare.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.oddsare.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_new_odds.*
import kotlinx.android.synthetic.main.fragment_new_odds.view.*

class NewOddsFragment : Fragment(), OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_new_odds, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference


        view.b_send_odds.setOnClickListener(this)
        return view
    }


    override fun onClick(v: View?) {
        when (v) {
            b_send_odds -> {
                sendRequest(et_email_temp.text.toString())
            }
        }
    }

    private fun sendRequest(username: String) {

        Log.d(TAG, "SEND REQUEST")

        try {
            var friendEmail = "" //database.child("Emails").child(splitString(auth.currentUser!!.email!!)
            database.child("Emails").child(username).addValueEventListener(object :
                ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    friendEmail = dataSnapshot.value as String
                    friendEmail = splitString(friendEmail)
                    database.child("Users").child(friendEmail).child("Requests").push()
                        .setValue(splitString(auth.currentUser!!.email!!))


                }

                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }


            })
        } catch (t: Throwable) {
            Toast.makeText(
                activity as Context, "User does not exist",
                Toast.LENGTH_SHORT
            ).show()
        }


    }

    private fun splitString(str: String): String {
        val split = str.split("@")
        return split[0]
    }

    companion object {
        const val TAG = "NewOddsFragment"
    }

}