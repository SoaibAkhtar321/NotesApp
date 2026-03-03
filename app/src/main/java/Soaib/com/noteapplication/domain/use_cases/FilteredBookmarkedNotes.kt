package Soaib.com.noteapplication.domain.use_cases

import Soaib.com.noteapplication.data.local.modal.Note
import Soaib.com.noteapplication.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilteredBookmarkedNotes @Inject constructor(
    private val repository: Repository
){
    operator fun invoke(): Flow<List<Note>>{
        return repository.getBookMarkedNotes()

    }
}