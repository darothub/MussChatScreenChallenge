package com.darothub.musschatscreen.application.di

import android.content.Context
import androidx.room.Room
import com.darothub.musschatscreen.data.database.MessageDatabase
import com.darothub.musschatscreen.data.repository.MessageRepositoryImpl
import com.darothub.musschatscreen.presentation.viewmodel.ConversationViewModel

class ServiceLocator private constructor(applicationContext: Context) {


    private val messageDb: MessageDatabase by lazy {
        val instance = synchronized(this) {
            Room.databaseBuilder(
                context = applicationContext,
                klass = MessageDatabase::class.java,
                name = DATABASE_NAME
            ).build()
        }
        instance
    }
    val messageRepository = MessageRepositoryImpl(messageDb.dao)

    fun provideConversationViewModel() = ConversationViewModel(messageRepository)

    companion object {
        private const val DATABASE_NAME = "conversation.db"
        private var instance: ServiceLocator? = null

        fun initialize(applicationContext: Context) {
            if (instance == null) {
                synchronized(ServiceLocator::class.java) {
                    if (instance == null) {
                        instance = ServiceLocator(applicationContext)
                    }
                }
            }
        }

        fun getInstance(): ServiceLocator {
            if (instance == null) {
                throw IllegalStateException("ServiceLocator must be initialized first.")
            }
            return instance!!
        }
    }

}