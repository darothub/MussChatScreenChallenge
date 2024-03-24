package com.darothub.musschatscreen.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.darothub.musschatscreen.data.entities.Message

@Composable
fun ColumnScope.MessageList(messages: List<Message>) {
    LazyColumn(
        modifier = Modifier.weight(1f)
    ) {
        items(messages) { message ->
            Text(message.content, color = Color.Black)
        }
    }
}