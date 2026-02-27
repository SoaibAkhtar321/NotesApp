package hoods.com.noteapplication.domain.use_cases

import hoods.com.noteapplication.data.local.modal.Note // Ensure correct import
import hoods.com.noteapplication.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val repository: Repository
){
    // FIX: Explicitly define the return type as Flow<Note?>
    // This handles cases where the note is deleted and returns null
    operator fun invoke(id: Long): Flow<Note?> = repository.getNoteById(id)
}