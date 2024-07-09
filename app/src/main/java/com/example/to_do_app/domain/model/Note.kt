package com.example.to_do_app.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.to_do_app.utils.Constants.NOTE_ENTITY
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity(tableName = NOTE_ENTITY)
data class Note(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    val title : String,
    val content : String,
    val timestamp : String,
    val color : Long
){
    companion object{
        val EMPTY_NOTE = Note(
            title = "",
            content = "",
            timestamp = getCurrentDate(),
            color = -1L
        )
    }
    fun Note.copy(
        id : Long? = -1,
        title: String? = null,
        content: String? = null,
        timestamp: String? = null,
        color: Long? = null
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
fun getCurrentDate(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    return currentDate.format(formatter)
}