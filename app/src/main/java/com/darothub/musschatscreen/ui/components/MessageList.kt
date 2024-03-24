package com.darothub.musschatscreen.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.darothub.musschatscreen.model.Message
import com.darothub.musschatscreen.ui.main.currentUser

@Composable
fun ColumnScope.MessageList(messages: List<Message>) {
    LazyColumn(
        modifier = Modifier.weight(1f)
    ) {
        items(messages) { message ->
            val alignment = if (message.sender == currentUser) Arrangement.Start else Arrangement.End
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = alignment) {
                MessageBubble(message = message)
            }
        }
    }
}