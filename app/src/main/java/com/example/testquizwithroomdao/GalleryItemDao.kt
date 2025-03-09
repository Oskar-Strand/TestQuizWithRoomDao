package com.example.testquizwithroomdao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

// DAO (Data Access Object) for performing database operations on the GalleryItem table
@Dao
interface GalleryItemDao {

    // Inserts a new item or updates it if it already exists
    @Upsert
    suspend fun insertGalleryItem(item: GalleryItem)

    // Deletes a specific item from the database
    @Delete
    suspend fun deleteGalleryItem(item: GalleryItem)

    // Retrieves all items sorted in ascending order by title
    @Query("SELECT * FROM galleryitem ORDER BY title ASC")
    fun getItemsSortedByTitleASC(): Flow<List<GalleryItem>>

    // Retrieves all items sorted in descending order by title
    @Query("SELECT * FROM galleryitem ORDER BY title DESC")
    fun getItemsSortedByTitleDESC(): Flow<List<GalleryItem>>

}
