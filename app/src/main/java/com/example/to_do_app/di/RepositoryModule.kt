package com.example.to_do_app.di

import com.example.to_do_app.data.repository.NoteRepositoryImpl
import com.example.to_do_app.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMyRepository(moviesRepositoryImpl: NoteRepositoryImpl): NoteRepository
}