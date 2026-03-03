package Soaib.com.noteapplication.presentation.bookmark

import androidx.compose.foundation.layout.Arrangement // Added
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import Soaib.com.noteapplication.common.ScreenViewState
import Soaib.com.noteapplication.data.local.modal.Note
import Soaib.com.noteapplication.presentation.home.NoteCard
import java.util.Date

@Composable
fun BookmarkScreen(
    state: BookmarkState,
    modifier: Modifier = Modifier,
    onBookmarkChange: (note: Note) -> Unit,
    onDeleteNote: (Long) -> Unit,
    onNoteClick: (Long) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (state.notes) {
            is ScreenViewState.Loading -> {
                CircularProgressIndicator()
            }

            is ScreenViewState.Success -> {
                val notes = state.notes.data
                if (notes.isEmpty()) {
                    Text(text = "No bookmarked notes found.")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        // contentPadding adds space around the edges of the list
                        contentPadding = PaddingValues(12.dp),
                        // verticalArrangement adds the space BETWEEN each card
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(
                            items = notes,
                            key = { _, note -> note.id }
                        ) { index, note ->
                            NoteCard(
                                index = index,
                                note = note,
                                onBookMarkChange = onBookmarkChange,
                                onDeleteNote = onDeleteNote,
                                onNoteClick = onNoteClick
                            )
                        }
                    }
                }
            }
            is ScreenViewState.Error -> {
                Text(
                    text = state.notes.message ?: "Unknown Error",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Bookmark Success State")
@Composable
fun PrevBookMark() {
    val sampleNotes = listOf(
        Note(id = 1, title = "First Bookmark", content = "Content 1", isBookMarked = true, createdDate = Date()),
        Note(id = 2, title = "Second Bookmark", content = "Content 2", isBookMarked = true, createdDate = Date())
    )
    BookmarkScreen(
        state = BookmarkState(
            notes = ScreenViewState.Success(sampleNotes)
        ),
        onBookmarkChange = {},
        onDeleteNote = {},
        onNoteClick = {}
    )
}