package com.example.testquizwithroomdao

import androidx.room.*

// Defines a Room database entity for storing gallery items
@Entity(tableName = "galleryitem")
data class GalleryItem(
    @PrimaryKey(autoGenerate = true)  val id: Int = 0,  // Unique ID for each item (auto-incremented)
    val imageUri: String,  // URI of the stored image (file path or resource URI)
    val title: String      // Title of the image (user-defined or default)
)
