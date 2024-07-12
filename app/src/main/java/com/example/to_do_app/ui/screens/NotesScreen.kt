package com.example.to_do_app.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.to_do_app.domain.model.Note
import com.example.to_do_app.ui.composables.SearchBar

@Composable
fun NotesScreen(
    modifier: Modifier,
    notesList: List<Note>,
    onNoteClicked: (Long) -> Unit,
    searchText: String,
    onUpdateSearchQuery: (String) -> Unit,
    onDeleteNote: (Note) -> Unit,
) {
    Column(
        modifier = modifier.padding(10.dp),
        verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start
    ) {

        Spacer(modifier = Modifier.height(10.dp))
        SearchBar(searchText = searchText, updateSearchQuery = onUpdateSearchQuery)
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
                    NoteItem(note = note, onNoteClicked = onNoteClicked, onDeleteNote)
                }
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(note: Note, onNoteClicked: (Long) -> Unit, onDeleteNote: (Note) -> Unit) {
    val noteColor =
        if (note.color == -1L) MaterialTheme.colorScheme.surface else Color(note.color).copy(alpha = 0.5f)
    val isVisible by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = isVisible,
        exit = fadeOut(animationSpec = tween(durationMillis = 300))
    ) {
        Card(
            elevation = CardDefaults.elevatedCardElevation(2.dp),
            shape = RoundedCornerShape(size = 14.dp),
            onClick = {
                onNoteClicked(note.id)
            },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = noteColor)
                    .padding(3.dp),
            ) {

                Icon(imageVector = Icons.Default.Close,
                    contentDescription = "remove note",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onDeleteNote(note) }
                        .align(Alignment.TopEnd)
                )

                Text(
                    text = note.timestamp,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 10.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 3.dp, bottom = 3.dp)
                )

                Column(
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxSize()
                ) {
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
                    Spacer(modifier = Modifier.height(2.dp))

                    if (note.alarmTime != null) {
                        AlarmItem(alarmTime = formatMillisToDateTime(note.alarmTime),showCancelButton = false){}
                    }
                }
            }
        }
    }
}
