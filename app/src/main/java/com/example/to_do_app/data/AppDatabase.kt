package com.example.to_do_app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.to_do_app.data.dao.NotesDao
import com.example.to_do_app.domain.model.Note

@Database(
    entities = [Note::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}