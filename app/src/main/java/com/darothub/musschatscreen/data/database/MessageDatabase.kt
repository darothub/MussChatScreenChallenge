package com.darothub.musschatscreen.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.darothub.musschatscreen.data.dao.MessageDao
import com.darothub.musschatscreen.data.entity.MessageEntity

@Database(entities = [MessageEntity::class], version = 1, exportSchema = false)
abstract class MessageDatabase: RoomDatabase() {
    abstract val dao: MessageDao
}