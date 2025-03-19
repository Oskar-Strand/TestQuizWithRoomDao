package com.example.testquizwithroomdao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// ViewModel responsible for handling the quiz logic
class QuizViewModel(private val dao: GalleryItemDao) : ViewModel() {

    // Full list of questions loaded from the Room database
    private var fullList: List<GalleryItem> = listOf()

    // A pool of questions that will be used in the current quiz session
    private var quizPool: MutableList<GalleryItem> = mutableListOf()

    // LiveData for the currently displayed question
    private val _currentQuestion = MutableLiveData<GalleryItem?>()
    val currentQuestion: LiveData<GalleryItem?> = _currentQuestion

    // LiveData for the answer options (4 multiple-choice answers)
    private val _answerOptions = MutableLiveData<List<String>>()
    val answerOptions: LiveData<List<String>> = _answerOptions

    // LiveData to keep track of the number of correct answers
    private val _correctCount = MutableLiveData<Int>(0)
    val correctCount: LiveData<Int> = _correctCount

    // LiveData to keep track of the number of wrong answers
    private val _wrongCount = MutableLiveData<Int>(0)
    val wrongCount: LiveData<Int> = _wrongCount

    // LiveData to keep track of the number of wrong answers
    private val _count = MutableLiveData<Int>(0)
    val count: LiveData<Int> = _count

    init {
        viewModelScope.launch {
            // Load all gallery items (quiz questions) from Room database
            fullList = dao.getItemsSortedByTitleASC().first()
            if (fullList.isNotEmpty()) {
                quizPool = fullList.toMutableList()
                startNewRound() // Start the first quiz question
            }
        }
    }

    // Starts a new question round in the quiz
    fun startNewRound() {
        if (fullList.isEmpty()) return // If there are no questions, exit

        // If the quiz pool is empty, refill it with all questions
        if (quizPool.isEmpty()) {
            quizPool = fullList.toMutableList()
        }

        // Randomly select a correct answer from the pool
        val correctItem = quizPool.random()
        _currentQuestion.value = correctItem

        // Select three incorrect answers (ensuring they are not the correct one)
        val wrongOptions = fullList.filter { it.title != correctItem.title }
            .shuffled() // Shuffle the options
            .take(3)    // Take three random incorrect options
            .map { it.title }

        // Combine the correct answer with the wrong options and shuffle them
        _answerOptions.value = (wrongOptions + correctItem.title).shuffled()
    }

    // Handles the user's answer selection
    fun submitAnswer(selectedAnswer: String) {
        val current = _currentQuestion.value ?: return

        if (selectedAnswer == current.title) {
            // If the answer is correct, increment the correct count
            _correctCount.value = (_correctCount.value ?: 0) + 1
            _count.value = (count.value ?: 0) + 1
            // Remove the question from the quiz pool to avoid repetition
            quizPool.removeAll { it.id == current.id }
        } else {
            // If the answer is wrong, increment the wrong count
            _wrongCount.value = (_wrongCount.value ?: 0) + 1
            _count.value = (count.value ?: 0) + 1
        }

        // Start the next question round
        startNewRound()
    }
}
