package br.edu.ifsp.scl.firebaseapp.presentation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import br.edu.ifsp.scl.firebaseapp.R
import br.edu.ifsp.scl.firebaseapp.controller.TaskListContract
import br.edu.ifsp.scl.firebaseapp.controller.TaskListController
import br.edu.ifsp.scl.firebaseapp.model.Task
import kotlinx.android.synthetic.main.activity_task_list.*

class TaskListActivity : AppCompatActivity(), TaskListContract {

    private var adapter: TasksAdapter? = null
    private val controller = TaskListController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        setupListView()
        controller.getTasks()
        setTitle(R.string.title_all)
    }

    private fun setupListView() {
        adapter = TasksAdapter(this)
        adapter?.addDeleteCallback(object : TasksAdapter.DeleteCallback {
            override fun deleteTask(task: Task) {
                controller.deleteTask(task)
            }

        })
        listViewTasks.adapter = adapter
        listViewTasks.setOnItemClickListener { parent, view, position, id ->
            val task = parent.getItemAtPosition(position) as Task
            controller.handleComplete(task)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemMenuCreate -> startActivity(Intent(this, AddTaskActivity::class.java))
            R.id.itemMenuAll -> {
                controller.getTasks()
                setTitle(R.string.title_all)
            }
            R.id.itemMenuAllCompleted -> {
                controller.getAllCompleted()
                setTitle(R.string.title_all_completed)
            }
            R.id.itemMenuAllCompletedAndLevel3 -> {
                controller.getAllCompletedAndLevel3()
                setTitle(R.string.title_all_completed_level_3)
            }
            R.id.itemMenuNotCompletedLessThanLevel3 ->{
                controller.getNotCompletedAndLessThanLevel3()
                setTitle(R.string.title_not_completed_less_than_lvl_3)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onGetTasksSuccess(tasks: List<Task?>) {
        adapter?.clear()
        adapter?.addAll(tasks)
    }

    override fun onGetTasksError() {
        Toast.makeText(this, R.string.message_task_error, Toast.LENGTH_SHORT).show()
    }
}
