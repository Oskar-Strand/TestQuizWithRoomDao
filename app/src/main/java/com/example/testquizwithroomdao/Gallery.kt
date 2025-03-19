package com.example.testquizwithroomdao

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.testquizwithroomdao.ui.theme.TestQuizWithRoomDaoTheme

// Main Activity for the Gallery, loads the Compose UI
class Gallery : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestQuizWithRoomDaoTheme {
                GalleryScreen() // Call the main Gallery UI
            }
        }
    }
}

// Composable function that builds the entire gallery screen
@Composable
fun GalleryScreen() {
    val context = LocalContext.current
    // Get database DAO instance
    val dao = DatabaseProvider.getDatabase(context).dao
    // Create ViewModel with factory
    val factory = GalleryViewModelFactory(dao)
    val viewModel: GalleryViewModel = viewModel(factory = factory)

    // Observe the gallery state from the ViewModel
    val state by viewModel.state.collectAsState()

    // Local UI state variables for sorting and dialog visibility
    var currentSort by remember { mutableStateOf(SortType.TITLE_ASC) }
    var showAddDialog by remember { mutableStateOf(false) }
    var imageUriInput by remember { mutableStateOf("") }
    var titleInput by remember { mutableStateOf("") }

    // Scaffold provides the top bar, floating action button, and content area
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button to exit the gallery
                Button(
                    modifier = Modifier.padding(16.dp),
                    onClick = { (context as? Activity)?.finish() }
                ) {
                    Text("Back")
                }
                // Sort button to toggle between A-Z and Z-A sorting
                Button(
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        currentSort = if (currentSort == SortType.TITLE_ASC) SortType.TITLE_DESC else SortType.TITLE_ASC
                        viewModel.onEvent(GalleryEvent.sortTitle(currentSort))
                    }
                ) {
                    Text(text = if (currentSort == SortType.TITLE_ASC) "Sort Z-A" else "Sort A-Z")
                }
            }
        },
        floatingActionButton = {
            // Floating button for adding a new image
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        // Main content area where images are displayed
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(text = "Number of items: ${state.items.size}")
            // LazyColumn is a more efficient version of RecyclerView
            LazyColumn {
                items(state.items) { item ->
                    GalleryItemRow(
                        item = item,
                        onDelete = { itemToDelete -> viewModel.onEvent(GalleryEvent.DeleteItem(itemToDelete)) }
                    )
                }
            }
        }

        // AlertDialog for adding a new image entry
        if (showAddDialog) {
            // Image picker to select an image from the gallery
            val imagePickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument()
            ) { uri: Uri? ->
                uri?.let {
                    // Grant read/write permission for the selected image
                    val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    try {
                        context.contentResolver.takePersistableUriPermission(it, flags)
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                    }
                    imageUriInput = it.toString()
                }
            }

            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Add Gallery Item") },
                text = {
                    Column {
                        // Upload image button
                        Button(
                            onClick = { imagePickerLauncher.launch(arrayOf("image/*")) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = if (imageUriInput.isEmpty()) "Upload Image" else "Image Selected")
                        }
                        // Text input field for title
                        OutlinedTextField(
                            value = titleInput,
                            onValueChange = { titleInput = it },
                            label = { Text("Title") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        // Save the image to the database through ViewModel events
                        viewModel.onEvent(GalleryEvent.insertImage(imageUriInput))
                        viewModel.onEvent(GalleryEvent.setTitle(titleInput))
                        viewModel.onEvent(GalleryEvent.SaveItem)
                        // Reset dialog inputs and close
                        imageUriInput = ""
                        titleInput = ""
                        showAddDialog = false
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    Button(onClick = { showAddDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

// Composable function that represents a single gallery item (image + title + delete button)
@Composable
fun GalleryItemRow(item: GalleryItem, onDelete: (GalleryItem) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Display the image using Coil (asynchronous image loading)
        Image(
            painter = rememberAsyncImagePainter(model = item.imageUri),
            contentDescription = "Gallery image",
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        // Display the title of the image
        Text(
            text = item.title,
            modifier = Modifier.weight(1f)
        )
        // Delete button to remove the item
        Button(onClick = { onDelete(item) },
            modifier = Modifier.testTag("DeleteButton_${item.title}")) {
            Text(text = "Delete Item")
        }
    }
}
