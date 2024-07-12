package com.example.to_do_app.di

import com.example.to_do_app.domain.repository.NoteRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface RepositoryEntryPoint {
    fun getNoteRepository(): NoteRepository
}