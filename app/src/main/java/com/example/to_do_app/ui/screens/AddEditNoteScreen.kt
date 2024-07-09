package com.example.to_do_app.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.to_do_app.domain.model.Note
import com.example.to_do_app.utils.Constants.NOTE_COLORS
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun AddEditNoteScreen(
    modifier: Modifier,
    noteState: StateFlow<Note>,
    onNoteTitleChanged: (String) -> Unit,
    onNoteContentChanged: (String) -> Unit,
    onBackButtonPressed: () -> Unit,
    onShowColorPicker: Boolean,
    onDismissColorPicker: () -> Unit,
    onNoteColorSelected : (Long) -> Unit
) {
    val note = noteState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    var isTitleFocused by remember { mutableStateOf(false) }
    var isContentFocused by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    BackHandler(
        onBack = {
            lifecycleOwner.lifecycleScope.launch {
                onBackButtonPressed()
            }
        })
    Log.d("note state",note.value.toString())
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .padding(12.dp)
            .fillMaxSize()
    ) {
        Text(text = note.value.timestamp, fontSize = 12.sp)
        OutlinedTextField(
            value = note.value.title,
            onValueChange = onNoteTitleChanged,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState -> isTitleFocused = focusState.isFocused },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
            ),
            placeholder = {
                Text(
                    text = "Title",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            textStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    isTitleFocused = false // Hide hint on done action
                }
            )
        )

        OutlinedTextField(

            value = note.value.content,
            onValueChange = onNoteContentChanged,
            placeholder = { Text("Any thoughts?") },
            modifier = Modifier
                .fillMaxSize()
                .onFocusChanged { focusState -> isContentFocused = focusState.isFocused },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Default),
            keyboardActions = KeyboardActions(
                onDone = {
                    isContentFocused = false // Hide hint on done action
                }
            )
        )
        if (onShowColorPicker) {
            ModalBottomSheet(
                onDismissRequest = { onDismissColorPicker() },
                sheetState = sheetState,
                dragHandle = null,
                modifier = Modifier.wrapContentSize(),
                shape = RectangleShape
            ) {
                LazyRow(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.padding(5.dp)
                ) {
                    items(NOTE_COLORS.size){i ->
                        val color = NOTE_COLORS[i]
                        ColorPickerItem(color = color, isSelected = note.value.color == color, onNoteColorSelected = onNoteColorSelected )
                    }
                }
            }
        }
    }
}

@Composable
fun ColorPickerItem(
    color: Long,
    isSelected : Boolean,
    onNoteColorSelected : (Long) -> Unit
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(shape = CircleShape)
            .background(color = Color(color))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = CircleShape
            )
            .clickable {
                onNoteColorSelected(color)
            }
        ,
        contentAlignment = Alignment.Center
    ){
        AnimatedVisibility(
            visible = isSelected,
            enter = fadeIn(animationSpec = tween(durationMillis = 500)),
            exit = fadeOut(animationSpec = tween(durationMillis = 500))
        ) {
            Icon(imageVector = Icons.Default.Check, contentDescription = "color selected", modifier = Modifier.size(30.dp))
        }
    }
}
