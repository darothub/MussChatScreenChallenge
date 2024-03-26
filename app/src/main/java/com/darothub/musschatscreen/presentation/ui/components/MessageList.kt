package com.darothub.musschatscreen.presentation.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darothub.musschatscreen.formatInstantToDayAndTime
import com.darothub.musschatscreen.model.Message
import com.darothub.musschatscreen.presentation.ui.screens.currentUser
import kotlinx.coroutines.launch
import java.time.Instant

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    conversation: State<List<Message>>,
    listState: LazyListState,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = paddingValues,
        state = listState,
    ) {
        items(conversation.value) {message ->
            val messages = conversation.value
            val isMe = message.sender == currentUser

            val alignment = if (isMe) Arrangement.End else Arrangement.Start

            val updatedMessage = if (message.hasTail(messages) && isMe) {
                message.copy(hasTail = message.hasTail(messages))
            } else {
                message
            }

            val shouldShowSectionHeader = shouldShowSectionHeader(message, messages)

            if (shouldShowSectionHeader){
                CreateSectionHeader(message)
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
                        MessageBubble(message = updatedMessage, isMe = isMe)
                    }
                }
            }
        }
    }
}

fun shouldShowSectionHeader(message: Message, messages: List<Message>): Boolean {
    return message.hasNoPreviousMessage(messages) or
            message.hasPreviousMessageSentMoreThanAnHourAgo(messages)
}
@Composable
fun CreateSectionHeader(message: Message) {
    val sectionHeader = formatInstantToDayAndTime(Instant.ofEpochMilli(message.timestamp))

    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = sectionHeader, fontSize = 16.sp)
    }
}