activity_play.xml	Hosts the QuizFragment using a FragmentContainerView. UI container for quiz experience.
fragment_quiz.xml	The layout for quiz questions. Contains image view, 4 buttons for answers, and text views for score.

Gallery.kt	Jetpack Compose screen for browsing, adding, and deleting quiz entries.
GalleryScreen()	Composable function inside Gallery.kt that builds the full UI using Compose.

GalleryItem.kt	Data class for each gallery item (title + image URI).
GalleryViewModel.kt	Handles UI logic and state for the gallery (adding, sorting, deleting).
GalleryViewModelFactory.kt	Provides a DAO to the GalleryViewModel.
GalleryItemDao.kt	DAO interface for database queries (insert, delete, fetch).
GalleryItemDatabase.kt	Room database configuration file.
DatabaseProvider.kt	Initializes Room database and pre-populates it with default animal images.
GalleryEvent.kt	Sealed class representing UI events like image added, deleted, etc.
GalleryState.kt	Data class holding gallery UI state (list of items, sort order, etc.).
SortType.kt	Enum for choosing how to sort gallery items (by title, ID).

Play.kt	Activity that loads QuizFragment and handles lifecycle/UI container.
QuizFragment.kt	Main UI logic for playing the quiz. Observes ViewModel, handles button clicks, displays scores.
QuizViewModel.kt	Core quiz logic: handles question pool, score tracking, answer checking, and LiveData.
QuizViewModelFactory.kt	Supplies DAO into the QuizViewModel.
Play process is calling following Play.kt -> activity_play.xml -> QuizFragment.kt -> fragment_quiz.xml && QuizViewModel

__Test Cases__
Navigation Test:
Wish is to be able to go to Gallery and Play the game.
Should press the button for both and open both.
Passes

ScoreTest:
Test Scenario:	When the user is presented with a question and selects the correct answer, the app should increment the correct guess counter by 1.
As a user, when I see a question with four answers, and I choose the correct one, I expect my "Correct" score to increase on the screen.
Expected Behavior	After selecting the correct answer, the correctCount value in the ViewModel should change from 0 to 1, and the UI should reflect "Correct: 1".
Test Method	testCorrectAnswerIncrementsScore()
How it works	- Launches the Play activity
- Accesses the active QuizFragment
- Retrieves the correct answer from the ViewModel
- Simulates clicking the correct answer button
- Waits for UI update
- Reads the updated correctCount from ViewModel and checks if it equals 1
!Passes


GalleryEntryAndDeleteTest:
The user opens the Gallery screen, adds a new quiz item by selecting an image from their device, enters a title, saves it, and then deletes it.
As a user, I want to add a new item to the quiz gallery by selecting an image and typing a title, and I want to be able to delete it afterward to manage my quiz entries.
Expected Behavior	- The number of gallery items should increase by 1 after saving the new image.
- The count should return to its original value after deleting the newly added item.
Test Method	testAddingAndDeletingGalleryEntry()
How it works	- Launches the Gallery activity.
- Uses Compose test rule to interact with UI elements.
- Clicks "Add", "Upload Image", and waits manually for image to be selected (no intent stub here).
- Inputs title "Test Image" and saves.
- Asserts that the number of items increases by 1.
- Then deletes "Test Image" and asserts count returns to original.
Passed (with manual image selection)

Comments	- Image selection is not stubbed — user must manually pick an image during the test run on emulator.
- Could be fully automated in the future with Intent Stubbing.
- Does delete right image, but sometimes it deletes the wrong image. But the test passes because final count is expected to be 3, and to pass it does not matter which image is deleted.
