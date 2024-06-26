package com.darothub.musschatscreen.model

import com.darothub.musschatscreen.Number.ONE
import com.darothub.musschatscreen.Number.TWENTY_SECONDS
import com.darothub.musschatscreen.data.entity.MessageEntity
import com.darothub.musschatscreen.utils.TimeUtils
import java.time.Instant

data class Message (
    val id: Long?= null,
    val sender: Sender,
    val content: String,
    val timestamp: Long = Instant.now().toEpochMilli(),
    var hasTail: Boolean = false
){
    fun toMessageEntity() = MessageEntity(
        sender = this.sender,
        content = this.content,
        timestamp = this.timestamp
    )
    fun hasTail(messages: List<Message>): Boolean {
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
            val previousMessage = messages[messages.indexOf(this) - ONE]
            val previousMessageInstantTime = Instant.ofEpochMilli(previousMessage.timestamp)
            val thisInstantTime = Instant.ofEpochMilli(this.timestamp)
            return TimeUtils.isTimeDifferenceGreaterThanOneHour(previousMessageInstantTime, thisInstantTime)
        }
        return false
    }
    fun hasNoPreviousMessage(messages: List<Message>): Boolean = !hasAPreviousMessage(messages)

    private fun isTheMostRecent(messages: List<Message>) =
        messages.lastIndex == messages.indexOf(this)
    private fun hasMessageSentAfterItByTheOther(messages: List<Message>): Boolean {
        if (hasMessageSentAfterIt(messages)) {
            return this.sender != getMessageSentAfterByCurrentIndex(messages)?.sender
        }
        return false
    }
    private fun hasMessageSentAfterIt(messages: List<Message>): Boolean =
        messages.indexOf(this) < messages.lastIndex
    private fun getMessageSentAfterByCurrentIndex(messages: List<Message>) =
        messages.getOrNull(messages.indexOf(this) + ONE)

    private fun hasAPreviousMessage(messages: List<Message>): Boolean =
        messages.getOrNull(messages.indexOf(this) - ONE) != null
    private fun hasMessageSentTwentySecondsAfterwards(messages: List<Message>): Boolean {
        if (this.hasMessageSentAfterIt(messages)) {
            return getMessageSentAfterByCurrentIndex(messages)?.
            isMoreThanTwentySecondsAgoAfterwards(this) == true
        }
        return false
    }
    private fun isMoreThanTwentySecondsAgoAfterwards(currentMessage: Message): Boolean =
        this.timestamp - currentMessage.timestamp > TWENTY_SECONDS

}

