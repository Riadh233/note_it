package com.example.to_do_app.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.to_do_app.R
import com.example.to_do_app.ui.screens.AddEditNoteScreen
import com.example.to_do_app.ui.viewmodels.AddEditNoteViewModel
import com.example.to_do_app.utils.NavigationRoutes
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.addEditScreenPage(
    navController: NavHostController,
    addEditNoteViewModel: AddEditNoteViewModel,
) {
    composable(route = NavigationRoutes.AddEditScreenRoute.route) {

        val noteColorState = addEditNoteViewModel.notesState.collectAsState().value.color
        val noteColor = if (noteColorState == -1L) MaterialTheme.colorScheme.surface else Color(
            noteColorState
        )
        val currentNote = addEditNoteViewModel.notesState.collectAsState()
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
                        IconButton(onClick = { addEditNoteViewModel.onShowTimePicker() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.add_reminder),
                                contentDescription = "set reminder",
                                tint = MaterialTheme.colorScheme.onBackground
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
                note = currentNote,
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
                showTimePicker = addEditNoteViewModel.showTimePicker,
                onDismissColorPicker = { addEditNoteViewModel.onTriggerColorPickerVisibility() },
                onDismissTimePicker = {addEditNoteViewModel.onDismissTimePicker() },
                onNoteColorSelected = { color ->
                    addEditNoteViewModel.onNoteColorChanged(color)

                },
                onSetAlarmTime = {hour,minute ->
                    addEditNoteViewModel.onSetAlarmTime(
                        getTimeInMillis(
                            hour = hour,
                            minute = minute
                        )
                    )
                },
                onCancelAlarm = {
                    addEditNoteViewModel.onCancelAlarm()
                }
            )
        }
    }
}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onDismissRequest: () -> Unit,
    confirmButton: @Composable (() -> Unit),
    dismissButton: @Composable (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = containerColor
                ),
            color = containerColor
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    dismissButton?.invoke()
                    confirmButton()
                }
            }
        }
    }
}

fun formatTime(hour: Int, minute: Int): String {
    val currentTime = Calendar.getInstance()
    val selectedTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    if (selectedTime.before(currentTime)) {
        selectedTime.add(Calendar.DAY_OF_MONTH, 1)
    }

    val dateFormat = SimpleDateFormat("d MMM 'at' HH:mm", Locale.getDefault())
    return dateFormat.format(selectedTime.time)
}

fun getTimeInMillis(hour: Int, minute: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    if (calendar.timeInMillis <= System.currentTimeMillis()) {
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }
    return calendar.timeInMillis
}


