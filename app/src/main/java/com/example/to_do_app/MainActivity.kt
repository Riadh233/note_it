package com.example.to_do_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.to_do_app.data.alarms.AlarmScheduler
import com.example.to_do_app.ui.navigation.addEditScreenPage
import com.example.to_do_app.ui.navigation.noteScreenPage
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
        val addEditNoteViewModel = hiltViewModel<AddEditNoteViewModel>()
        val noteViewModel = hiltViewModel<NoteViewModel>()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            MyAppNavHost(
                navController = navController,
                noteViewModel = noteViewModel,
                addEditNoteViewModel = addEditNoteViewModel,
            )
        }
    }

    @Composable
    fun MyAppNavHost(
        navController: NavHostController,
        noteViewModel: NoteViewModel,
        addEditNoteViewModel: AddEditNoteViewModel,
    ) {
        Log.d("note state", "nav host called")
        NavHost(
            navController = navController,
            modifier = Modifier,
            startDestination = NavigationRoutes.NoteScreenRoute.route
        ) {
            noteScreenPage(
                navController = navController,
                noteViewModel = noteViewModel,
                addEditNoteViewModel = addEditNoteViewModel,
            )
            addEditScreenPage(
                navController = navController,
                addEditNoteViewModel = addEditNoteViewModel,
            )
        }
    }
}

