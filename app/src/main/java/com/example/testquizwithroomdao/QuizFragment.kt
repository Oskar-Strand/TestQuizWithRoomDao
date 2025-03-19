package com.example.testquizwithroomdao

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.testquizwithroomdao.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    // Companion object to create new instances of the fragment
   // companion object {
   //     fun newInstance(): QuizFragment = QuizFragment()
   // }

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: QuizViewModel

    // Inflate the layout when the fragment is created
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Set up UI elements and ViewModel after the view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Obtain the DAO from DatabaseProvider and initialize the ViewModel
        val dao = DatabaseProvider.getDatabase(requireContext()).dao
        viewModel = ViewModelProvider(this, QuizViewModelFactory(dao)).get(QuizViewModel::class.java)

        // Observe the current question and update the image view
        viewModel.currentQuestion.observe(viewLifecycleOwner) { question ->
            if (question != null) {
                binding.quizImage.setImageURI(Uri.parse(question.imageUri))
            }
        }

        // Observe answer options and update the answer buttons
        viewModel.answerOptions.observe(viewLifecycleOwner) { options ->
            binding.button1.text = options.getOrNull(0) ?: "" // Assign text or empty if unavailable
            binding.button2.text = options.getOrNull(1) ?: ""
            binding.button3.text = options.getOrNull(2) ?: ""
            binding.button4.text = options.getOrNull(3) ?: ""
            resetAnswerButtonColors() // Reset button colors before new question
            enableAnswerButtons() // Re-enable buttons for new question
        }

        // Observe and update the score display
        viewModel.correctCount.observe(viewLifecycleOwner) { count ->
            binding.correctCountText.text = "Correct: $count"
        }
        viewModel.wrongCount.observe(viewLifecycleOwner) { count ->
            binding.wrongCountText.text = "Wrong: $count"
        }
        viewModel.count.observe(viewLifecycleOwner) { count ->
            binding.wrongCountText.text = "Guesses: $count"
        }


        // Set click listeners for answer buttons
        val answerClickListener = View.OnClickListener { v ->
            val button = v as Button
            disableAnswerButtons()
            val selected = button.text.toString()

            // Check if the selected answer is correct and update button color accordingly
            if (selected == viewModel.currentQuestion.value?.title) {
                button.setBackgroundColor(Color.GREEN)
            } else {
                button.setBackgroundColor(Color.RED)
                highlightCorrectButton(viewModel.currentQuestion.value?.title ?: "") // Show correct answer
            }


            Handler(Looper.getMainLooper()).postDelayed({
                viewModel.submitAnswer(selected)
            }, 1000)
        }

        // Attach the click listener to all answer buttons
        binding.button1.setOnClickListener(answerClickListener)
        binding.button2.setOnClickListener(answerClickListener)
        binding.button3.setOnClickListener(answerClickListener)
        binding.button4.setOnClickListener(answerClickListener)

        // Back button to return to MainActivity
        binding.backButton.setOnClickListener {
            activity?.finish()
        }
    }

    // Disables all answer buttons to prevent multiple selections
    private fun disableAnswerButtons() {
        binding.button1.isEnabled = false
        binding.button2.isEnabled = false
        binding.button3.isEnabled = false
        binding.button4.isEnabled = false
    }

    // Enables all answer buttons for a new question
    private fun enableAnswerButtons() {
        binding.button1.isEnabled = true
        binding.button2.isEnabled = true
        binding.button3.isEnabled = true
        binding.button4.isEnabled = true
    }

    // Resets all answer button colors to the default state
    private fun resetAnswerButtonColors() {
        binding.button1.setBackgroundColor(Color.LTGRAY)
        binding.button2.setBackgroundColor(Color.LTGRAY)
        binding.button3.setBackgroundColor(Color.LTGRAY)
        binding.button4.setBackgroundColor(Color.LTGRAY)
    }

    // Highlights the correct answer button in green when the user selects a wrong answer
    private fun highlightCorrectButton(correctTitle: String) {
        when {
            binding.button1.text.toString() == correctTitle -> binding.button1.setBackgroundColor(Color.GREEN)
            binding.button2.text.toString() == correctTitle -> binding.button2.setBackgroundColor(Color.GREEN)
            binding.button3.text.toString() == correctTitle -> binding.button3.setBackgroundColor(Color.GREEN)
            binding.button4.text.toString() == correctTitle -> binding.button4.setBackgroundColor(Color.GREEN)
        }
    }

    // Clears the binding reference when the view is destroyed to avoid memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

