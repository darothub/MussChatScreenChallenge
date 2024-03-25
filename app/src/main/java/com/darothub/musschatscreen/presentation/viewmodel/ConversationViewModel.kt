package com.darothub.musschatscreen.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darothub.musschatscreen.data.entity.MessageEntity
import com.darothub.musschatscreen.data.repository.MessageRepository
import com.darothub.musschatscreen.model.Message
import com.darothub.musschatscreen.presentation.ui.screens.Conversation
import com.darothub.musschatscreen.presentation.ui.screens.Number
import com.darothub.musschatscreen.presentation.ui.screens.isTimeDifferenceGreaterThanOneHour
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant

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
    private fun updateTails(message: Message) {
        val lastMessage = messages.value.lastOrNull() ?: return
        lastMessage.hasTail = messages.value.size == 1 || // Most recent message
                messages.value.getOrNull(messages.value.lastIndex - 1)?.sender != message.sender

    }
    fun updateMessage(message: Message) = viewModelScope.launch {
        messageRepository.updateMessage(message.toMessageEntity())
    }
    fun hasTail(messageIndex: Long): Boolean {
        Log.d("Message", "$messages")
        if (messageIndex >= messages.value.size) {
            return false
        }
        val currentMessage = messages.value[messageIndex.toInt()]
        if (currentMessage.isTheMostRecent()) return true
        if (currentMessage.hasMessageSentAfterItByTheOther()) return true
        return currentMessage.hasMessageSentTwentySecondsAfterwards()
    }
    fun hasPreviousMessageSentMoreThanAnHourAgo(currentMessage: Message): Boolean {
        if (messages.value.isEmpty()){
            return false
        }
        val messageIndex = currentMessage.id!!.toInt()
        if (currentMessage.hasAPreviousMessage()){
            val previousMessage = messages.value[messageIndex - Number.ONE_INT]
            val previousMessageInstantTime = Instant.ofEpochMilli(previousMessage.timestamp)
            val currentMessageInstantTime = Instant.ofEpochMilli(currentMessage.timestamp)
            return isTimeDifferenceGreaterThanOneHour(previousMessageInstantTime, currentMessageInstantTime)
        }
        if (currentMessage.hasNoPreviousMessage()){
            return true
        }
        return false
    }
    fun calculateTimeDifferenceBetweenTwoMessages(index1: Long, index2: Long): Long {
        val message1 = messages.value[index1.toInt()]
        val message2 = messages.value[index2.toInt()]
        val instant1 = Instant.ofEpochMilli(message1.timestamp)
        val instant2 = Instant.ofEpochMilli(message2.timestamp)
        val duration = Duration.between(instant1, instant2).abs()
        return duration.toMillis()
    }
    private fun Message.isTheMostRecent() = messageBoundSafeSize == this.id
    private fun Message.hasMessageSentAfterIt(): Boolean = this.id!! < messageBoundSafeSize
    private fun Message.getMessageSentAfterByCurrentIndex() = messages.value[this.id!!.toInt() + Number.ONE_INT]
    private fun Message.hasMessageSentAfterItByTheOther(): Boolean {
        if (this.hasMessageSentAfterIt()) {
            return this.sender != getMessageSentAfterByCurrentIndex().sender
        }
        return false
    }
    private fun Message.hasAPreviousMessage(): Boolean = this.id!! > Number.ZERO_LONG
    private fun Message.hasNoPreviousMessage(): Boolean = this.id == Number.ZERO_LONG
    private fun Message.hasMessageSentTwentySecondsAfterwards(): Boolean {
        if (this.hasMessageSentAfterIt()) {
            return getMessageSentAfterByCurrentIndex()
                .isMoreThanTwentySecondsAgoAfterwards(this)
        }
        return false
    }
    private fun Message.isMoreThanTwentySecondsAgoAfterwards(currentMessage: Message): Boolean =
        this.timestamp - currentMessage.timestamp > Number.TWENTY_SECONDS
    private fun Message.toMessageEntity() = MessageEntity(
        sender = this.sender,
        content = this.content,
        timestamp = this.timestamp
    )
}