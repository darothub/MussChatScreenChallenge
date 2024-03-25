package com.darothub.musschatscreen.ui.components

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darothub.musschatscreen.ui.main.Conversation
import com.darothub.musschatscreen.ui.main.Number
import com.darothub.musschatscreen.ui.main.currentUser
import com.darothub.musschatscreen.ui.main.formatInstantToDayAndTime
import com.darothub.musschatscreen.util.flip
import kotlinx.coroutines.launch
import java.time.Instant

@Composable
fun MessageList(modifier: Modifier = Modifier, conversation: Conversation, listState: LazyListState, paddingValues: PaddingValues) {
    LazyColumn(
        modifier = modifier,
        contentPadding = paddingValues,
        state = listState,
    ) {
        items(conversation.messages) {message ->
            val isMe = message.sender == currentUser
            val hasTail = conversation.hasTail(message.id)
            val updatedMessage = message.copy(hasTail = hasTail)
            val showSection = conversation.hasPreviousMessageSentMoreThanAnHourAgo(message.id)
            val alignment = if (isMe) Arrangement.Start else Arrangement.End
            if (showSection){
                val sectionHeader:String = if (message.id == Number.ZERO_LONG){
                    formatInstantToDayAndTime(Instant.ofEpochMilli(message.timestamp))
                } else {
                    val timeDiff = conversation.calculateTimeDifferenceBetweenTwoMessages(message.id, message.id-Number.ONE_LONG)
                    formatInstantToDayAndTime(Instant.ofEpochMilli(timeDiff))
                }
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

