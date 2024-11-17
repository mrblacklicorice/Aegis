package com.cs407.aegis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(private var messages: MutableList<TextMessage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val MESSAGE_TYPE_SENT = 1
    private val MESSAGE_TYPE_RECEIVED = 2

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isSent) MESSAGE_TYPE_SENT else MESSAGE_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == MESSAGE_TYPE_SENT) {
            val view = inflater.inflate(R.layout.sent_message, parent, false)
            SentViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.received_message, parent, false)
            ReceivedViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is SentViewHolder) {
            holder.bind(message)
        } else if (holder is ReceivedViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: TextMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    fun removeMessage(position: Int) {
        messages.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateMessage(position: Int, newMessage: TextMessage) {
        messages[position] = newMessage
        notifyItemChanged(position)
    }

    class SentViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(message: TextMessage) {
            val textView: TextView = view.findViewById(R.id.text_message_body_sent)
            textView.text = message.text
        }
    }

    class ReceivedViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(message: TextMessage) {
            val textView: TextView = view.findViewById(R.id.text_message_body_received)
            textView.text = message.text
        }
    }
}
