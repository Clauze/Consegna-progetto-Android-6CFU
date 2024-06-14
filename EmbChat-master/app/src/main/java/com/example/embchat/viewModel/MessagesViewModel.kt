package com.example.embchat.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.embchat.data.MessageRepository
import com.example.embchat.data.entity.Message
import kotlinx.coroutines.launch

class MessagesViewModel (private val repository: MessageRepository) : ViewModel(){

    /**
     * Lista dei messaggi di una chat come live data
     */
    val allMessage: LiveData<List<Message>> = repository.getMessageForChat(chatId).asLiveData()

    /**
     * Inserisce un nuovo messaggio
     */
    fun insert(message: Message) = viewModelScope.launch {
        repository.insertMessage(message)
    }

    /**
     * Elimina n messaggi dalla chat corrente
     */
    fun delete(numberMessages: Int) = viewModelScope.launch {
        repository.deleteMessages(numberMessages, chatId)
    }

    /**
     * Contiene l'id della chat corrispondente
     */
    companion object{

        private var chatId = -1

        fun setChatId(idChat: Int){
            chatId = idChat
        }

        fun resetChatId() {
            chatId = -1
        }

    }
}

class MessagesViewModelFactory(private val repository: MessageRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessagesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MessagesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }

}