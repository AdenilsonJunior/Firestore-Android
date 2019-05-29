package br.edu.ifsp.scl.firebaseapp.controller

import br.edu.ifsp.scl.firebaseapp.model.Task
import br.edu.ifsp.scl.firebaseapp.util.Constants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class AddTaskController(
    val view: AddTaskContract?,
    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {


    fun addTask(title: String, level: Int) {
        val document = firestore.collection(Constants.TASKS).document()
        document.set(Task(document.id, title, level, false))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    view?.onAddTaskSuccess()
                } else {
                    view?.onAddTaskError()
                }
            }
    }
}

interface AddTaskContract {
    fun onAddTaskSuccess()
    fun onAddTaskError()
}