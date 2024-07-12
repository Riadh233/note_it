package com.example.to_do_app.domain.repository

import com.example.to_do_app.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun insertNote(note: Note) : Long
    suspend fun getNoteById(id : Long) : Note
    fun getAllNotes() : Flow<List<Note>>
    suspend fun deleteNote(note: Note)
    suspend fun deleteAllNotes()
    suspend fun resetNoteAlarm(noteId : Long)
}