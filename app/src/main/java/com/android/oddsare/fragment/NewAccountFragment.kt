package com.android.oddsare.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.android.oddsare.R
import kotlinx.android.synthetic.main.fragment_new_account.*
import kotlinx.android.synthetic.main.fragment_new_account.view.*

class NewAccountFragment : Fragment(), View.OnClickListener {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view : View = inflater.inflate(R.layout.fragment_new_account, container, false)
        view.b_sign_up_email.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View) {
        when (v) {
            b_sign_up_email -> {
                try {

                    val fragmentManager = (context as FragmentActivity).supportFragmentManager
                    fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.login_frag_placeholder, CreateWithEmailFragment())
                        .commit()

                } catch (e: ClassCastException) {
                    Log.e(TAG, "Can't get fragment manager")
                }



            }
            b_sign_in_facebook -> {

            }
        }
    }

    companion object {
        private const val TAG = "NewAccountFragment"
    }
}