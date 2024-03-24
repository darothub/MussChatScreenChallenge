package com.darothub.musschatscreen.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.darothub.musschatscreen.model.Message
import com.darothub.musschatscreen.model.Sender
import com.darothub.musschatscreen.ui.components.ChatTextField
import com.darothub.musschatscreen.ui.components.MessageList
import com.darothub.musschatscreen.ui.components.SendIcon
import com.darothub.musschatscreen.ui.theme.MussChatScreenTheme
import com.darothub.musschatscreen.util.bringViewAboveKeyboard
import com.darothub.musschatscreen.util.say
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

var currentUser = Sender.ME
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun ChatScreen(messages: List<Message>, newContent: MutableState<String>, onSend: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        MessageList(messages)
        Surface(
            modifier = Modifier
                .bringViewAboveKeyboard()
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
                        .height(40.dp)
                        .padding(end = 15.dp),
                    value = newContent.value,
                    onValueChange = {
                        newContent.value = it
                    }
                )
                SendIcon(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    onClick = {
                        onSend()
                        newContent.value = ""
                    }
                )
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    val messages = remember {
        mutableStateListOf<Message>()
    }
    val text = remember { mutableStateOf("") }
    MussChatScreenTheme {
        ChatScreen(messages, text) {
            if (currentUser.say { messages.add(Message(it, text.value)) }) {
                Sender.OTHER.say { messages.add(Message(it, text.value))  }
            }
        }
    }
}

