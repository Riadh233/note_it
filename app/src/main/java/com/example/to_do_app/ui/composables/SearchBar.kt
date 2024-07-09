package com.example.to_do_app.ui.composables

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.to_do_app.R

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchText: String,
    updateSearchQuery: (String) -> Unit
) {

    val isTrailingIconVisible = searchText.isNotBlank()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val isFocused = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = searchText,
        onValueChange = updateSearchQuery,
        modifier = modifier
            .fillMaxWidth().height(50.dp)
            .background(
                shape = RoundedCornerShape(20.dp),
               color =  MaterialTheme.colorScheme
                    .surfaceColorAtElevation(6.dp)
            )
            .onFocusChanged { focusState -> isFocused.value = focusState.isFocused },
        placeholder = { Text(text = "Search Notes", color = MaterialTheme.colorScheme.onSurface) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search),
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        trailingIcon = {
            if (isTrailingIconVisible)
                Icon(
                    modifier = Modifier.clickable { updateSearchQuery("") },
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.clear_search_text_icon),
                    tint = MaterialTheme.colorScheme.onSurface
                )
        },
        shape = RoundedCornerShape(20.dp),

        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions {
            keyboardController?.hide()
            focusManager.clearFocus()
        },
    )
}