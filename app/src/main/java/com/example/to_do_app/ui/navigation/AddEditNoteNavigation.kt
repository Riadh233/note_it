package com.example.to_do_app.ui.navigation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.sharp.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.to_do_app.R
import com.example.to_do_app.ui.screens.AddEditNoteScreen
import com.example.to_do_app.ui.viewmodels.AddEditNoteViewModel
import com.example.to_do_app.utils.NavigationRoutes

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.addEditScreenPage(
    navController: NavHostController,
    addEditNoteViewModel: AddEditNoteViewModel
) {
    composable(route = NavigationRoutes.AddEditScreenRoute.route) {

        val noteColorState = addEditNoteViewModel.notesState.collectAsState().value.color
        val noteColor = if (noteColorState == -1L) MaterialTheme.colorScheme.surface else Color(
            noteColorState
        )
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = noteColor),
                    title = {},
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                addEditNoteViewModel.addEditNote()
                                navController.navigateUp()
                            }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "navigate up"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = "set completed"
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Sharp.DateRange,
                                contentDescription = "set completed"
                            )
                        }
                        IconButton(onClick = { addEditNoteViewModel.onTriggerColorPickerVisibility() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.color_palette),
                                contentDescription = "color palette",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                )
            },
            containerColor = noteColor
        ) { innerPadding ->
            AddEditNoteScreen(
                modifier = Modifier.padding(innerPadding),
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
                },
                onShowColorPicker = addEditNoteViewModel.showColorPicker,
                onDismissColorPicker = { addEditNoteViewModel.onTriggerColorPickerVisibility() },
                onNoteColorSelected = { color ->
                    addEditNoteViewModel.onNoteColorChanged(color)

                }
            )
        }
    }
}