package com.example.to_do_app.domain.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.to_do_app.utils.Constants.NOTE_ENTITY

@Entity(tableName = NOTE_ENTITY)
data class Note(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    val title : String,
    val content : String,
    val timestamp : Long,
    val color : Int
){
    companion object{
        val EMPTY_NOTE = Note(
            title = "",
            content = "",
            timestamp = -1L,
            color = -1
        )
    }
    fun Note.copy(
        id : Long? = -1,
        title: String? = null,
        content: String? = null,
        timestamp: Long? = null,
        color: Int? = null
    ): Note {
        return Note(
            id = this.id, // Assuming ID should remain the same
            title = title ?: this.title,
            content = content ?: this.content,
            timestamp = timestamp ?: this.timestamp,
            color = color ?: this.color
        )
    }
}
