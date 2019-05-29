package br.edu.ifsp.scl.firebaseapp.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import br.edu.ifsp.scl.firebaseapp.R
import br.edu.ifsp.scl.firebaseapp.controller.AddTaskContract
import br.edu.ifsp.scl.firebaseapp.controller.AddTaskController
import kotlinx.android.synthetic.main.activity_add_task.*

class AddTaskActivity : AppCompatActivity(), AddTaskContract {

    private val controller = AddTaskController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.itemMenuSave -> {
                controller.addTask(
                    editTextTitle.text.toString(),
                    when (radioGroupLevel.checkedRadioButtonId) {
                        R.id.radioButton1 -> 1
                        R.id.radioButton2 -> 2
                        R.id.radioButton3 -> 3
                        else -> 1
                    }
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAddTaskSuccess() {
        Toast.makeText(this, getString(R.string.message_task_added), Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onAddTaskError() {
        Toast.makeText(this, getString(R.string.message_task_error), Toast.LENGTH_SHORT).show()
    }
}