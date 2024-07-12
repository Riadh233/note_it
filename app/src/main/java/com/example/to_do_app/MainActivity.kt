package com.example.to_do_app

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.to_do_app.ui.navigation.addEditScreenPage
import com.example.to_do_app.ui.navigation.noteScreenPage
import com.example.to_do_app.ui.viewmodels.AddEditNoteViewModel
import com.example.to_do_app.ui.viewmodels.NoteViewModel
import com.example.to_do_app.utils.NavigationRoutes
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
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
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                RequestNotificationPermissionDialog()
            }

        }
    }

    @Composable
    fun MyAppNavHost(
        navController: NavHostController,
        noteViewModel: NoteViewModel,
        addEditNoteViewModel: AddEditNoteViewModel,
    ) {
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun RequestNotificationPermissionDialog() {
        val permissionState =
            rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)

        var showDialog by remember { mutableStateOf(true) }

        if (showDialog && !permissionState.status.isGranted && permissionState.status.shouldShowRationale) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Notification Permission Required") },
                text = { Text(stringResource(id = R.string.notfication_permission)) },
                confirmButton = {
                    Button(onClick = { permissionState.launchPermissionRequest() }) {
                        Text("Grant")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Deny")
                    }
                }
            )
        } else {
            LaunchedEffect(key1 = Unit) {
                permissionState.launchPermissionRequest()
            }
        }
    }
}

