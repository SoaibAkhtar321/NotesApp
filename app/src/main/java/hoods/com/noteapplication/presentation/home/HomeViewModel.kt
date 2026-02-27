package hoods.com.noteapplication.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hoods.com.noteapplication.common.ScreenViewState
import hoods.com.noteapplication.data.local.modal.Note
import hoods.com.noteapplication.domain.use_cases.DeleteNoteUseCase
import hoods.com.noteapplication.domain.use_cases.GetAllNotesUseCase
import hoods.com.noteapplication.domain.use_cases.UpdateNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch // 1. Import 'catch'
import kotlinx.coroutines.flow.launchIn // 2. Import 'launchIn'
import kotlinx.coroutines.flow.onEach // 3. Import 'onEach'
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    // These are the use cases injected by Hilt
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        // This will be called when the ViewModel is created
        getAllNotes()
    }

    // This function now correctly fetches notes
    private fun getAllNotes() {
        // 4. Call the USE CASE, not the function itself
        getAllNotesUseCase()
            .onEach { notes ->
                // This block is called every time the notes in the database change
                _state.value = HomeState(notes = ScreenViewState.Success(notes))
            }
            .catch { exception ->
                // This block is called if there is an error in the flow
                _state.value = HomeState(notes = ScreenViewState.Error(exception.message))
            }
            // 5. Use launchIn to start collecting the flow within the viewModelScope
            .launchIn(viewModelScope)
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            deleteNoteUseCase(noteId)
        }
    }

    fun onBookMarkChange(note: Note) {
        viewModelScope.launch {
            updateNoteUseCase(note.copy(isBookMarked = !note.isBookMarked))
        }
    }
    fun onNoteClick(noteId: Long) {
        // Navigation will be added later
        // Example:
        // _navigationEvent.emit(noteId)
    }
}

data class HomeState(
    // The UI state, which can be Loading, Success, or Error
    val notes: ScreenViewState<List<Note>> = ScreenViewState.Loading,
)
