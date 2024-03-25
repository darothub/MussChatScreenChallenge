package com.darothub.musschatscreen.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import com.darothub.musschatscreen.model.Message
import com.darothub.musschatscreen.model.Sender
import com.darothub.musschatscreen.ui.components.ChatBox
import com.darothub.musschatscreen.ui.components.MessageList
import com.darothub.musschatscreen.ui.main.Number.ONE_INT
import com.darothub.musschatscreen.ui.main.Number.ONE_LONG
import com.darothub.musschatscreen.ui.main.Number.TEN_SECONDS
import com.darothub.musschatscreen.ui.main.Number.TWENTY_SECONDS
import com.darothub.musschatscreen.ui.main.Number.TWO_SECONDS
import com.darothub.musschatscreen.ui.main.Number.ZERO_LONG
import com.darothub.musschatscreen.ui.theme.MussChatScreenTheme
import com.darothub.musschatscreen.util.says
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

var currentUser = Sender.ME

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun ChatScreen(conversation: Conversation, onSend: (String) -> Unit) {
    val listState = rememberLazyListState()
    Scaffold(
        bottomBar = {
            ChatBox (conversation = conversation, onSend = onSend, listState = listState)
        }
    ) { paddingValues ->
        MessageList(conversation = conversation, listState = listState, paddingValues = paddingValues)
    }
}

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    val scope = rememberCoroutineScope()
    val conversation = Conversation()

    LaunchedEffect(key1 = Unit) {
        scope.launch {
            conversation.simulateOnGoingChat()
        }
    }

    MussChatScreenTheme {
        ChatScreen(conversation) { content ->
            if (currentUser.says { conversation.addMessage(Message(sender = it, content = content)) }) {
                scope.launch {
                    delay(TWO_SECONDS)
                    Sender.OTHER.says { conversation.addMessage(Message(sender = it, content = content)) }
                }
            }
        }
    }
}


class Conversation {
    var messages = mutableStateListOf<Message>()
    val messageBoundSafeSize: Long
        get() = messages.size.toLong() - ONE_LONG

    fun addMessage(newMessage: Message) = messages.add(newMessage)

    fun hasTail(messageIndex: Long): Boolean {
        if (messageIndex >= messages.size) {
            return false
        }
        val currentMessage = messages[messageIndex.toInt()]
        if (currentMessage.isTheMostRecent()) return true
        if (currentMessage.hasMessageSentAfterItByTheOther()) return true
        return currentMessage.hasMessageSentTwentySecondsAfterwards()
    }
    fun hasPreviousMessageSentMoreThanAnHourAgo(messageIndex: Long): Boolean {
        val currentMessage = messages[messageIndex.toInt()]
        if (currentMessage.hasAPreviousMessage()){
            val previousMessage = messages[messageIndex.toInt()- ONE_INT]
            val previousMessageInstantTime = Instant.ofEpochMilli(previousMessage.timestamp)
            val currentMessageInstantTime = Instant.ofEpochMilli(currentMessage.timestamp)
            return isTimeDifferenceGreaterThanOneHour(previousMessageInstantTime, currentMessageInstantTime)
        }
        if (currentMessage.hasNoPreviousMessage()){
            return true
        }
        return false
    }
    fun calculateTimeDifferenceBetweenTwoMessages(index1: Long, index2: Long): Long{
        val message1 = messages[index1.toInt()]
        val message2 = messages[index2.toInt()]
        val instant1 = Instant.ofEpochMilli(message1.timestamp)
        val instant2 = Instant.ofEpochMilli(message2.timestamp)
        val duration = Duration.between(instant1, instant2).abs()
        return duration.toMillis()
    }
    private fun Message.isTheMostRecent() = messageBoundSafeSize == this.id
    private fun Message.hasMessageSentAfterIt(): Boolean = this.id < messageBoundSafeSize
    private fun Message.getMessageSentAfterByCurrentIndex() = messages[this.id.toInt() + ONE_INT]
    private fun Message.hasMessageSentAfterItByTheOther(): Boolean {
        if (this.hasMessageSentAfterIt()) {
            return this.sender != getMessageSentAfterByCurrentIndex().sender
        }
        return false
    }
    private fun Message.hasAPreviousMessage(): Boolean = this.id > ZERO_LONG
    private fun Message.hasNoPreviousMessage(): Boolean = this.id == ZERO_LONG
    private fun Message.hasMessageSentTwentySecondsAfterwards(): Boolean {
        if (this.hasMessageSentAfterIt()) {
            return getMessageSentAfterByCurrentIndex()
                .isMoreThanTwentySecondsAgoAfterwards(this)
        }
        return false
    }
    private fun Message.isMoreThanTwentySecondsAgoAfterwards(currentMessage: Message): Boolean =
        this.timestamp - currentMessage.timestamp > TWENTY_SECONDS

    suspend fun simulateOnGoingChat() {
        for (i in 0..5){
            addMessage(Message(sender = Sender.ME, content = "Hey"))
            addMessage(Message(sender = Sender.ME, content = "How are you?"))
        }
        delay(TEN_SECONDS)
        addMessage(
            Message(
                sender = Sender.OTHER,
                content = "Hey"
            )
        )
    }
}

fun isTimeDifferenceGreaterThanOneHour(instant1: Instant, instant2: Instant): Boolean {
    val duration = Duration.between(instant1, instant2).abs()
    return duration.toMinutes() > Number.SIXTY_MIN
}

fun formatInstantToDayAndTime(instant: Instant): String {
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
    val formatter = DateTimeFormatter.ofPattern("EE HH:mm")
    return localDateTime.format(formatter)
}
object Number {
    const val ZERO_LONG = 0L
    const val ONE_LONG = 1L
    const val ONE_INT = 1
    const val SIXTY_MIN = 60
    const val TWENTY_SECONDS = 20000L
    const val TWO_SECONDS = 2000L
    const val TEN_SECONDS = 10000L
}