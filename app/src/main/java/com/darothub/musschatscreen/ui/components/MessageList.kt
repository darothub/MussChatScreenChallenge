package com.darothub.musschatscreen.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.darothub.musschatscreen.model.Message
import com.darothub.musschatscreen.ui.main.currentUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ColumnScope.MessageList(messages: List<Message>) {
    LazyColumn(
        modifier = Modifier.weight(1f)
    ) {
        items(messages) { message ->
            val isMe = message.sender == currentUser
            val alignment = if (isMe) Arrangement.Start else Arrangement.End
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
                        MessageBubble(isMe = isMe, message = message)
                    }
                }

            }
        }
    }
}