package Soaib.com.noteapplication.domain.use_cases

import Soaib.com.noteapplication.data.local.modal.Note
import Soaib.com.noteapplication.domain.repository.Repository
import javax.inject.Inject

class AddUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(note: Note) {
        repository.insert(note)
    }
}

