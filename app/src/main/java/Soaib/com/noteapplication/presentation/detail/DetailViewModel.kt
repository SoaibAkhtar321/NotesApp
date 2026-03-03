package Soaib.com.noteapplication.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import Soaib.com.noteapplication.data.local.modal.Note
import Soaib.com.noteapplication.domain.use_cases.AddUseCase
import Soaib.com.noteapplication.domain.use_cases.DeleteNoteUseCase
import Soaib.com.noteapplication.domain.use_cases.GetNoteByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class DetailViewModel @AssistedInject constructor(
    private val addUseCase: AddUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    @Assisted private val noteId: Long
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()

    // Holds the reference to the observation coroutine
    private var getNoteJob: Job? = null

    val isFormNotBlank: Boolean
        get() = _state.value.content.isNotBlank()

    private val note: Note
        get() = _state.value.run {
            Note(
                id = id,
                title = title.ifBlank { "Untitled Note" },
                content = content,
                createdDate = createdDate,
                isBookMarked = isBookMarked
            )
        }

    init {
        initialize()
    }

    private fun initialize() {
        val isUpdatingNote = noteId != -1L
        _state.update { it.copy(isUpdatingNote = isUpdatingNote) }
        if (isUpdatingNote) {
            getNoteById()
        }
    }

    private fun getNoteById() {
        // Cancel any existing job before starting a new one
        getNoteJob?.cancel()
        getNoteJob = viewModelScope.launch {
            getNoteByIdUseCase(noteId).collectLatest { note ->
                // note is now nullable Flow<Note?>
                note?.let { n ->
                    _state.update {
                        it.copy(
                            id = n.id,
                            title = n.title,
                            content = n.content,
                            isBookMarked = n.isBookMarked,
                            createdDate = n.createdDate
                        )
                    }
                }
            }
        }
    }

    fun onTitleChange(title: String) = _state.update { it.copy(title = title) }
    fun onContentChange(content: String) = _state.update { it.copy(content = content) }
    fun onBookMarkChange(isBookmarked: Boolean) = _state.update { it.copy(isBookMarked = isBookmarked) }

    fun addOrUpdateNote() = viewModelScope.launch(Dispatchers.IO) {
        if (isFormNotBlank) addUseCase(note)
    }

    fun deleteNote(onSuccess: () -> Unit) = viewModelScope.launch {
        try {
            if (_state.value.isUpdatingNote) {
                // 1. CRITICAL: Stop listening to the database immediately.
                // This prevents the "App Error" crash caused by the Flow emitting null.
                getNoteJob?.cancel()

                // 2. Perform deletion on IO thread
                withContext(Dispatchers.IO) {
                    deleteNoteUseCase(note.id)
                }

                // 3. Navigate back to Home Screen
                onSuccess()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

data class DetailState(
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val isBookMarked: Boolean = false,
    val createdDate: Date = Date(),
    val isUpdatingNote: Boolean = false
)

@AssistedFactory
interface DetailAssistedFactory {
    fun create(noteId: Long): DetailViewModel
}

class DetailViewModelFactory(
    private val noteId: Long,
    private val assistedFactory: DetailAssistedFactory
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return assistedFactory.create(noteId) as T
    }
}