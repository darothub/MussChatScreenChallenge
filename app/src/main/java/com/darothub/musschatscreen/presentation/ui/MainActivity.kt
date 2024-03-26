package com.darothub.musschatscreen.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.darothub.musschatscreen.Number
import com.darothub.musschatscreen.application.di.ServiceLocator
import com.darothub.musschatscreen.model.Message
import com.darothub.musschatscreen.model.Sender
import com.darothub.musschatscreen.presentation.ui.screens.MainScreen
import com.darothub.musschatscreen.presentation.ui.screens.currentUser
import com.darothub.musschatscreen.presentation.viewmodel.ConversationViewModel
import com.darothub.musschatscreen.presentation.viewmodel.ConversationViewModelFactory
import com.darothub.musschatscreen.says
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
val listOfLetters = listOf("a", "b", "w", "c", "o", "e", "i", "u")
val sentence = listOf(
    "Try another word with the letter",
    "hmm you must good at using the letter",
    "This is not how to use the letter",
    "I just learnt a new word with the letter"
).random()

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
            var counter = 0
            MainScreen(messages = collectedMessages) { content ->
                when {
                    content.isNotEmpty() -> {
                        currentUser.says {
                            conversationViewModel.addMessage(Message(sender = it, content = content))
                        }
                        val letters = listOfLetters.filter { content.contains(it) }
                        if (letters.isEmpty()){
                            return@MainScreen
                        }
                        val letter = letters[0]
                        val otherResponse = "$sentence $letter"

                        scope.launch {
                            delay(Number.TWO_SECONDS)
                            Sender.OTHER.says {
                                counter++
                                if (counter<3){
                                    conversationViewModel.addMessage(Message(sender = it, content = otherResponse))
                                }
                                if (counter in 3..5){
                                    val message = "I'm waiting for a letter I do not know"
                                    conversationViewModel.addMessage(Message(sender = it, content = message))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}




