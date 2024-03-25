package com.darothub.musschatscreen.presentation.ui.components

import android.util.Log
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
import com.darothub.musschatscreen.model.Message
import com.darothub.musschatscreen.model.calculateTimeDifferenceBetweenTwoMessages
import com.darothub.musschatscreen.presentation.ui.screens.Conversation
import com.darothub.musschatscreen.presentation.ui.screens.Number
import com.darothub.musschatscreen.presentation.ui.screens.currentUser
import com.darothub.musschatscreen.presentation.ui.screens.formatInstantToDayAndTime
import com.darothub.musschatscreen.util.flip
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
            val alignment = if (isMe) Arrangement.Start else Arrangement.End
            val hasTail = message.hasTail(messages)

            Log.d("HasTail", "$hasTail")
            var updatedMessage: Message = message
            if (hasTail && isMe){
                updatedMessage = message.copy(hasTail = hasTail)
            }
            val hasNoPreviousMessage =
                message.hasNoPreviousMessage(messages)
            val hasPreviousMessageSentMoreThanAnHourAgo =
                message.hasPreviousMessageSentMoreThanAnHourAgo(messages)
            val showSection = hasNoPreviousMessage || hasPreviousMessageSentMoreThanAnHourAgo

            if (showSection){
                val currentMessageIndex = messages.indexOf(message)
                val supposedPreviousMessageIndex = currentMessageIndex - 1
                val timeDiff = calculateTimeDifferenceBetweenTwoMessages(
                    messages.indexOf(message), supposedPreviousMessageIndex,
                    messages
                )
                val sectionHeader:String =
                    formatInstantToDayAndTime(Instant.ofEpochMilli(timeDiff))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = sectionHeader, fontSize = 16.sp)
                }
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
                        MessageBubble(modifier = Modifier.flip(!isMe), isMe = isMe, message = updatedMessage)
                    }
                }
            }
        }
    }
}

