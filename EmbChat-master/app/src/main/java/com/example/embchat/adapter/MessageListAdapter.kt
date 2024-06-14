package com.example.embchat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.embchat.adapter.MessageListAdapter.MessageViewHolder
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.embchat.R
import com.example.embchat.data.entity.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageListAdapter : ListAdapter<Message, MessageViewHolder>(MESSAGE_COMPARATOR){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder.create(parent,viewType)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    override fun getItemViewType(position: Int): Int {
        val current = getItem(position)
        return if(current.sentByMe)
            SENT_BY_ME
        else
            SENT_BY_OTHER
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       private val messageText:TextView = itemView.findViewById(R.id.message_me_text)
       private val messageDate:TextView = itemView.findViewById(R.id.message_me_date)
       private val messageTime:TextView = itemView.findViewById(R.id.message_me_time)


        fun bind(message: Message) {
            messageText.text = message.text

            val date = Date(message.timestamp)
            val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

            messageDate.text = dateFormat.format(date)
            messageTime.text = timeFormat.format(date)
        }

        companion object {
            fun create(parent: ViewGroup, currentUser:Int): MessageViewHolder {
                val view: View = if(currentUser == SENT_BY_ME) {
                    LayoutInflater.from(parent.context).inflate(R.layout.chat_me_item, parent, false)
                } else{
                    LayoutInflater.from(parent.context).inflate(R.layout.chat_other_user_item, parent, false)
                }
                return MessageViewHolder(view)
            }
        }
    }



    companion object {
        private const val SENT_BY_ME = 1
        private const val SENT_BY_OTHER = 0
        private val MESSAGE_COMPARATOR = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.messageId == newItem.messageId
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }
        }
    }
}
