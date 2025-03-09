package com.example.testquizwithroomdao

import androidx.room.Database
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class GalleryItemDaoTest {
    private lateinit var database: GalleryItemDatabase
    private lateinit var dao: GalleryItemDao

    @Before
    fun setup() {
        // Create an in-memory version of the database.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GalleryItemDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.dao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertGalleryItem_savesItemCorrectly() = runBlocking {
        val testItem = GalleryItem(imageUri = "test_uri", title = "Test Title")
        dao.insertGalleryItem(testItem)
        val items = dao.getItemsSortedByTitleASC().first()
        items.forEach { item ->
            println("GalleryItem(title=${item.title}, imageUri=${item.imageUri})")
        }
        assertTrue(items.any { it.title == "Test Title" && it.imageUri == "test_uri" })

    }

}