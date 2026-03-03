package Soaib.com.noteapplication.domain.use_cases

import Soaib.com.noteapplication.domain.repository.Repository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: Repository
)  {
    suspend operator fun invoke(id: Long) = repository.delete(id)

}