package com.sem.lvl2task2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sem.lvl2task2.databinding.ActivityMainBinding
import com.sem.lvl2task2.databinding.ItemQuestionBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val questions = arrayListOf<Question>()
    private val questionAdapter = QuestionAdapter(questions)
    // Don't forget to create a binding object as you did in previous assignments.
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        addQuestion("A 'val' and 'var' are the same.", false)
        addQuestion("Mobile Application Development grants 12 ECTS.", true)
        addQuestion("A Unit in Kotlin corresponds to a void in Java.", true)
        addQuestion("In Kotlin 'when' replaces the 'switch' operator in Java.", true)
    }

    private fun initViews() {

        binding.rvReminders.layoutManager =
                LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL,
                        false)
        binding.rvReminders.adapter = questionAdapter

        binding.rvReminders.addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))

        createItemTouchHelper().attachToRecyclerView(rvReminders)

    }

    private fun addQuestion(question: String, answer: Boolean) {
        if(question.isNotBlank()) {
            questions.add(Question(question, answer))
            questionAdapter.notifyDataSetChanged()
        }
    }

//    else {
//        Snackbar.make(etWrong, "Thats not correct!", Snackbar.LENGTH_SHORT).show()
//    }

    private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

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
                if(direction == ItemTouchHelper.RIGHT && questions.get(position).answer == true) {
                    questions.removeAt(position)
                    questionAdapter.notifyDataSetChanged()
                } else if(direction == ItemTouchHelper.LEFT && questions.get(position).answer == false) {
                    questions.removeAt(position)
                    questionAdapter.notifyDataSetChanged()
                } else {
                    Snackbar.make(etWrong, "Thats not correct!", Snackbar.LENGTH_SHORT).show()
                    questionAdapter.notifyDataSetChanged()
                }

            }
        }

        return ItemTouchHelper(callback)
    }


}



data class Question (
        var questionText: String,
        var answer: Boolean
)

class QuestionAdapter(private val questions: List<Question>) : RecyclerView.Adapter<QuestionAdapter.ViewHolder>(){


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = ItemQuestionBinding.bind(itemView)

        fun databind(question: Question) {
            binding.tvQuestion.text = question.questionText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        )
    }

    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int {
        return questions.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(questions[position])
    }


}
