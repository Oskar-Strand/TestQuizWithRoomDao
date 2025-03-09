package com.example.testquizwithroomdao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Factory class to create an instance of QuizViewModel with a DAO dependency
class QuizViewModelFactory(private val dao: GalleryItemDao) : ViewModelProvider.Factory {

    // Creates and returns an instance of QuizViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the requested ViewModel class is QuizViewModel
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") // Suppresses unchecked cast warning
            return QuizViewModel(dao) as T
        }
        // Throw an exception if an unknown ViewModel is requested
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
