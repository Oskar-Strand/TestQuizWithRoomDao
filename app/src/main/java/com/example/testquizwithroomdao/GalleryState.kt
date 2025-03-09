package com.example.testquizwithroomdao

// Represents the current state of the gallery screen
data class GalleryState(
    val items: List<GalleryItem> = emptyList(),  // List of gallery items (default: empty)
    val title: String = "",                      // Title of the current image being added
    val imageUri: String = "",                   // URI of the selected image
    val isAddingImage: Boolean = false,          // Controls if the add image dialog is open
    val sortType: SortType = SortType.TITLE_ASC  // Current sorting order (A-Z by default)
)

