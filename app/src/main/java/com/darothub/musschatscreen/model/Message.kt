package com.darothub.musschatscreen.model

import java.time.Instant

data class Message (
    val id: Long = generateId(),
    val sender: Sender,
    val content: String,
    val timestamp: Long = Instant.now().toEpochMilli(),
    val hasTail: Boolean = false
){
    companion object {
        private var counter:Long = 0
        private val indexes = mutableListOf<Long>()
        fun generateId(): Long {
            indexes.add(counter)
            return counter++
        }
    }
}