package com.darothub.musschatscreen.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.darothub.musschatscreen.util.flip

@Composable
fun MessageBubble(modifier: Modifier=Modifier, message: Message, isMe: Boolean = true) {
    Box(modifier = modifier){
        Surface(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 5.dp)
                .wrapContentSize()
                .align(Alignment.BottomEnd),
            shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp, topStart = 16.dp),
            color = if (isMe) Color.LightGray else Color.Black,
            contentColor = if (isMe) Color.Black else Color.White
        ) {
            Column {
                Text(
                    text = message.content,
                    modifier = Modifier.flip(!isMe)
                        .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = if (message.hasTailImage) 0.dp else 8.dp)
                        .align(Alignment.CenterHorizontally)
                )
                if (message.hasTailImage){
                    TailImage()
                }
            }
        }
    }
}