package com.example.to_do_app.ui.viewmodels


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do_app.domain.model.Note
import com.example.to_do_app.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
) : ViewModel() {
    private val _noteState = MutableStateFlow<Note>(Note.EMPTY_NOTE)
    val notesState: StateFlow<Note> = _noteState

    var showColorPicker by mutableStateOf(false)
        private set

    fun getNoteById(noteId : Int){
        viewModelScope.launch {
            if(noteId != 0){
                _noteState.value = noteRepository.getNoteById(id = noteId)
            }else{
                _noteState.value = Note.EMPTY_NOTE
            }
        }
    }

    fun onNoteTitleChanged(title : String){
        _noteState.value = _noteState.value.copy(title = title)
    }
    fun onNoteContentChanged(content : String){
        _noteState.value = _noteState.value.copy(content = content)
    }
    fun onNoteColorChanged(color: Long){
        _noteState.value =  _noteState.value.copy(color = color)
    }

    fun addEditNote(){
        viewModelScope.launch {
            if(_noteState.value.title.isEmpty() && _noteState.value.content.isEmpty()) return@launch
            noteRepository.insertNote(_noteState.value)
        }
    }

    fun onTriggerColorPickerVisibility(){
        showColorPicker = !(showColorPicker)
    }
}