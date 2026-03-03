package Soaib.com.noteapplication.data.repository

import Soaib.com.noteapplication.data.local.modal.Note
import Soaib.com.noteapplication.data.local.modal.NoteDao
import Soaib.com.noteapplication.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : Repository {

    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes()
    }


    override fun getNoteById(id: Long): Flow<Note?> {
        return noteDao.getNoteById(id)
    }

    override suspend fun insert(note: Note) {
        noteDao.insertNote(note)
    }

    override suspend fun update(note: Note) {
        noteDao.updateNote(note)
    }

    override suspend fun delete(id: Long) {
        noteDao.delete(id)
    }

    override fun getBookMarkedNotes(): Flow<List<Note>> {
        return noteDao.getBookMarkedNotes()
    }
}