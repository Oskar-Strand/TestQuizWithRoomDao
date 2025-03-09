package com.example.testquizwithroomdao

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuizScoreTest {

    @get:Rule
    val activityRule = ActivityTestRule(Play::class.java)

    @Test
    fun testCorrectAnswerIncrementsCorrectAndTotalGuesses() {
        var initialCorrect = 0
        var initialTotalGuesses = 0

        // Get initial correct count
        onView(withId(R.id.correctCountText)).check { view, _ ->
            val text = (view as android.widget.TextView).text.toString()
            initialCorrect = text.replace("Correct: ", "").toInt()
        }

        // Get initial total guesses (correct + wrong)
        onView(withId(R.id.wrongCountText)).check { view, _ ->
            val text = (view as android.widget.TextView).text.toString()
            initialTotalGuesses = text.replace("Wrong: ", "").toInt() + initialCorrect
        }

        // Click a button (assuming it's the correct answer)
        onView(withId(R.id.button1)).perform(click())

        // Wait for UI update
        Thread.sleep(1500)

        // Expected values after correct guess
        val expectedCorrect = initialCorrect + 1
        val expectedTotalGuesses = initialTotalGuesses + 1

        // Check if correct count is incremented
        onView(withId(R.id.correctCountText)).check(matches(withText(containsString("Correct: $expectedCorrect"))))

        // Check if total guesses is incremented
        onView(withId(R.id.wrongCountText)).check { view, _ ->
            val currentTotalGuesses = (view as android.widget.TextView).text.toString().replace("Wrong: ", "").toInt() + expectedCorrect
            assert(currentTotalGuesses == expectedTotalGuesses) { "Total guesses did not increment correctly." }
        }
    }

    @Test
    fun testWrongAnswerIncrementsOnlyTotalGuesses() {
        var initialCorrect = 0
        var initialTotalGuesses = 0

        // Get initial correct count
        onView(withId(R.id.correctCountText)).check { view, _ ->
            val text = (view as android.widget.TextView).text.toString()
            initialCorrect = text.replace("Correct: ", "").toInt()
        }

        // Get initial total guesses (correct + wrong)
        onView(withId(R.id.wrongCountText)).check { view, _ ->
            val text = (view as android.widget.TextView).text.toString()
            initialTotalGuesses = text.replace("Wrong: ", "").toInt() + initialCorrect
        }

        // Click a button (assuming it's the WRONG answer)
        onView(withId(R.id.button2)).perform(click())

        // Wait for UI update
        Thread.sleep(1500)

        // Expected values after wrong guess
        val expectedWrong = initialTotalGuesses - initialCorrect + 1 // Only wrong count increments
        val expectedTotalGuesses = initialTotalGuesses + 1

        // Ensure correct count does NOT change
        onView(withId(R.id.correctCountText)).check(matches(withText(containsString("Correct: $initialCorrect"))))

        // Ensure total guesses increments
        onView(withId(R.id.wrongCountText)).check { view, _ ->
            val currentTotalGuesses = (view as android.widget.TextView).text.toString().replace("Wrong: ", "").toInt() + initialCorrect
            assert(currentTotalGuesses == expectedTotalGuesses) { "Total guesses did not increment correctly." }
        }
    }
}
