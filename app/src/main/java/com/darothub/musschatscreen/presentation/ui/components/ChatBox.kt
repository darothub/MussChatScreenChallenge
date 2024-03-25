package com.darothub.musschatscreen.presentation.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.darothub.musschatscreen.model.Message
import com.darothub.musschatscreen.presentation.ui.screens.Conversation
import com.darothub.musschatscreen.util.bringViewAboveKeyboard
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ChatBox(modifier: Modifier = Modifier, listState: LazyListState, conversation: State<List<Message>>, onSend: (String) -> Unit){
    var newMessageText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = modifier
            .bringViewAboveKeyboard()
            .padding(top = 120.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .shadow(elevation = 10.dp),
        contentColor = Color.Black
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            ChatTextField(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .padding(end = 15.dp),
                value = newMessageText,
                onValueChange = {
                    newMessageText = it
                }
            )
            SendIcon(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                onClick = {
                    onSend(newMessageText)
                    newMessageText = ""
                    coroutineScope.launch {
                        if (conversation.value.isNotEmpty()){
                            listState.animateScrollToItem(conversation.value.lastIndex)
                        }
                    }
                }
            )
        }
    }
}