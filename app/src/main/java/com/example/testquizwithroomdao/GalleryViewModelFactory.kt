package com.example.testquizwithroomdao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Factory class for creating an instance of GalleryViewModel with a DAO dependency
class GalleryViewModelFactory(private val dao: GalleryItemDao) : ViewModelProvider.Factory {

    // Creates and returns the correct ViewModel instance
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Checks if the requested ViewModel class is GalleryViewModel
        if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") // Suppresses the unchecked cast warning
            return GalleryViewModel(dao) as T
        }
        // Throws an exception if an unknown ViewModel class is requested
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

