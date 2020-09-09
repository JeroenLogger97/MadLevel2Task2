package com.example.madlevel2task2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel2task2.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val questions = arrayListOf<Question>()
    private val questionAdapter = QuestionAdapter(questions)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        binding.rvQuestions.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        binding.rvQuestions.adapter = questionAdapter
        binding.rvQuestions.addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))

        for (i in Question.QUESTIONS.indices) {
            questions.add(Question(Question.QUESTIONS[i]))
        }

        questionAdapter.notifyDataSetChanged()

        createItemTouchHelper().attachToRecyclerView(rvQuestions)
    }

    /**
     * Create a touch helper to recognize when a user swipes an item from a recycler view.
     * An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
     * and uses callbacks to signal when a user is performing these actions.
     */
    private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val questionIndex = Question.QUESTIONS.indexOf(questions[position].question)
                val answer = Question.ANSWERS[questionIndex]
                var isCorrect = false

                if (direction == ItemTouchHelper.LEFT) {
                    isCorrect = answer == false
                } else if (direction == ItemTouchHelper.RIGHT) {
                    isCorrect = answer == true
                }

                if (isCorrect) {
                    questions.removeAt(position)
                    onCorrectAnswer()
                } else {
                    onWrongAnswer()
                }
                questionAdapter.notifyDataSetChanged()
            }
        }
        return ItemTouchHelper(callback)
    }

    private fun onCorrectAnswer() {
        Snackbar.make(binding.rvQuestions, "Correct!", Snackbar.LENGTH_SHORT).show()
    }

    private fun onWrongAnswer() {
        Snackbar.make(binding.rvQuestions, "Wrong answer, the question will not be removed!", Snackbar.LENGTH_SHORT).show()
    }
}
