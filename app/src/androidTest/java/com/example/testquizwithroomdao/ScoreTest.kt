package com.example.testquizwithroomdao

import androidx.compose.ui.test.IdlingResource
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.ext.junit.rules.ActivityScenarioRule
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScoreTest {


    @get:Rule
    val activityRule = ActivityScenarioRule(Play::class.java)

    private val idlingResource = CountingIdlingResource("Loading")

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun teardown() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    fun testCorrectAnswerIncrementsScore() {

        activityRule.scenario.onActivity { activity ->
            val fragment = activity.supportFragmentManager
                .findFragmentById(R.id.fragmentContainer) as QuizFragment


            Thread.sleep(4000)


           // fragment.viewModel.currentQuestion.observe(fragment.viewLifecycleOwner) { question ->
            //    if (question != null) {


           //     }
         //   }
        }



        var correctAnswerText: String? = null
        activityRule.scenario.onActivity { activity ->
            val fragment = activity.supportFragmentManager
                .findFragmentById(R.id.fragmentContainer) as QuizFragment
            correctAnswerText = fragment.viewModel.currentQuestion.value?.title
        }
        assertNotNull("Current question title is null", correctAnswerText)


        onView(withText(correctAnswerText)).perform(click())
        Thread.sleep(1500)

        var correctCount: Int? = null
        activityRule.scenario.onActivity { activity ->
            val fragment = activity.supportFragmentManager
                .findFragmentById(R.id.fragmentContainer) as QuizFragment
            correctCount = fragment.viewModel.correctCount.value
        }
        assertNotNull("Correct Count is null", correctCount)
        assert(correctCount == 1)
    }
}
