package hoods.com.noteapplication.domain.use_cases

import hoods.com.noteapplication.data.local.modal.Note
import hoods.com.noteapplication.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilteredBookmarkedNotes @Inject constructor(
    private val repository: Repository
){
    operator fun invoke(): Flow<List<Note>>{
        return repository.getBookMarkedNotes()

    }
}