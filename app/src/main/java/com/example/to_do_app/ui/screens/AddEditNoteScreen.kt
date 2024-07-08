package com.example.to_do_app.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.to_do_app.domain.model.Note
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun AddEditNoteScreen(
    noteState: StateFlow<Note>,
    onNoteTitleChanged: (String) -> Unit,
    onNoteContentChanged: (String) -> Unit,
    onBackButtonPressed: () -> Unit,
    ) {
    val note = noteState.collectAsState()
    Log.d("note",note.value.title)
    val lifecycleOwner = LocalLifecycleOwner.current

    // Handle back button press
    BackHandler(
        onBack = {
        // Create or update note when back button is pressed
        lifecycleOwner.lifecycleScope.launch {
            onBackButtonPressed()
        }
    })
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
    ) {
        TextField(
            value = note.value.title,
            onValueChange = { onNoteTitleChanged(it) },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = note.value.content,
            onValueChange = { onNoteContentChanged(it) },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onBackButtonPressed.invoke()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Note")
        }
    }
}