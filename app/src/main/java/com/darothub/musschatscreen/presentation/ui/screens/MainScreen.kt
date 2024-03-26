package com.darothub.musschatscreen.presentation.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.ExperimentalComposeUiApi
import com.darothub.musschatscreen.model.Message
import com.darothub.musschatscreen.presentation.ui.theme.MussChatScreenTheme

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(messages: State<List<Message>>, onSend: (String) -> Unit){
    MussChatScreenTheme {
        ChatScreen(messages, onSend = onSend)
    }
}