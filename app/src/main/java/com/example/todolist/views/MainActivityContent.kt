package com.example.todolist.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.todolist.MainViewModel
import com.example.todolist.Routes
import com.example.todolist.ui.theme.TodoListTheme


@Composable
fun MainActivityContent(mainViewModel: MainViewModel, navigate: (route:String) ->Unit) {
    TodoListTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text("To do List") })
                },
                drawerContent = {/**/ },
                bottomBar = {/**/ },
                floatingActionButton = {
                    FloatingActionButton(onClick = { navigate("${Routes.Create.route}/0") }) {
                        Icon(Icons.Filled.Add,"")
                    }
                },
                snackbarHost = {/**/ },
                content = {
                    mainViewModel.todos.observeAsState().value?.let {
                        TodoList(it,mainViewModel,navigate)
                    }
                }
            )
        }
    }
}
