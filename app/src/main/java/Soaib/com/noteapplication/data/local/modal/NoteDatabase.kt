package Soaib.com.noteapplication.data.local.modal

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import Soaib.com.noteapplication.data.local.modal.converters.DateConverter // <-- ADD THIS IMPORT

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class) // This line will now work correctly
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao
}
