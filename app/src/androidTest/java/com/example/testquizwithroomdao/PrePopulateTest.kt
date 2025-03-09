package com.example.testquizwithroomdao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PrePopulateTest {
    private lateinit var database: GalleryItemDatabase
    private lateinit var dao: GalleryItemDao

    @Before
    fun setup() {
        // Create an in-memory version of the database.
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, GalleryItemDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.dao

        // Manually pre-populate the database with default items.
        runBlocking {
            val packageName = context.packageName
            val img1Uri = "android.resource://$packageName/${R.drawable.giraffe}"
            val img2Uri = "android.resource://$packageName/${R.drawable.okapi}"
            val img3Uri = "android.resource://$packageName/${R.drawable.zebra}"

            dao.insertGalleryItem(GalleryItem(imageUri = img1Uri, title = "Giraffe"))
            dao.insertGalleryItem(GalleryItem(imageUri = img2Uri, title = "Okapi"))
            dao.insertGalleryItem(GalleryItem(imageUri = img3Uri, title = "Zebra"))
        }
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun itemsAreInGallery() = runBlocking {
        // Query the items from the database.
        val items = dao.getItemsSortedByTitleASC().first()
        // Check that there are three items.
        assertEquals(3, items.size)
        // Check that each expected title is present.
        assertTrue(items.any { it.title == "Giraffe" })
        assertTrue(items.any { it.title == "Okapi" })
        assertTrue(items.any { it.title == "Zebra" })
    }

    @Test
    fun printDatabaseContents() = runBlocking {
        // Query the database.
        val items = dao.getItemsSortedByTitleASC().first()
        // Print each item to the console.
        items.forEach { item ->
            println("Item: Title = ${item.title}, ImageUri = ${item.imageUri}")
        }
    }

}
