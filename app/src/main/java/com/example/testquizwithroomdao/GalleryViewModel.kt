package com.example.testquizwithroomdao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// ViewModel responsible for managing the state and business logic of the Gallery screen
class GalleryViewModel(
    private val dao: GalleryItemDao  // Inject DAO for database operations
) : ViewModel() {

    // Holds the current sorting type (A-Z or Z-A)
    private val _sortType = MutableStateFlow(SortType.TITLE_ASC)
    val sortType = _sortType.asStateFlow()

    // Holds the current UI state of the gallery
    private val _state = MutableStateFlow(GalleryState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            // Observes changes in sort type and updates the gallery list accordingly
            _sortType.flatMapLatest { sort ->
                when (sort) {
                    SortType.TITLE_ASC -> dao.getItemsSortedByTitleASC()
                    SortType.TITLE_DESC -> dao.getItemsSortedByTitleDESC()
                }
            }.collect { items ->
                // Update state with the new sorted list of items
                _state.update { currentState -> currentState.copy(items = items) }
            }
        }
    }

    // Handles user interactions and updates the state accordingly
    fun onEvent(event: GalleryEvent) {
        when (event) {

            // Updates the title in the state when the user types in the input field
            is GalleryEvent.setTitle -> {
                _state.update { it.copy(title = event.title) }
            }

            // Updates the image URI in the state when the user selects an image
            is GalleryEvent.insertImage -> {
                _state.update { it.copy(imageUri = event.imageUri) }
            }

            // Saves a new gallery item to the database
            GalleryEvent.SaveItem -> {
                val currentState = state.value

                if (currentState.title.isNotBlank() && currentState.imageUri.isNotBlank()) {
                    viewModelScope.launch {
                        dao.insertGalleryItem(
                            GalleryItem(
                                imageUri = currentState.imageUri,
                                title = currentState.title
                            )
                        )
                    }
                }
                // Reset state after saving
                _state.update { it.copy(title = "", imageUri = "", isAddingImage = false) }
            }

            // Updates the sorting type (A-Z or Z-A)
            is GalleryEvent.sortTitle -> {
                _state.update { it.copy(sortType = event.sortType) }
                _sortType.value = event.sortType
            }

            // Deletes a gallery item from the database
            is GalleryEvent.DeleteItem -> {
                viewModelScope.launch {
                    dao.deleteGalleryItem(event.item)
                }
            }
        }
    }
}
