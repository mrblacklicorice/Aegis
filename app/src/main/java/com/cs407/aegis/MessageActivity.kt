package com.cs407.aegis

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.EditText
import android.widget.ImageButton
import android.content.Context

class MessageActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private val messageList = mutableListOf<TextMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.imessage)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chat"

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        recyclerView = findViewById(R.id.messagesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        messageAdapter = MessageAdapter(messageList)
        recyclerView.adapter = messageAdapter

        loadMessages()

        val messageInput: EditText = findViewById(R.id.messageInput)
        val sendButton: ImageButton = findViewById(R.id.sendButton)

        sendButton.setOnClickListener {
            val messageText = messageInput.text.toString()
            if (messageText.isNotEmpty()) {
                val sentMessage = TextMessage(id = System.currentTimeMillis().toString(), text = messageText, isSent = true)
                messageAdapter.addMessage(sentMessage)
                messageInput.setText("")

                // Simulate receiving the same message
                val receivedMessage = TextMessage(id = System.currentTimeMillis().toString(), text = messageText, isSent = false)
                messageAdapter.addMessage(receivedMessage)

                saveMessages()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun loadMessages() {
        val sharedPreferences = getSharedPreferences("Messages", Context.MODE_PRIVATE)
        val messagesString = sharedPreferences.getString("messages", null)
        messagesString?.let {
            it.split("<!!>").forEach { line ->
                val parts = line.split("<!>")
                if (parts.size >= 3) {
                    val message = TextMessage(id = parts[0], text = parts[1], isSent = parts[2].toBoolean())
                    messageList.add(message)
                }
            }
            messageAdapter.notifyDataSetChanged()
        }
    }

    private fun saveMessages() {
        val sharedPreferences = getSharedPreferences("Messages", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val messagesString = messageList.joinToString("<!!>") { message ->
            "${message.id}<!>${message.text}<!>${message.isSent}"
        }
        editor.putString("messages", messagesString)
        editor.apply()
    }
}
