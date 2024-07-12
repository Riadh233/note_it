package com.example.to_do_app.ui.navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.to_do_app.ui.screens.NotesScreen
import com.example.to_do_app.ui.viewmodels.AddEditNoteViewModel
import com.example.to_do_app.ui.viewmodels.NoteViewModel
import com.example.to_do_app.utils.NavigationRoutes

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.noteScreenPage(
    navController: NavHostController,
    noteViewModel: NoteViewModel,
    addEditNoteViewModel: AddEditNoteViewModel,
){
    composable(route = NavigationRoutes.NoteScreenRoute.route) {
        var showActionMenu by remember { mutableStateOf(false) }
        var showAlertDialogue by remember { mutableStateOf(false) }
        val notesList = noteViewModel.searchResults.collectAsState().value
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                    title = {
                        Text(
                            text = "Note it!", style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    actions = {
                        IconButton(onClick = { showActionMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "more actions"
                            )
                        }

                        DropdownMenu(
                            modifier = Modifier.wrapContentSize(),
                            expanded = showActionMenu,
                            onDismissRequest = { showActionMenu = false }) {
                            DropdownMenuItem(
                                text = { Text(text = "Delete all notes") },
                                onClick = {
                                    showAlertDialogue = true
                                    showActionMenu = false
                                })
                        }
                    }

                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(NavigationRoutes.AddEditScreenRoute.route)
                        addEditNoteViewModel.getNoteById(0)
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }

            }
        ) {innerPadding ->
            NotesScreen(
                modifier = Modifier.padding(innerPadding),
                notesList = notesList,
                onNoteClicked = { id ->
                    navController.navigate(NavigationRoutes.AddEditScreenRoute.route)
                    addEditNoteViewModel.getNoteById(id)
                },
                searchText = noteViewModel.searchQuery,
                onUpdateSearchQuery = noteViewModel::updateSearchQuery,
                onDeleteNote = {note ->
                    noteViewModel.deleteNote(note)
                    noteViewModel.cancelAlarm(note.id)
                },
            )
            if(showAlertDialogue){
                AlertDialog(
                    title = {
                        Text(text = "Delete all Notes")
                    },
                    text = {
                        Text(text = "do you confirm deleting all your notes ?")
                    },
                    onDismissRequest = {
                        showAlertDialogue = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                notesList.forEach { note ->
                                    noteViewModel.cancelAlarm(note.id)
                                }
                                noteViewModel.deleteAllNotes()
                                showAlertDialogue = false
                            }
                        ) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showAlertDialogue = false
                            }
                        ) {
                            Text("Dismiss")
                        }
                    }
                )
            }
        }
    }
}
