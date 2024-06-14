package com.example.embchat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.embchat.ChatDetail
import com.example.embchat.R
import com.example.embchat.data.entity.Chat
import com.example.embchat.adapter.ChatListAdapter.ChatViewHolder
import com.example.embchat.fragment.ChatListFragmentDirections

class ChatListAdapter (private val onDeleteClick: (Chat) -> Unit) : ListAdapter<Chat,ChatViewHolder>(CHAT_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder.create(parent, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class ChatViewHolder(itemView: View, private val onDelete: (Chat) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val chatItemView: TextView = itemView.findViewById(R.id.phone)
        private val chatDeleteButton: ImageView = itemView.findViewById(R.id.delChat)

        fun bind(chat: Chat) {
            chatItemView.text = chat.phoneNumber

            itemView.setOnClickListener{view ->
                val arg = ChatDetail(chat.chatId, chat.phoneNumber)
                val action = ChatListFragmentDirections.actionChatListFragmentToMessagesListFragment(arg)
                Navigation.findNavController(view).navigate(action)
            }

            chatDeleteButton.setOnClickListener{
                onDelete(chat)
            }
        }

        companion object {
            fun create(parent: ViewGroup, onDeleteClick: ((Chat) -> Unit)): ChatViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return ChatViewHolder(view, onDeleteClick)
            }
        }
    }



    companion object {
        private val CHAT_COMPARATOR = object : DiffUtil.ItemCallback<Chat>() {
            override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem.chatId == newItem.chatId
            }

            override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem.phoneNumber == newItem.phoneNumber
            }
        }
    }
}
