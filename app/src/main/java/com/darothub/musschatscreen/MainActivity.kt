package com.darothub.musschatscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.darothub.musschatscreen.application.di.ServiceLocator
import com.darothub.musschatscreen.model.Message
import com.darothub.musschatscreen.model.Sender
import com.darothub.musschatscreen.presentation.ui.screens.MainScreen
import com.darothub.musschatscreen.presentation.ui.screens.Number
import com.darothub.musschatscreen.presentation.ui.screens.currentUser
import com.darothub.musschatscreen.presentation.viewmodel.ConversationViewModel
import com.darothub.musschatscreen.presentation.viewmodel.ConversationViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val conversationViewModel by viewModels<ConversationViewModel> {
        ConversationViewModelFactory(
            ServiceLocator.getInstance().messageRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scope = rememberCoroutineScope()
            val collectedMessages = conversationViewModel.messages.collectAsStateWithLifecycle()
            MainScreen(messages = collectedMessages) { content ->
                when {
                    content.isNotEmpty() -> {
                        currentUser.says {
                            conversationViewModel.addMessage(Message(sender = it, content = content))
                        }
                        scope.launch {
                            delay(Number.TWO_SECONDS)
                            Sender.OTHER.says {
                                conversationViewModel.addMessage(Message(sender = it, content = content))
                            }
                        }
                    }
                }
            }
        }
    }
}




