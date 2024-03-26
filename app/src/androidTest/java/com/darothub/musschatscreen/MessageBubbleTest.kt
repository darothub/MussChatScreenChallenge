package com.darothub.musschatscreen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.darothub.musschatscreen.model.Message
import com.darothub.musschatscreen.model.Sender
import com.darothub.musschatscreen.presentation.ui.components.MessageBubble

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class MessageBubbleTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun messageBubble_displaysCorrectly_forOwnMessage() {
        val content = "Hello"
        val message = Message(1,Sender.ME,content, hasTail = true)
        composeTestRule.setContent {
            MessageBubble(message = message)
        }

        val bubble = composeTestRule.onNodeWithText(content)

        bubble.assertIsDisplayed()

        composeTestRule.onNodeWithTag("TailImage").assertIsDisplayed()
    }
    @Test
    fun messageBubble_displaysCorrectly_forOtherMessage() {
        val content = "Hey"
        val message = Message(1,Sender.OTHER, content)
        composeTestRule.setContent {
            MessageBubble(message = message)
        }

        val bubble = composeTestRule.onNodeWithText(content)

        bubble.assertIsDisplayed()

        composeTestRule.onNodeWithTag("TailImage").assertDoesNotExist()
    }
}
