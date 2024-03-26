package com.darothub.musschatscreen.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.darothub.musschatscreen.model.Message
@Composable
fun MessageBubble(
    modifier: Modifier = Modifier,
    message: Message,
    isMe: Boolean = true,
    tailImage: @Composable (() -> Unit)? = null
) {
    val bubbleColor = if (isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val contentColor = if (isMe) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
    val shouldShowTail = message.hasTail && isMe
    Box(modifier = modifier) {
        Surface(
            modifier = modifier.padding(horizontal = 15.dp, vertical = 5.dp).wrapContentSize(),
            shape = roundedBubbleShape(isMe),
            color = bubbleColor,
            contentColor = contentColor
        ) {
            Column(
                modifier = modifier.align(Alignment.BottomStart),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = message.content,
                    modifier = modifier.padding(
                        top = 8.dp,
                        start = 8.dp,
                        end = 8.dp,
                        bottom = if (shouldShowTail) 0.dp else 8.dp
                    )
                )
                if (shouldShowTail) {
                    tailImage?.invoke() ?: TailImage()
                }
            }
        }
    }
}


private fun roundedBubbleShape(isMe: Boolean) = RoundedCornerShape(
    topStart = 16.dp,
    topEnd = 16.dp,
    bottomStart = if (isMe) 16.dp else 0.dp,
    bottomEnd = if (isMe) 0.dp else 16.dp
)