package com.example.todolist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.telecom.Call
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.todolist.data.Todo
import com.example.todolist.data.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Comment
import java.io.DataOutputStream
import java.net.InetAddress
import java.net.Socket

class MainViewModel  constructor(
    private val repository: TodoRepository
) : ViewModel() {
    var todos: LiveData<List<Todo>> = repository.getTodos().asLiveData()

    fun insert(todo: Todo) = viewModelScope.launch {
        repository.insert(todo)
    }
    fun updateTodo(todo:Todo,id: Int) = viewModelScope.launch {
        repository.update(todo.title,todo.description,todo.date,todo.isDone,id)
    }
    fun updateDoneTodo(isDone:Boolean,id: Int) = viewModelScope.launch {
        repository.updateDone(isDone,id)
    }
    fun deleteTodo(id: Int) = viewModelScope.launch {
        repository.delete(id)
    }
}