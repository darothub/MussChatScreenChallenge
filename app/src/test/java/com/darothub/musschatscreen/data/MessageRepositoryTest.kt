package com.darothub.musschatscreen.data

import com.darothub.musschatscreen.data.entity.MessageEntity
import com.darothub.musschatscreen.data.repository.MessageRepository
import com.darothub.musschatscreen.model.Message
import com.darothub.musschatscreen.model.Sender
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import java.time.Instant

class MessageRepositoryTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @MockK
    private lateinit var messageRepository: MessageRepository

    @Test
    fun testFetchAllMessages_ReturnsEmptyFlow(): Unit = runBlocking {
        coEvery { messageRepository.fetchAllMessages() } returns flowOf(emptyList())

        messageRepository.fetchAllMessages().collect {
            assertEquals(emptyList<Message>(), it)
        }
    }
    @Test
    fun testFetchAllMessages_ReturnsListOfMessages(): Unit = runBlocking {
        val messageList = listOf(Message(1, Sender.ME,"Hello"), Message(2, Sender.ME, "World"))

        coEvery { messageRepository.fetchAllMessages() } returns flowOf(messageList)

        messageRepository.fetchAllMessages().collect {
            assertEquals(messageList, it)
        }

    }
    @Test
    fun testAddMessage(): Unit = runBlocking {
        val messageEntity = MessageEntity(1, Sender.OTHER,"Hello", Instant.now().toEpochMilli())

        coEvery { messageRepository.addMessage(messageEntity) } returns Unit

        messageRepository.addMessage(messageEntity)

        coVerify { messageRepository.addMessage(messageEntity) }
    }
}