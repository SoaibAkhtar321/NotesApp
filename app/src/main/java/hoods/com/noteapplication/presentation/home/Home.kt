package hoods.com.noteapplication.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hoods.com.noteapplication.common.ScreenViewState
import hoods.com.noteapplication.data.local.modal.Note
import java.util.Date

// 1. ADDED HomeState data class for better organization


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    onBookMarkChange: (Note) -> Unit,
    onDeleteNote: (noteId: Long) -> Unit,
    onNoteClick: (noteId: Long) -> Unit // 2. ADDED onNoteClick parameter
) {
    // 3. ADDED a Box to center the Loading and Error states
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
                HomeDetail(
                    notes = notes,
                    modifier = modifier,
                    onBookMarkChange = onBookMarkChange,
                    onDeleteNote = onDeleteNote,
                    onNoteClick = onNoteClick // 4. PASS the correct parameter
                ) // 5. REMOVED the extra {}
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

@Composable
private fun HomeDetail(
    notes: List<Note>,
    modifier: Modifier,
    onBookMarkChange: (note: Note) -> Unit,
    onDeleteNote: (noteId: Long) -> Unit,
    onNoteClick: (noteId: Long) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize() // Make grid take up the whole screen
    ) {
        itemsIndexed(
            items = notes,
            key = { _, note -> note.id } // Add key for performance
        ) { index, note ->
            NoteCard(
                index = index,
                note = note,
                onBookMarkChange = onBookMarkChange,
                onDeleteNote = onDeleteNote,
                onNoteClick = onNoteClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCard(
    index: Int,
    note: Note,
    onBookMarkChange: (note: Note) -> Unit,
    onDeleteNote: (noteId: Long) -> Unit,
    onNoteClick: (noteId: Long) -> Unit
) {
    val isEvenIndex = index % 2 == 0
    val shape = when {
        isEvenIndex -> {
            RoundedCornerShape(
                topStart = 50f,
                bottomEnd = 50f
            )
        }
        else -> {
            RoundedCornerShape(
                topEnd = 50f,
                bottomStart = 50f
            )
        }
    }

    val icon = if (note.isBookMarked) Icons.Default.BookmarkRemove
    else Icons.Outlined.BookmarkAdd

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = shape,
        onClick = {
            onNoteClick(note.id)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = note.title,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = note.content,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End, // Arranged to end
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onBookMarkChange(note) }) {
                    Icon(imageVector = icon, contentDescription = "Bookmark")
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Error State")
@Composable
fun PrevHome() {
    HomeScreen(
        state = HomeState(notes = ScreenViewState.Success(notes)
        ),
        onBookMarkChange = {},
        onDeleteNote = {},
        onNoteClick = {}
    )
}
val placeHolderText =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
val notes = listOf(
    Note(
        title = "Room Database",
        content = placeHolderText + placeHolderText,
        createdDate = Date()
    ),
    Note(
        title = "JetPack Compose",
        content = "Testing",
        createdDate = Date(),
        isBookMarked = true,
    ),
    Note(
        title = "Room Database",
        content = placeHolderText + placeHolderText,
        createdDate = Date()
    ),
    Note(
        title = "JetPack Compose",
        content = "Testing",
        createdDate = Date(),
        isBookMarked = true,
    )
)