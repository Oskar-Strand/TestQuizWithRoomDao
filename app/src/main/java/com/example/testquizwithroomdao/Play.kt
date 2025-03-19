package com.example.testquizwithroomdao

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import com.example.testquizwithroomdao.databinding.ActivityPlayBinding

class Play : AppCompatActivity() {

    private lateinit var binding: ActivityPlayBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using view binding
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // If this is the first creation, add the QuizFragment to the container.
        if (savedInstanceState == null) {
           // supportFragmentManager.beginTransaction()
           //     .replace(binding.fragmentContainer.id, QuizFragment.newInstance())
            //    .commit()
        }
    }
}



