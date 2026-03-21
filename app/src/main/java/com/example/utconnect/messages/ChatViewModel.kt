package com.example.utconnect.messages

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

data class Message(val text: String, val isFromMe: Boolean, val time: String)

class ChatViewModel : ViewModel() {
    private val _messagesByContact = mutableStateMapOf<Int, List<Message>>()

    fun getMessagesForContact(contactId: Int): List<Message> {
        return _messagesByContact[contactId] ?: emptyList()
    }

    fun sendMessage(contactId: Int, text: String) {
        val trimmedText = text.trim()
        if (trimmedText.isBlank()) return
        
        val currentMessages = _messagesByContact[contactId] ?: emptyList()
        val newMessage = Message(trimmedText, true, "12:00 PM")
        _messagesByContact[contactId] = currentMessages + newMessage
    }
}
