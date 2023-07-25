package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.asLiveData
import com.example.todolist.data.Todo
import com.example.todolist.data.TodoDatabase
import com.example.todolist.data.TodoRepository
import com.example.todolist.ui.theme.TodoListTheme
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database=TodoDatabase.getInstance(this)
        val repository=TodoRepository(database.todoDao())
        val todos=repository.getTodos().asLiveData()
        setContent {
            TodoListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val todosState=todos.observeAsState()
                    todosState.value?.let{
                        TodoList(it)
                    }
                }
            }
        }
    }
}

@Composable
fun TodoList(todos: List<Todo>) {
    LazyColumn() {
        items(todos){
            TodoDetails(it)
        }
    }
}

@Composable
fun TodoDetails(todo: Todo) {
    Column() {
        Text(
            text = todo.title,
        )
        Text(
            text = todo.description,
        )
        Text(
            text = Date(todo.date).toString(),
        )
    }
}
