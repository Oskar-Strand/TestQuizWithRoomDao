package com.example.testquizwithroomdao

// Defines events that can be triggered in the Gallery (used for ViewModel)
sealed interface GalleryEvent {

    // Event to save a new gallery item
    object SaveItem: GalleryEvent

    // Event to set an image URI when a user selects an image
    data class insertImage(val imageUri: String): GalleryEvent

    // Event to set a title when a user types a title for the image
    data class setTitle(val title: String): GalleryEvent

    // Event to change the sorting order (A-Z or Z-A)
    data class sortTitle(val sortType: SortType): GalleryEvent

    // Event to delete an item from the gallery
    data class DeleteItem(val item: GalleryItem): GalleryEvent
}
