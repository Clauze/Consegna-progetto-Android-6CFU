package com.example.embchat.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.embchat.data.entity.Chat
import com.example.embchat.data.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {

    /**
     * Live data contenente la lista dei numeri di telefono
     */
    val allChats: LiveData<List<Chat>> = repository.allChat.asLiveData()

    /**
     * Crea un nuova chat
     */
    fun insert(chat: Chat) = viewModelScope.launch {
        repository.insert(chat)
    }

    /**
     * Elimina una chat
     */
    fun delete(chat: Chat) = viewModelScope.launch {
        repository.delete(chat)
    }

    /**
     * Trova una chat
     */
    suspend fun findChat(phone: String) : Chat?{
        return repository.findChat(phone)
    }
}

class ChatViewModelFactory(private val repository: ChatRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }

}