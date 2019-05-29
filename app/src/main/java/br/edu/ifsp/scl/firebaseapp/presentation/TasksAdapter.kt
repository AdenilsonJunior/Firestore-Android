package br.edu.ifsp.scl.firebaseapp.presentation

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import br.edu.ifsp.scl.firebaseapp.R
import br.edu.ifsp.scl.firebaseapp.model.Task
import kotlinx.android.synthetic.main.item_task.view.*

class TasksAdapter(context: Context, private val tasks: MutableList<Task> = mutableListOf()) :
    ArrayAdapter<Task>(context, R.layout.item_task, tasks) {

    private var callback: DeleteCallback? = null
    override fun getCount() = tasks.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)

        val task = tasks[position]
        with(view) {
            textViewTask.text = task.title
            textViewLevel.text = context.getString(R.string.text_level_placeholder, task.level.toString())
            backgroundTask.setBackgroundColor(
                if (task.completed) {
                    ContextCompat.getColor(context, R.color.green_light)
                } else {
                    ContextCompat.getColor(context, R.color.red_light)
                }
            )
            imageViewDelete.setOnClickListener {
                callback?.deleteTask(task)
            }
        }

        return view
    }

    fun addDeleteCallback(callback: DeleteCallback){
        this.callback = callback
    }

    interface DeleteCallback {
        fun deleteTask(task: Task)
    }
}