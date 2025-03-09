package com.example.testquizwithroomdao

import androidx.room.Database
import androidx.room.RoomDatabase

// Defines the Room database for storing gallery items
@Database(
    entities = [GalleryItem::class], // Specifies the table(s) in the database
    version = 1,                     // Database version (increment when schema changes)
    exportSchema = false              // Disables schema export for simplicity
)
abstract class GalleryItemDatabase: RoomDatabase() {

    // Provides access to the DAO (Data Access Object) for performing database operations
    abstract val dao: GalleryItemDao

}
