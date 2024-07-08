package com.example.to_do_app.utils

sealed class NavigationRoutes(val route: String) {
    data object NoteScreenRoute : NavigationRoutes("note_screen")
    data object AddEditScreenRoute : NavigationRoutes("add_edit_screen")
}