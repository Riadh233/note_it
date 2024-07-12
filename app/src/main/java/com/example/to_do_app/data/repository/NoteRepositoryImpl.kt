package com.example.to_do_app.data.repository

import com.example.to_do_app.data.AppDatabase
import com.example.to_do_app.data.dao.NotesDao
import com.example.to_do_app.domain.model.Note
import com.example.to_do_app.domain.repository.NoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val dispatcherIO : CoroutineDispatcher
)  : NoteRepository  {
    private val noteDao : NotesDao = appDatabase.notesDao()

    override suspend fun insertNote(note: Note) : Long {
       return withContext(dispatcherIO){
            noteDao.insertNote(note)
        }
    }

    override suspend fun getNoteById(id: Long): Note {
        return withContext(dispatcherIO){
            noteDao.getNoteById(id)
        }
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().flowOn(dispatcherIO)
    }

    override suspend fun deleteNote(note : Note) {
        withContext(dispatcherIO){
            noteDao.deleteNote(note)
        }
    }

    override suspend fun deleteAllNotes() {
        withContext(dispatcherIO){
            noteDao.deleteAllNotes()
        }
    }

    override suspend fun resetNoteAlarm(noteId: Long) {
        withContext(dispatcherIO){
            noteDao.resetNoteAlarm(noteId)
        }
    }
}