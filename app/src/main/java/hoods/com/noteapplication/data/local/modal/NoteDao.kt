package hoods.com.noteapplication.data.local.modal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY createdDate")
    fun getAllNotes(): Flow<List<Note>>

    // FIX: Added '?' to make it nullable.
    // This allows Room to emit null when a note is deleted without crashing.
    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteById(id: Long): Flow<Note?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: Note)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM notes WHERE isBookMarked = 1 ORDER BY createdDate DESC")
    fun getBookMarkedNotes(): Flow<List<Note>>
}