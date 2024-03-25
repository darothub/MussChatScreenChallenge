package com.darothub.musschatscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import com.darothub.musschatscreen.application.di.ServiceLocator
import com.darothub.musschatscreen.model.Message
import com.darothub.musschatscreen.model.Sender
import com.darothub.musschatscreen.presentation.ui.screens.ChatScreen
import com.darothub.musschatscreen.presentation.ui.screens.Number
import com.darothub.musschatscreen.presentation.ui.screens.currentUser
import com.darothub.musschatscreen.presentation.viewmodel.ConversationViewModel
import com.darothub.musschatscreen.presentation.ui.theme.MussChatScreenTheme
import com.darothub.musschatscreen.presentation.viewmodel.ConversationViewModelFactory
import com.darothub.musschatscreen.util.sayss
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val conversationViewModel by viewModels<ConversationViewModel> {
        ConversationViewModelFactory(
            ServiceLocator.getInstance().messageRepository
        )
    }

    @OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MussChatScreenTheme {
                val scope = rememberCoroutineScope()
                ChatScreen(conversationViewModel) { content ->
                    when {
                        content.isNotEmpty() -> {
                            currentUser.sayss {
                                conversationViewModel.addMessage(Message(sender = it, content = content))
                            }
                            scope.launch {
                                delay(Number.TWO_SECONDS)
                                Sender.OTHER.sayss {
                                    conversationViewModel.addMessage(Message(sender = it, content = content))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}




