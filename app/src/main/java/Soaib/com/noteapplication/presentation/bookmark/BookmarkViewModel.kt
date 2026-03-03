package Soaib.com.noteapplication.presentation.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import Soaib.com.noteapplication.common.ScreenViewState
import Soaib.com.noteapplication.data.local.modal.Note
import Soaib.com.noteapplication.domain.use_cases.DeleteNoteUseCase
import Soaib.com.noteapplication.domain.use_cases.FilteredBookmarkedNotes
import Soaib.com.noteapplication.domain.use_cases.UpdateNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val filteredBookmarkNotes: FilteredBookmarkedNotes,
    private val deleteNoteUseCase: DeleteNoteUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(BookmarkState())
    val state: StateFlow<BookmarkState> = _state.asStateFlow()

    init {
        getBookMarkedNotes()
    }

    private fun getBookMarkedNotes() {
        filteredBookmarkNotes()
            .onEach { notes ->
                _state.value = BookmarkState(
                    notes = ScreenViewState.Success(notes)
                )
            }
            .catch { exception ->
                _state.value = BookmarkState(
                    notes = ScreenViewState.Error(exception.message)
                )
            }
            .launchIn(viewModelScope)
    }

    fun onBookMarkChange(note: Note) {
        viewModelScope.launch {
            updateNoteUseCase(
                note.copy(isBookMarked = !note.isBookMarked)
            )
        }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            deleteNoteUseCase(noteId)
        }
    }
}

data class BookmarkState(
    val notes: ScreenViewState<List<Note>> = ScreenViewState.Loading,
)
