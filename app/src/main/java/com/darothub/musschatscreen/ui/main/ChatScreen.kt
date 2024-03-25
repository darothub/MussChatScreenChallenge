package com.darothub.musschatscreen.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import com.darothub.musschatscreen.model.Message
import com.darothub.musschatscreen.model.Sender
import com.darothub.musschatscreen.ui.components.ChatBox
import com.darothub.musschatscreen.ui.components.MessageList
import com.darothub.musschatscreen.ui.theme.MussChatScreenTheme
import com.darothub.musschatscreen.util.says
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant

var currentUser = Sender.ME

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun ChatScreen(conversation: Conversation, onSend: (String) -> Unit) {
    var newMessageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

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
                    delay(2000)
                    Sender.OTHER.says { conversation.addMessage(Message(sender = it, content = content)) }
                }
            }
        }
    }
}


class Conversation {
    var messages = mutableStateListOf<Message>()
    val messagesSize: Int
        get() = messages.size - 1
    fun addMessage(newMessage: Message) = messages.add(newMessage)

    fun hasTail(messageIndex: Int): Boolean {
        if (messageIndex >= messages.size) {
            return false
        }
        val currentMessage = messages[messageIndex]
        if (currentMessage.isTheMostRecent()) return true
        if (currentMessage.hasMessageSentAfterItByTheOther()) return true
        return currentMessage.hasMessageSentTwentySecondsAfterwards()
    }
    private fun Message.isTheMostRecent() = messagesSize == this.id.toInt()
    private fun Message.hasMessageSentAfterIt(): Boolean = this.id.toInt() < messagesSize
    private fun Message.getMessageSentAfterByCurrentIndex() = messages[this.id.toInt() + 1]
    private fun Message.hasMessageSentAfterItByTheOther(): Boolean {
        if (this.hasMessageSentAfterIt()) {
            return this.sender != getMessageSentAfterByCurrentIndex().sender
        }
        return false
    }
    private fun Message.hasMessageSentTwentySecondsAfterwards(): Boolean {
        if (this.hasMessageSentAfterIt()) {
            return getMessageSentAfterByCurrentIndex()
                .isMoreThanTwentySecondsAgoAfterwards(this)
        }
        return false
    }
    private fun Message.isMoreThanTwentySecondsAgoAfterwards(currentMessage: Message): Boolean =
        this.timestamp - currentMessage.timestamp > 20000L

    suspend fun simulateOnGoingChat() {
        for (i in 0..5){
            addMessage(Message(sender = Sender.ME, content = "Hey"))
            addMessage(Message(sender = Sender.ME, content = "How are you?"))
        }
        delay(10000)
        addMessage(
            Message(
                sender = Sender.OTHER,
                content = "Hey",
                timestamp = Instant.now().toEpochMilli()
            )
        )
    }
}

