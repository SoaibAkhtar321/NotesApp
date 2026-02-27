package hoods.com.noteapplication.domain.repository

import hoods.com.noteapplication.data.local.modal.Note
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getAllNotes(): Flow<List<Note>>

    // FIX: Changed Note to Note? (nullable) to prevent crash on deletion
    fun getNoteById(id: Long): Flow<Note?>

    suspend fun insert(note: Note)
    suspend fun update(note: Note)
    suspend fun delete(id: Long)
    fun getBookMarkedNotes(): Flow<List<Note>>
}