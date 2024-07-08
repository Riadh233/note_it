package com.example.to_do_app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.compose.AppTheme
import com.example.to_do_app.ui.screens.AddEditNoteScreen
import com.example.to_do_app.ui.screens.NotesScreen
import com.example.to_do_app.ui.viewmodels.AddEditNoteViewModel
import com.example.to_do_app.ui.viewmodels.NoteViewModel
import com.example.to_do_app.utils.NavigationRoutes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                MyApp()
            }
        }
    }

    @SuppressLint(
        "UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation",
        "StateFlowValueCalledInComposition"
    )
    @Composable
    fun MyApp() {
        val navController = rememberNavController()
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        //navigate to add note screen
                        navController.navigate(NavigationRoutes.AddEditScreenRoute.route + "/0")

                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }

            }
        ) { innerPadding ->
            // Your main content
            NavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                startDestination = NavigationRoutes.NoteScreenRoute.route
            ) {
                composable(route = NavigationRoutes.NoteScreenRoute.route) {
                    val noteViewModel = hiltViewModel<NoteViewModel>()
                    NotesScreen(
                        notesList = noteViewModel.searchResults.collectAsState().value,
                        onNoteClicked = { id ->
                            navController.navigate(NavigationRoutes.AddEditScreenRoute.route + "/${id}")
                        },
                        searchText = noteViewModel.searchQuery,
                        onUpdateSearchQuery = noteViewModel::updateSearchQuery
                    )
                }
                composable(route = NavigationRoutes.AddEditScreenRoute.route + "/{noteId}",
                    arguments = listOf(
                        navArgument(name = "noteId") {
                            type = NavType.IntType
                        }
                    )) {
                    val addEditNoteViewModel = hiltViewModel<AddEditNoteViewModel>()
                    val noteId = it.arguments?.getInt("noteId") ?: 0

                    addEditNoteViewModel.getNoteById(noteId)

                    AddEditNoteScreen(
                        noteState = addEditNoteViewModel.notesState,
                        onBackButtonPressed = {
                            addEditNoteViewModel.addEditNote()
                            navController.navigateUp()
                        },
                        onNoteTitleChanged = { title ->
                            addEditNoteViewModel.onNoteTitleChanged(
                                title
                            )
                        },
                        onNoteContentChanged = { content ->
                            addEditNoteViewModel.onNoteContentChanged(
                                content
                            )
                        }
                    )
                }
            }
        }
    }
}

