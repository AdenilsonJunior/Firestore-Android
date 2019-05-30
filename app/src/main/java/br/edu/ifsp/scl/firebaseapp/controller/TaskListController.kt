package br.edu.ifsp.scl.firebaseapp.controller

import br.edu.ifsp.scl.firebaseapp.model.Task
import br.edu.ifsp.scl.firebaseapp.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot

class TaskListController(
    var view: TaskListContract?,
    val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    var getAllListener: ListenerRegistration? = null
    var getAllCompletedListener: ListenerRegistration? = null
    var getAllCompletedAndLevel3Listener: ListenerRegistration? = null
    var getNotCompletedAndLessThanLevel3Listener: ListenerRegistration? = null

    fun getTasks() {
        getAllCompletedListener?.remove()
        getAllCompletedAndLevel3Listener?.remove()
        getNotCompletedAndLessThanLevel3Listener?.remove()
        getAllListener = firebaseFirestore.collection(Constants.TASKS)
            .addSnapshotListener { querySnapShot, exception ->
                convertQuerySnapShotToTask(querySnapShot)
                if (exception != null) {
                    view?.onGetTasksError()
                }
            }
    }

    private fun convertQuerySnapShotToTask(querySnapshot: QuerySnapshot?) {
        val tasks = querySnapshot?.documents?.map {
            it.toObject(Task::class.java)
        } ?: listOf()
        view?.onGetTasksSuccess(tasks)
    }

    fun handleComplete(task: Task) {
        firebaseFirestore.runTransaction {
            val taskRef = firebaseFirestore.collection(Constants.TASKS).document(task.id)
            val completedFromServer = it.get(taskRef).getBoolean(Constants.TASK_COMPLETED) ?: false
            it.update(taskRef, Constants.TASK_COMPLETED, !completedFromServer)
        }
    }

    fun deleteTask(task: Task) {
        firebaseFirestore.collection(Constants.TASKS)
            .document(task.id)
            .delete()
    }

    fun getNotCompletedAndLessThanLevel3() {
        getAllListener?.remove()
        getAllCompletedListener?.remove()
        getAllCompletedAndLevel3Listener?.remove()
        getNotCompletedAndLessThanLevel3Listener = firebaseFirestore.collection(Constants.TASKS)
            .whereEqualTo(Constants.TASK_COMPLETED, false)
            .whereLessThan(Constants.TASK_LEVEL, 3)
            .addSnapshotListener { querySnapshot, exception ->
                convertQuerySnapShotToTask(querySnapshot)
                if (exception != null) {
                    view?.onGetTasksError()
                }
            }
    }

    fun getAllCompletedAndLevel3() {
        getAllListener?.remove()
        getNotCompletedAndLessThanLevel3Listener?.remove()
        getAllCompletedListener?.remove()
        getAllCompletedAndLevel3Listener = firebaseFirestore.collection(Constants.TASKS)
            .whereEqualTo(Constants.TASK_COMPLETED, true)
            .whereEqualTo(Constants.TASK_LEVEL, 3)
            .addSnapshotListener { querySnapshot, exception ->
                convertQuerySnapShotToTask(querySnapshot)
                if (exception != null) {
                    view?.onGetTasksError()
                }
            }
    }

    fun getAllCompleted() {
        getAllListener?.remove()
        getNotCompletedAndLessThanLevel3Listener?.remove()
        getAllCompletedAndLevel3Listener?.remove()
        getAllCompletedListener = firebaseFirestore.collection(Constants.TASKS)
            .whereEqualTo(Constants.TASK_COMPLETED, true)
            .addSnapshotListener { querySnapshot, exception ->
                convertQuerySnapShotToTask(querySnapshot)
                if (exception != null) {
                    view?.onGetTasksError()
                }
            }
    }
}

interface TaskListContract {
    fun onGetTasksSuccess(tasks: List<Task?>)
    fun onGetTasksError()
}