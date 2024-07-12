package com.example.to_do_app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.to_do_app.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note) : Long

    @Query("SELECT * FROM notes_table WHERE id = :id")
    fun getNoteById(id: Long) : Note

    @Query("SELECT * FROM notes_table")
    fun getAllNotes(): Flow<List<Note>>

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM notes_table")
    suspend fun deleteAllNotes()

    @Query("UPDATE notes_table SET alarmTime = NULL WHERE id = :id")
    suspend fun resetNoteAlarm(id: Long)

}