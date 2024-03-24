package com.darothub.musschatscreen.model

import java.time.Instant

data class Message (
    val id: Long = generateId(),
    val sender: Sender,
    val content: String,
    val timestamp: Long = Instant.now().toEpochMilli(),
    val hasTailImage: Boolean = false
){
//    val hasTailImage:Boolean
//        get() {
//            return this.id == (indexes.size - 1).toLong()
//        }
    companion object {
        private var counter:Long = 0
        private val indexes = mutableListOf<Long>()
        fun generateId(): Long {
            indexes.add(counter)
            return counter++
        }
    }
}