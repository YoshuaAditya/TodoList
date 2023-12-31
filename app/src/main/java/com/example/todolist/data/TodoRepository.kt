package com.example.todolist.data

import androidx.annotation.WorkerThread

class TodoRepository constructor(private val todoDao: TodoDao) {

    fun getTodos() = todoDao.getTodos()

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: TodoRepository? = null

        fun getInstance(todoDao: TodoDao) =
            instance ?: synchronized(this) {
                instance ?: TodoRepository(todoDao).also { instance = it }
            }
    }
    @WorkerThread
    suspend fun insert(todo: Todo) {
        todoDao.insertTodo(todo)
    }
    @WorkerThread
    suspend fun delete(id: Int) {
        todoDao.deleteTodo(id)
    }
    @WorkerThread
    suspend fun update(title: String, description: String, date: Long,isDone: Boolean,id: Int) {
        todoDao.update(title, description,date,isDone,id)
    }
    @WorkerThread
    suspend fun updateDone(isDone:Boolean,id: Int) {
        todoDao.updateDone(isDone,id)
    }
}
