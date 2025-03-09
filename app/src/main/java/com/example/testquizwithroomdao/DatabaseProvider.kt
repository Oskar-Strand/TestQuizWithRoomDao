package com.example.testquizwithroomdao

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

object DatabaseProvider {
    @Volatile
    private var INSTANCE: GalleryItemDatabase? = null

    fun getDatabase(context: Context): GalleryItemDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                GalleryItemDatabase::class.java,
                "gallery_item_database"
            ).addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Pre-populate the database with three default images.
                    Executors.newSingleThreadExecutor().execute {

                        runBlocking {
                            val packageName = context.packageName
                            val img1Uri = "android.resource://$packageName/${R.drawable.giraffe}"
                            val img2Uri = "android.resource://$packageName/${R.drawable.okapi}"
                            val img3Uri = "android.resource://$packageName/${R.drawable.zebra}"

                            // Insert the default GalleryItems.
                            getDatabase(context).dao.insertGalleryItem(
                                GalleryItem(imageUri = img1Uri, title = "Giraffe")
                            )
                            getDatabase(context).dao.insertGalleryItem(
                                GalleryItem(imageUri = img2Uri, title = "Okapi")
                            )
                            getDatabase(context).dao.insertGalleryItem(
                                GalleryItem(imageUri = img3Uri, title = "Zebra")
                            )
                        }
                    }
                }
            })
                .build()
            INSTANCE = instance
            instance
        }
    }
}
