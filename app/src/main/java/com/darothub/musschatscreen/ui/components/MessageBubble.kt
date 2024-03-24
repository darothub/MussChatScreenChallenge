package com.darothub.musschatscreen.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.darothub.musschatscreen.model.Message

@Composable
fun MessageBubble(isMe: Boolean = true, message: Message) {
    Box(modifier = Modifier){
        Surface(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 5.dp)
                .wrapContentSize()
                .align(Alignment.BottomEnd),
            shape = if (isMe) RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp, topStart = 16.dp)
            else RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp, topEnd = 16.dp),
            color = if (isMe) Color.LightGray else Color.Black,
            contentColor = if (isMe) Color.Black else Color.White
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}