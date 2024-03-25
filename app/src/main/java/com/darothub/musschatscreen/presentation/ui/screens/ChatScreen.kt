package com.darothub.musschatscreen.presentation.ui.screens

import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import com.darothub.musschatscreen.application.di.ServiceLocator
import com.darothub.musschatscreen.model.Message
import com.darothub.musschatscreen.model.Sender
import com.darothub.musschatscreen.presentation.viewmodel.ConversationViewModel
import com.darothub.musschatscreen.presentation.ui.components.ChatBox
import com.darothub.musschatscreen.presentation.ui.components.MessageList
import com.darothub.musschatscreen.presentation.ui.screens.Number.ONE_INT
import com.darothub.musschatscreen.presentation.ui.screens.Number.ONE_LONG
import com.darothub.musschatscreen.presentation.ui.screens.Number.TEN_SECONDS
import com.darothub.musschatscreen.presentation.ui.screens.Number.TWENTY_SECONDS
import com.darothub.musschatscreen.presentation.ui.screens.Number.TWO_SECONDS
import com.darothub.musschatscreen.presentation.ui.screens.Number.ZERO_LONG
import com.darothub.musschatscreen.presentation.ui.theme.MussChatScreenTheme
import com.darothub.musschatscreen.presentation.viewmodel.ConversationViewModelFactory
import com.darothub.musschatscreen.util.says
import com.darothub.musschatscreen.util.sayss
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

var currentUser = Sender.ME

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun ChatScreen(viewModel: ConversationViewModel, onSend: (String) -> Unit) {
    val listState = rememberLazyListState()
    val messages: StateFlow<List<Message>> = viewModel.messages
    val collectedMessages = messages.collectAsState()
    Scaffold(
        bottomBar = {
            ChatBox (conversation = collectedMessages, onSend = onSend, listState = listState)
        }
    ) { paddingValues ->
        MessageList(conversation = collectedMessages, listState = listState, paddingValues = paddingValues)
    }
}

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    val scope = rememberCoroutineScope()
    val conversationViewModel = ServiceLocator.getInstance().provideConversationViewModel()
//    LaunchedEffect(key1 = Unit) {
//        scope.launch {
//            conversation.simulateOnGoingChat()
//        }
//    }
    MussChatScreenTheme {
        ChatScreen(conversationViewModel) { content ->
            when {
                content.isNotEmpty() -> {
                    scope.launch {
                        currentUser.sayss {
                            conversationViewModel.addMessage(Message(sender = it, content = content))
                        }
//                        delay(TWENTY_SECONDS)
//                        Sender.OTHER.sayss {
//                            conversationViewModel.addMessage(Message(sender = it, content = content))
//                        }
                    }
                }
            }
        }
    }
}


class Conversation(
    var messages:List<Message>  = mutableStateListOf()
) {

    val messageBoundSafeSize: Long
        get() = messages.size.toLong() - ONE_LONG

    fun hasTail(messageIndex: Long): Boolean {
        Log.d("Message", "$messages")
        if (messageIndex >= messages.size) {
            return false
        }
        val currentMessage = messages[messageIndex.toInt()]
        if (currentMessage.isTheMostRecent()) return true
        if (currentMessage.hasMessageSentAfterItByTheOther()) return true
        return currentMessage.hasMessageSentTwentySecondsAfterwards()
    }
    fun hasPreviousMessageSentMoreThanAnHourAgo(currentMessage: Message): Boolean {
        if (messages.isEmpty()){
            return false
        }
        val messageIndex = currentMessage.id!!.toInt()
        if (currentMessage.hasAPreviousMessage()){
            val previousMessage = messages[messageIndex - ONE_INT]
            val previousMessageInstantTime = Instant.ofEpochMilli(previousMessage.timestamp)
            val currentMessageInstantTime = Instant.ofEpochMilli(currentMessage.timestamp)
            return isTimeDifferenceGreaterThanOneHour(previousMessageInstantTime, currentMessageInstantTime)
        }
        if (currentMessage.hasNoPreviousMessage()){
            return true
        }
        return false
    }
    fun calculateTimeDifferenceBetweenTwoMessages(index1: Long, index2: Long): Long {
        val message1 = messages[index1.toInt()]
        val message2 = messages[index2.toInt()]
        val instant1 = Instant.ofEpochMilli(message1.timestamp)
        val instant2 = Instant.ofEpochMilli(message2.timestamp)
        val duration = Duration.between(instant1, instant2).abs()
        return duration.toMillis()
    }
    private fun Message.isTheMostRecent() = messageBoundSafeSize == this.id
    private fun Message.hasMessageSentAfterIt(): Boolean = this.id!! < messageBoundSafeSize
    private fun Message.getMessageSentAfterByCurrentIndex() = messages[this.id!!.toInt() + ONE_INT]
    private fun Message.hasMessageSentAfterItByTheOther(): Boolean {
        if (this.hasMessageSentAfterIt()) {
            return this.sender != getMessageSentAfterByCurrentIndex().sender
        }
        return false
    }
    private fun Message.hasAPreviousMessage(): Boolean = this.id!! > ZERO_LONG
    private fun Message.hasNoPreviousMessage(): Boolean = this.id == ZERO_LONG
    private fun Message.hasMessageSentTwentySecondsAfterwards(): Boolean {
        if (this.hasMessageSentAfterIt()) {
            return getMessageSentAfterByCurrentIndex()
                .isMoreThanTwentySecondsAgoAfterwards(this)
        }
        return false
    }
    private fun Message.isMoreThanTwentySecondsAgoAfterwards(currentMessage: Message): Boolean =
        this.timestamp - currentMessage.timestamp > TWENTY_SECONDS

//    suspend fun simulateOnGoingChat() {
//        for (i in 0..5){
//            addMessage(Message(sender = Sender.ME, content = "Hey"))
//            addMessage(Message(sender = Sender.ME, content = "How are you?"))
//        }
//        delay(TEN_SECONDS)
//        addMessage(
//            Message(
//                sender = Sender.OTHER,
//                content = "Hey"
//            )
//        )
//    }
}

fun isTimeDifferenceGreaterThanOneHour(instant1: Instant, instant2: Instant): Boolean {
    val duration = Duration.between(instant1, instant2).abs()
    return duration.toMinutes() > Number.SIXTY_MIN
}

fun formatInstantToDayAndTime(instant: Instant): String {
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
    val formatter = DateTimeFormatter.ofPattern("EEEE HH:mm")
    return localDateTime.format(formatter)
}
object Number {
    const val ZERO_LONG = 1L
    const val ONE_LONG = 1L
    const val ONE_INT = 1
    const val SIXTY_MIN = 60
    const val TWENTY_SECONDS = 20000L
    const val TWO_SECONDS = 2000L
    const val TEN_SECONDS = 10000L
}