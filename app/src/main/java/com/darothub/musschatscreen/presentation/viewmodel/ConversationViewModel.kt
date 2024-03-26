package com.darothub.musschatscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darothub.musschatscreen.data.repository.MessageRepository
import com.darothub.musschatscreen.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConversationViewModel(
    private val messageRepository: MessageRepository
): ViewModel() {
    private val _messages = MutableStateFlow(emptyList<Message>())
    val messages: StateFlow<List<Message>> = _messages
    var messageBoundSafeSize: Long = 0

    init {
        viewModelScope.launch {
            messageRepository.fetchAllMessages()
                .collect { messages ->
                    _messages.value = messages
                    messageBoundSafeSize = messages.size.toLong()
                }
        }
    }
    fun addMessage(message: Message) = viewModelScope.launch {
        messageRepository.addMessage(message.toMessageEntity())
    }

}