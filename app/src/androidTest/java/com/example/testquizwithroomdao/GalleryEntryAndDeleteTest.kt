package com.example.testquizwithroomdao

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.hamcrest.Matchers.allOf
import org.junit.*

import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GalleryEntryAndDeleteTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(Gallery::class.java) // Ensures Activity is launched

    @get:Rule
    val composeTestRule = createAndroidComposeRule<Gallery>() // Enables Compose testing

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testAddingAndDeletingGalleryEntry() {
        onView(withId(android.R.id.content)).check(matches(isDisplayed())) // Ensure activity is running

        val initialItemCount = composeTestRule.onAllNodesWithText("Delete Item").fetchSemanticsNodes().size

        composeTestRule.onNodeWithContentDescription("Add").assertExists().performClick()

        composeTestRule.onNodeWithText("Upload Image").assertExists().performClick()

        Thread.sleep(5000)
        composeTestRule.onNodeWithText("Title").assertExists().performTextInput("Test Image")

        composeTestRule.onNodeWithText("Save").assertExists().performClick()

        val newItemCount = composeTestRule.onAllNodesWithText("Delete Item").fetchSemanticsNodes().size
        Thread.sleep(1000)
       Assert.assertEquals(initialItemCount + 1, newItemCount)
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag("DeleteButton_Test Image").performClick()
        Thread.sleep(2000)
        val finalItemCount = composeTestRule.onAllNodesWithText("Delete Item").fetchSemanticsNodes().size

        Assert.assertEquals(initialItemCount, finalItemCount)
    }
}



