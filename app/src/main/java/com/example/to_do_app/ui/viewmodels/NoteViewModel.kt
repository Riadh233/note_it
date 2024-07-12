package com.example.to_do_app.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do_app.data.alarms.AlarmScheduler
import com.example.to_do_app.domain.model.Note
import com.example.to_do_app.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {
    private val _notesState = MutableStateFlow<List<Note>>(emptyList())
    var searchQuery by mutableStateOf("")
        private set

    @OptIn(FlowPreview::class)
    val searchQueryFlow = snapshotFlow { searchQuery.trim() }.debounce(500).conflate()

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<Note>> = searchQueryFlow.flatMapLatest { query ->
        searchNotes(query)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        noteRepository.getAllNotes()
            .onEach { notes ->
                _notesState.value = notes
            } // Handle collected data
            .launchIn(viewModelScope) // Start collecting in the ViewModel's scope
    }

    fun deleteNote(note : Note){
        viewModelScope.launch {
            noteRepository.deleteNote(note)
        }
    }

    fun deleteAllNotes(){
        viewModelScope.launch {
            noteRepository.deleteAllNotes()
        }
    }

    private fun searchNotes(query: String): Flow<List<Note>> {
        return if (query.isBlank()) {
            _notesState
        } else {
            _notesState.map { notes ->
                notes.filter { note ->
                    note.title.contains(query, ignoreCase = true) || note.content.contains(query, ignoreCase = true)
                }
            }
        }
    }
    fun updateSearchQuery(query: String){
        searchQuery = query
    }

    fun cancelAlarm(noteId : Long){
        viewModelScope.launch {
            alarmScheduler.cancel(noteId)
        }
    }
}