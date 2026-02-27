package hoods.com.noteapplication.presentation.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    noteId: Long,
    assistedFactory: DetailAssistedFactory,
    navigateUp: () -> Unit
) {
    val viewModel: DetailViewModel = viewModel(
        modelClass = DetailViewModel::class.java,
        factory = DetailViewModelFactory(
            noteId = noteId,
            assistedFactory = assistedFactory
        )
    )

    val state by viewModel.state.collectAsState()

    DetailScreenContent(
        modifier = modifier,
        title = state.title,
        content = state.content,
        isBookMarked = state.isBookMarked,
        isUpdatingNote = state.isUpdatingNote,
        isFormNotBlank = viewModel.isFormNotBlank,
        onTitleChange = viewModel::onTitleChange,
        onContentChange = viewModel::onContentChange,
        onBookMarkChange = viewModel::onBookMarkChange,
        onDeleteClick = {
            // Wait for VM to finish deletion before navigating up
            viewModel.deleteNote(onSuccess = {
                navigateUp()
            })
        },
        navigateUp = navigateUp,
        onBtnClick = {
            viewModel.addOrUpdateNote()
            navigateUp()
        }
    )
}

@Composable
private fun DetailScreenContent(
    modifier: Modifier = Modifier,
    isUpdatingNote: Boolean,
    title: String,
    content: String,
    isBookMarked: Boolean,
    isFormNotBlank: Boolean,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onBookMarkChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    navigateUp: () -> Unit,
    onBtnClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopSection(
            title = title,
            isBookMark = isBookMarked,
            isUpdatingNote = isUpdatingNote,
            isFormNotBlank = isFormNotBlank,
            onBookMarkChange = onBookMarkChange,
            onTitleChange = onTitleChange,
            onDeleteClick = onDeleteClick,
            onNavigate = navigateUp,
            onSaveClick = onBtnClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(4.dp)
        ) {
            NotesTextField(
                value = content,
                label = "Note something down...",
                onValueChange = onContentChange,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun TopSection(
    modifier: Modifier = Modifier,
    title: String,
    isBookMark: Boolean,
    isUpdatingNote: Boolean,
    isFormNotBlank: Boolean,
    onBookMarkChange: (Boolean) -> Unit,
    onTitleChange: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onNavigate: () -> Unit,
    onSaveClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onNavigate) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }

        NotesTextField(
            modifier = Modifier.weight(1f),
            value = title,
            label = "Title",
            labelAlign = TextAlign.Center,
            onValueChange = onTitleChange
        )

        AnimatedVisibility(visible = isFormNotBlank) {
            IconButton(onClick = onSaveClick) {
                val icon = if (isUpdatingNote) Icons.Default.Update else Icons.Default.Check
                Icon(
                    imageVector = icon,
                    contentDescription = "Save",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (isUpdatingNote) {
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.DeleteSweep,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        IconButton(onClick = { onBookMarkChange(!isBookMark) }) {
            val icon = if (isBookMark) Icons.Default.BookmarkRemove else Icons.Outlined.BookmarkAdd
            Icon(imageVector = icon, contentDescription = "Bookmark")
        }
    }
}

@Composable
private fun NotesTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    labelAlign: TextAlign? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        placeholder = {
            Text(
                text = label,
                textAlign = labelAlign ?: TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        },
        textStyle = LocalTextStyle.current.copy(textAlign = labelAlign ?: TextAlign.Start)
    )
}

// Added this to resolve the Unresolved Reference and satisfy DetailScreen requirements
//class DetailViewModelFactory(
//    private val noteId: Long,
//    private val assistedFactory: DetailAssistedFactory
//) : ViewModelProvider.Factory {
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
//        return assistedFactory.create(noteId) as T
//    }
//}