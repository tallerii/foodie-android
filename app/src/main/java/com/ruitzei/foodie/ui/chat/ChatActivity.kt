package com.ruitzei.foodie.ui.chat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ruitzei.foodie.R
import com.ruitzei.foodie.model.ChatMessage
import com.ruitzei.foodie.utils.BaseActivity


class ChatActivity : BaseActivity() {

    class MessageViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        internal var messageTextView: TextView
        internal var messageImageView: ImageView
        internal var messengerTextView: TextView

        init {
            messageTextView = itemView.findViewById(R.id.messageTextView) as TextView
            messageImageView = itemView.findViewById(R.id.messageImageView) as ImageView
            messengerTextView = itemView.findViewById(R.id.messengerTextView) as TextView
        }
    }

    val ANONYMOUS = "anonymous"
    private val MESSAGE_SENT_EVENT = "message_sent"
    private var mUsername: String? = null
    private var mSharedPreferences: SharedPreferences? = null

    private var mSendButton: Button? = null
    private var mMessageRecyclerView: RecyclerView? = null
    private var mLinearLayoutManager: LinearLayoutManager? = null
    private var mProgressBar: ProgressBar? = null
    private var mMessageEditText: EditText? = null
    private var mAddMessageImageView: ImageView? = null

    // Firebase instance variables
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mFirebaseUser: FirebaseUser? = null
    private var mFirebaseDatabaseReference: DatabaseReference? = null
    private var mFirebaseAdapter: FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder>? = null

    private var orderId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_chat)
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        // Set default username is anonymous.
        mUsername = ANONYMOUS

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth!!.currentUser

        // Initialize ProgressBar and RecyclerView.
        mProgressBar = findViewById(R.id.progressBar)
        mMessageRecyclerView = findViewById(R.id.messageRecyclerView)
        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager!!.stackFromEnd = true
        mMessageRecyclerView!!.layoutManager = mLinearLayoutManager

        orderId = intent!!.extras!!.getString(ORDER_ID).orEmpty()
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference.child("chats")

        val parser = SnapshotParser<ChatMessage> { dataSnapshot ->
                val friendlyMessage =
                    dataSnapshot.getValue<ChatMessage>(ChatMessage::class.java)
                if (friendlyMessage != null) {
                    friendlyMessage.senderId = dataSnapshot.key.orEmpty()
                }
                friendlyMessage!!
            }

        val messagesRef = mFirebaseDatabaseReference!!.child(orderId)
        val options: FirebaseRecyclerOptions<ChatMessage>  = FirebaseRecyclerOptions.Builder<ChatMessage>()
            .setQuery(messagesRef, parser)
            .build()

        mFirebaseAdapter =
            object : FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder>(options) {
                override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MessageViewHolder {
                    val inflater = LayoutInflater.from(viewGroup.context)
                    return MessageViewHolder(
                        inflater.inflate(
                            R.layout.item_message,
                            viewGroup,
                            false
                        )
                    )
                }

                override fun onBindViewHolder(
                    viewHolder: MessageViewHolder,
                    position: Int,
                    friendlyMessage: ChatMessage
                ) {
                    mProgressBar!!.visibility = ProgressBar.INVISIBLE
                    if (friendlyMessage.message.isNotEmpty()) {
                        viewHolder.messageTextView.text = friendlyMessage.message
                        viewHolder.messageTextView.visibility = TextView.VISIBLE
                        viewHolder.messageImageView.visibility = ImageView.GONE
                    }

                    viewHolder.messengerTextView.text = friendlyMessage.senderId
                }
            }

        mFirebaseAdapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                val friendlyMessageCount = mFirebaseAdapter!!.itemCount
                val lastVisiblePosition =
                    mLinearLayoutManager!!.findLastCompletelyVisibleItemPosition()
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if ((lastVisiblePosition == -1 || ((positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))))) {
                    mMessageRecyclerView!!.scrollToPosition(positionStart)
                }
            }
        })

        mMessageRecyclerView!!.adapter = mFirebaseAdapter

        mMessageEditText = findViewById(R.id.messageEditText)
        mMessageEditText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                mSendButton!!.isEnabled = charSequence.toString().trim { it <= ' ' }.isNotEmpty()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        mSendButton = findViewById(R.id.sendButton)
        mSendButton!!.setOnClickListener {
            val friendlyMessage = ChatMessage(
                mMessageEditText!!.text.toString(),
                mFirebaseUser?.uid.orEmpty()
            )
            mFirebaseDatabaseReference!!.child(orderId)
                .push().setValue(friendlyMessage)
            mMessageEditText!!.setText("")
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in.
        // TODO: Add code to check if user is signed in.
    }

    public override fun onPause() {
        mFirebaseAdapter!!.stopListening()
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        mFirebaseAdapter!!.startListening()
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        val TAG: String = ChatActivity::class.java.simpleName
        val ORDER_ID: String = "orderId"

        fun newIntent(context: Context, orderId: String): Intent {
            val intent = Intent(context, ChatActivity::class.java).apply {
                putExtra(ORDER_ID, orderId)
            }

            return intent
        }
    }
}