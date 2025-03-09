
import com.example.testquizwithroomdao.Gallery
import com.example.testquizwithroomdao.MainActivity
import com.example.testquizwithroomdao.Play


import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testNavigationToGallery() {
        // Click the "Gallery" button in Compose
        composeTestRule.onNodeWithText("Gallery").performClick()

        // Verify the intent to open Gallery activity was sent
        Intents.intended(hasComponent(Gallery::class.java.name))
    }

    @Test
    fun testNavigationToQuiz() {
        // Click the "Start Game" button in Compose
        composeTestRule.onNodeWithText("Start Game").performClick()

        // Verify the intent to open Play activity was sent
        Intents.intended(hasComponent(Play::class.java.name))
    }
}
