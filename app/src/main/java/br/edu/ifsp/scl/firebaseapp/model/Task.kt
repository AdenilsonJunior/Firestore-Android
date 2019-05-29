package br.edu.ifsp.scl.firebaseapp.model

class Task {

    var id: String = ""
    var title: String = ""
    var level: Int = 0
    var completed: Boolean = false

    constructor(id: String,title: String, level: Int, completed: Boolean) {
        this.id = id
        this.title = title
        this.level = level
        this.completed = completed
    }

    constructor()
}