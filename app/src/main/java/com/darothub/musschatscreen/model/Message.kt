package com.darothub.musschatscreen.model

import android.util.Log
import com.darothub.musschatscreen.data.entity.MessageEntity
import com.darothub.musschatscreen.presentation.ui.screens.Number
import com.darothub.musschatscreen.presentation.ui.screens.isTimeDifferenceGreaterThanOneHour
import java.time.Duration
import java.time.Instant

data class Message (
    val id: Long?= null,
    val sender: Sender,
    val content: String,
    val timestamp: Long = Instant.now().toEpochMilli(),
    var hasTail: Boolean = false
){

    fun hasTail(messages: List<Message>): Boolean {
        Log.d("Message", "$messages")
        if (this.id == null){
            return false
        }
        val messageIndex = messages.indexOf(this)
        if (messageIndex >= messages.size) {
            return false
        }
        if (isTheMostRecent(messages)) return true
        if (hasMessageSentAfterItByTheOther(messages)) return true
        return hasMessageSentTwentySecondsAfterwards(messages)
    }
    fun hasPreviousMessageSentMoreThanAnHourAgo(messages: List<Message>): Boolean {
        if (messages.isEmpty()){
            return false
        }
        if (hasAPreviousMessage(messages)){
            val previousMessage = messages.getOrNull(messages.indexOf(this) - 1)
            val previousMessageInstantTime = Instant.ofEpochMilli(previousMessage!!.timestamp)
            val thisInstantTime = Instant.ofEpochMilli(this.timestamp)
            return isTimeDifferenceGreaterThanOneHour(previousMessageInstantTime, thisInstantTime)
        }
        return false
    }

    private fun isTheMostRecent(messages: List<Message>) = messages.lastIndex == messages.indexOf(this)
    private fun hasMessageSentAfterItByTheOther(messages: List<Message>): Boolean {
        if (hasMessageSentAfterIt(messages)) {
            return this.sender != getMessageSentAfterByCurrentIndex(messages)?.sender
        }
        return false
    }
    private fun hasMessageSentAfterIt(messages: List<Message>): Boolean =
        messages.getOrNull(messages.indexOf(this) + 1) != null
    private fun getMessageSentAfterByCurrentIndex(messages: List<Message>) =
        messages.getOrNull(messages.indexOf(this) + 1)

    private fun hasAPreviousMessage(messages: List<Message>): Boolean =
        messages.getOrNull(messages.indexOf(this) - 1) != null
    fun hasNoPreviousMessage(messages: List<Message>): Boolean =
        messages.getOrNull(messages.indexOf(this) - 1) == null
    private fun hasMessageSentTwentySecondsAfterwards(messages: List<Message>): Boolean {
        if (this.hasMessageSentAfterIt(messages)) {
            return getMessageSentAfterByCurrentIndex(messages)!!.isMoreThanTwentySecondsAgoAfterwards(this)
        }
        return false
    }
    private fun isMoreThanTwentySecondsAgoAfterwards(currentMessage: Message): Boolean =
        this.timestamp - currentMessage.timestamp > Number.TWENTY_SECONDS
    private fun Message.toMessageEntity() = MessageEntity(
        sender = this.sender,
        content = this.content,
        timestamp = this.timestamp
    )
}

fun calculateTimeDifferenceBetweenTwoMessages(index1: Int, index2: Int?, messages: List<Message>): Long {
    val message1 = messages[index1]
    if (index2 == null || index2 < 0){
        return message1.timestamp
    }
    val message2 = messages.get(index2.toInt())
    val instant1 = Instant.ofEpochMilli(message1.timestamp)
    val instant2 = Instant.ofEpochMilli(message2.timestamp)
    val duration = Duration.between(instant1, instant2).abs()
    return duration.toMillis()
}