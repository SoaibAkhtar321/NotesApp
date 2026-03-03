package Soaib.com.noteapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import Soaib.com.noteapplication.presentation.bookmark.BookmarkViewModel
import Soaib.com.noteapplication.presentation.detail.DetailAssistedFactory
import Soaib.com.noteapplication.presentation.home.HomeViewModel
import Soaib.com.noteapplication.presentation.navigation.NoteNavigation
import Soaib.com.noteapplication.presentation.navigation.Screen
import Soaib.com.noteapplication.presentation.navigation.navigateSingleTop
import Soaib.com.noteapplication.ui.theme.NoteApplicationTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var assistedFactory: DetailAssistedFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NoteApp(assistedFactory = assistedFactory)
                }
            }
        }
    }
}

@Composable
fun NoteApp(assistedFactory: DetailAssistedFactory) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val bookmarkViewModel: BookmarkViewModel = hiltViewModel()
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute == Screen.Home.name || currentRoute == Screen.Bookmark.name) {
                BottomAppBar(
                    actions = {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            InputChip(
                                selected = currentRoute == Screen.Home.name,
                                onClick = { navController.navigateSingleTop(Screen.Home.name) },
                                label = { Text(text = "Home") },
                                leadingIcon = { Icon(Icons.Default.Home, null) }
                            )

                            InputChip(
                                selected = currentRoute == Screen.Bookmark.name,
                                onClick = { navController.navigateSingleTop(Screen.Bookmark.name) },
                                label = { Text(text = "Bookmarks") },
                                leadingIcon = { Icon(Icons.Default.Bookmark, null) }
                            )
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                // Navigate to Details with ID -1 for a NEW note
                                navController.navigateSingleTop("${Screen.Details.name}?id=-1")
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Note")
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NoteNavigation(
            modifier = Modifier.padding(paddingValues),
            navHostController = navController,
            homeViewModel = homeViewModel,
            bookmarkViewModel = bookmarkViewModel,
            assistedFactory = assistedFactory
        )
    }
}