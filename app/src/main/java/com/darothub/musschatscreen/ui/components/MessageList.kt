package com.darothub.musschatscreen.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.darothub.musschatscreen.model.Message
import com.darothub.musschatscreen.ui.main.MessageDatabase
import com.darothub.musschatscreen.ui.main.arrayOfMessage
import com.darothub.musschatscreen.ui.main.currentUser
import com.darothub.musschatscreen.util.flip
import kotlinx.coroutines.launch
import java.time.Instant
import kotlin.time.Duration.Companion.seconds

@Composable
fun ColumnScope.MessageList(messages: List<Message>) {
    LazyColumn(
        modifier = Modifier.weight(1f)
    ) {
        itemsIndexed(messages) { index, message ->
            val isMe = message.sender == currentUser
            val alignment = if (isMe) Arrangement.Start else Arrangement.End
            val isTheMostRecentMessage = message.id == ( messages.size - 1).toLong()
            val hasNextMessageByTheOtherUser =
                if (!isTheMostRecentMessage) messages[index+1].sender != message.sender else false
            val hasNextMessageSentTwentySecondsAgo =
                if (!isTheMostRecentMessage) messages[index+1].timestamp.isTwentySecondsAgo() else false
            if (isTheMostRecentMessage || hasNextMessageByTheOtherUser || hasNextMessageSentTwentySecondsAgo) {
                val updatedMessage = message.copy(hasTailImage = true)
                val database = MessageDatabase()
                database.updateMessage(updatedMessage)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = alignment) {
                var animationState by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    launch {
                        animationState = true
                    }
                }
                AnimatedContent(
                    targetState = animationState,
                ) { content ->
                    if (content){
                        MessageBubble(modifier = Modifier.flip(!isMe), isMe = isMe, message = message)
                    }
                }
            }
        }
    }
}

fun Long.isTwentySecondsAgo(): Boolean {
    val threshold = Instant.now().minusSeconds(20)
    return Instant.ofEpochMilli(this) >= threshold
}