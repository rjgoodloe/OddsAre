package com.android.oddsare.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.oddsare.R
import com.android.oddsare.model.OddsChallenge
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.fragment_notifications.view.*
import kotlinx.android.synthetic.main.list_item_odds_notification.view.*

class NotificationsFragment : Fragment() {


    private lateinit var database: DatabaseReference
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var adapter: NotificationAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_notifications, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        databaseReference = database.child("Odds").child(splitString(auth.currentUser!!.email!!))


        view.recycler_notifications.layoutManager = LinearLayoutManager(activity as Context)


        return view
    }


    override fun onStart() {
        super.onStart()

        adapter = NotificationAdapter(activity as Context, databaseReference)
        recycler_notifications.adapter = adapter
    }

    private fun splitString(str: String): String {
        val split = str.split("@")
        return split[0]
    }

    private class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(notification: OddsChallenge) {
            itemView.tv_odds_info.text = notification.info
            itemView.tv_user.text = "sent by ${notification.user}"
        }
    }

    private class NotificationAdapter(
        private val context: Context,
        databaseReference: DatabaseReference
    ) : RecyclerView.Adapter<NotificationViewHolder>() {

        private val childEventListener: ChildEventListener?

        private val notificationKeys = ArrayList<String>()
        private val notificationList = ArrayList<OddsChallenge>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.list_item_odds_notification, parent, false)
            return NotificationViewHolder(view)
        }

        override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
            holder.bind(notificationList[position])

        }

        override fun getItemCount(): Int = notificationList.size


        init {


            // Create child event listener
            // [START child_event_listener_recycler]
            val childEventListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.key!!)

                    // A new comment has been added, add it to the displayed list
                    val comment = dataSnapshot.getValue(OddsChallenge::class.java)

                    // [START_EXCLUDE]
                    // Update RecyclerView
                    notificationKeys.add(dataSnapshot.key!!)
                    notificationList.add(comment!!)
                    notifyItemInserted(notificationList.size - 1)
                    // [END_EXCLUDE]
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                    Log.d(TAG, "onChildChanged: ${dataSnapshot.key}")

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so displayed the changed comment.
                    val newComment = dataSnapshot.getValue(OddsChallenge::class.java)
                    val commentKey = dataSnapshot.key

                    // [START_EXCLUDE]
                    val commentIndex = notificationKeys.indexOf(commentKey)
                    if (commentIndex > -1 && newComment != null) {
                        // Replace with the new data
                        notificationList[commentIndex] = newComment

                        // Update the RecyclerView
                        notifyItemChanged(commentIndex)
                    } else {
                        Log.w(TAG, "onChildChanged:unknown_child: $commentKey")
                    }
                    // [END_EXCLUDE]
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.key!!)

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so remove it.
                    val commentKey = dataSnapshot.key

                    // [START_EXCLUDE]
                    val commentIndex = notificationKeys.indexOf(commentKey)
                    if (commentIndex > -1) {
                        // Remove data from the list
                        notificationKeys.removeAt(commentIndex)
                        notificationList.removeAt(commentIndex)

                        // Update the RecyclerView
                        notifyItemRemoved(commentIndex)
                    } else {
                        Log.w(TAG, "onChildRemoved:unknown_child:" + commentKey!!)
                    }
                    // [END_EXCLUDE]
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                    Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)

                    // A comment has changed position, use the key to determine if we are
                    // displaying this comment and if so move it.
                    val movedComment = dataSnapshot.getValue(OddsChallenge::class.java)
                    val commentKey = dataSnapshot.key

                    // ...
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "postComments:onCancelled", databaseError.toException())
                    Toast.makeText(
                        context, "Failed to load comments.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            databaseReference.addChildEventListener(childEventListener)
            // [END child_event_listener_recycler]

            // Store reference to listener so it can be removed on app stop
            this.childEventListener = childEventListener
        }
    }


    companion object {

        private const val TAG = "NotificationFragment"
    }
}

