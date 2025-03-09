package com.example.testquizwithroomdao

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testquizwithroomdao.ui.theme.TestQuizWithRoomDaoTheme

// Main activity that serves as the entry point of the app
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enables edge-to-edge display (modern full-screen UI)
        setContent {
            TestQuizWithRoomDaoTheme {
                MainScreen() // Calls the main screen UI
            }
        }
    }
}

// Composable function that builds the main menu UI
@Composable
fun MainScreen() {
    val context = LocalContext.current // Get the current context (used for launching activities)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly, // Space elements evenly in the column
        horizontalAlignment = Alignment.CenterHorizontally // Center elements horizontally
    ) {
        // App title
        Text(
            text = "Animal Quiz",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Button to start the quiz game
        Button(
            onClick = {
                val intent = Intent(context, Play::class.java)
                context.startActivity(intent) // Starts the Play activity
            },
            modifier = Modifier.fillMaxWidth(0.7f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Cyan,
                contentColor = Color.Black
            )
        ) {
            Text(text = "Start Game")
        }

        // Button to navigate to the gallery
        Button(
            onClick = {
                val intent = Intent(context, Gallery::class.java)
                context.startActivity(intent) // Starts the Gallery activity
            },
            modifier = Modifier.fillMaxWidth(0.7f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Cyan,
                contentColor = Color.Black
            )
        ) {
            Text(text = "Gallery")
        }
    }
}

// Preview function to show the UI inside Android Studio
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
