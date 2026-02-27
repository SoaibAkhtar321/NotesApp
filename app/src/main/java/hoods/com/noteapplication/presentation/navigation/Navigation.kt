package hoods.com.noteapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import hoods.com.noteapplication.presentation.bookmark.BookmarkScreen
import hoods.com.noteapplication.presentation.bookmark.BookmarkViewModel
import hoods.com.noteapplication.presentation.detail.DetailAssistedFactory
import hoods.com.noteapplication.presentation.detail.DetailScreen
import hoods.com.noteapplication.presentation.home.HomeScreen
import hoods.com.noteapplication.presentation.home.HomeViewModel

enum class Screen {
    Home, Details, Bookmark
}

@Composable
fun NoteNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    homeViewModel: HomeViewModel,
    bookmarkViewModel: BookmarkViewModel,
    startDestination: Screen = Screen.Home,
    assistedFactory: DetailAssistedFactory
) {

    NavHost(
        navController = navHostController,
        startDestination = startDestination.name,
        modifier = modifier
    ) {

        // ---------------- HOME ----------------
        composable(Screen.Home.name) {
            val state by homeViewModel.state.collectAsState()
            HomeScreen(
                state = state,
                onBookMarkChange = homeViewModel::onBookMarkChange,
                onDeleteNote = homeViewModel::deleteNote,
                onNoteClick = { noteId ->
                    navHostController.navigateSingleTop("${Screen.Details.name}?id=$noteId")
                }
            )
        }

        // ---------------- BOOKMARK ----------------
        composable(Screen.Bookmark.name) {
            val state by bookmarkViewModel.state.collectAsState()
            BookmarkScreen(
                state = state,
                // FIX: Pass Modifier.fillMaxSize() or similar here.
                // Do NOT pass the 'modifier' from NoteNavigation again,
                // or it will apply the Scaffold padding twice!
                onBookmarkChange = bookmarkViewModel::onBookMarkChange,
                onDeleteNote = bookmarkViewModel::deleteNote,
                onNoteClick = { noteId ->
                    navHostController.navigateSingleTop("${Screen.Details.name}?id=$noteId")
                }
            )
        }

        // ---------------- DETAILS (FIXED) ----------------
        composable(
            route = "${Screen.Details.name}?id={id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                    defaultValue = -1L // FIX: This allows navigating to "Details" without an ID
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("id") ?: -1L

            DetailScreen(
                noteId = noteId,
                assistedFactory = assistedFactory,
                navigateUp = { navHostController.navigateUp() }
            )
        }
    }
}

fun NavHostController.navigateSingleTop(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}