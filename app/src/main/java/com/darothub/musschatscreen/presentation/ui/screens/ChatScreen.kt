package com.darothub.musschatscreen.presentation.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.ExperimentalComposeUiApi
import com.darothub.musschatscreen.model.Message
import com.darothub.musschatscreen.model.Sender
import com.darothub.musschatscreen.presentation.ui.components.ChatBox
import com.darothub.musschatscreen.presentation.ui.components.MessageList
import com.darothub.musschatscreen.presentation.ui.components.TopAppBarCard

var currentUser = Sender.ME

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun ChatScreen(messages: State<List<Message>>, onSend: (String) -> Unit) {
    val listState = rememberLazyListState()
    Scaffold(
        topBar = {
            TopAppBarCard()
        },
        bottomBar = {
            ChatBox (conversation = messages, onSend = onSend, listState = listState)
        }
    ) { paddingValues ->
        MessageList(conversation = messages, listState = listState, paddingValues = paddingValues)
    }
}


object Number {
    const val ONE = 1
    const val SIXTY_MIN = 60
    const val TWENTY_SECONDS = 20000L
    const val TWO_SECONDS = 2000L
}








