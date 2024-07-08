package com.example.to_do_app.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.to_do_app.domain.model.Note
import com.example.to_do_app.ui.composables.SearchBar

@Composable
fun NotesScreen(
    notesList: List<Note>,
    onNoteClicked: (Int) -> Unit,
    searchText : String,
    onUpdateSearchQuery : (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(6.dp),
        verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        Text(
            text = "Note it!", style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(10.dp))
        SearchBar(searchText = searchText , updateSearchQuery = onUpdateSearchQuery)
        Spacer(modifier = Modifier.height(10.dp))

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalItemSpacing = 14.dp,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(notesList.size) { i ->
                val note = notesList[i]
                if (note != Note.EMPTY_NOTE) {
                    Log.d("note", note.toString())
                    NoteItem(note = note, onNoteClicked = onNoteClicked)
                }
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(note: Note, onNoteClicked: (Int) -> Unit) {
    val noteColor = if (note.color == -1) MaterialTheme.colorScheme.surface else Color(note.color)
    Card(
        modifier = Modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = noteColor),
        shape = RoundedCornerShape(size = 14.dp),
        onClick = {
            onNoteClicked(note.id)
        },
    ) {
        Log.d("note id ", note.id.toString())
        Column(modifier = Modifier.padding(6.dp)) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Normal,
                maxLines = 15,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
