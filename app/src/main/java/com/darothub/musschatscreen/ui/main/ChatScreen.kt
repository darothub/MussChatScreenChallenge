package com.darothub.musschatscreen.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.darothub.musschatscreen.data.entities.Message
import java.time.Duration
import java.time.Instant

val sampleListOfMessages = listOf(
    Message("Abdul", "Salam", Instant.now().toEpochMilli()),
    Message("Abdul", "hey", Instant.now().plus(Duration.ofMinutes(2)).toEpochMilli()), 
    Message("Aisha", "hey", Instant.now().plus(Duration.ofMinutes(2)).toEpochMilli()),
    Message("Aisha", "hey", Instant.now().plus(Duration.ofMinutes(2)).toEpochMilli()),
)

val sortedMessageListByTime = sampleListOfMessages.sortedBy {
    it.timestamp
}
val reversedSortedMessageList = sortedMessageListByTime.reversed()

@Composable
fun ChatScreen(messages: List<Message>) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize(), reverseLayout = true) {
            items(messages){message ->
                Text(message.content, color = Color.White)
            }
        }
    }
}

@Preview
@Composable
fun ChatScreenPreview() {
    ChatScreen(messages = reversedSortedMessageList)
}
