package com.example.todolist.views

import android.text.format.DateFormat
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.todolist.MainViewModel
import com.example.todolist.Routes
import com.example.todolist.data.Todo
import com.example.todolist.ui.theme.Green
import com.example.todolist.ui.theme.Red
import java.util.*

@Composable
fun TodoList(todos: List<Todo>,mainViewModel: MainViewModel, navigate: (route:String) ->Unit) {
    LazyColumn() {
        items(todos, key = { it.id }){
            TodoDetails(it,mainViewModel, navigate)
        }
    }
}

@Composable
fun TodoDetails(todo: Todo,mainViewModel: MainViewModel, navigate: (route:String) ->Unit) {
    Column() {
        var isChecked by remember { mutableStateOf(todo.isDone) }
        val surfaceColor by animateColorAsState(
            if (isChecked) Green else MaterialTheme.colors.surface,
        )
        Surface(modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .border(width = 1.dp, color = Color.Gray), color = surfaceColor) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Row(){
                    Checkbox(checked = isChecked,onCheckedChange = {
                        isChecked= !isChecked
                        mainViewModel.updateDoneTodo(isChecked,todo.id)
                    } )
                    Column() {
                        Text(
                            text = todo.title,
                            style = MaterialTheme.typography.body1,
                        )
                        Text(
                            text = if(todo.description.length>50) "${todo.description.take(50)}..."  else todo.description,
                            style = MaterialTheme.typography.caption,
                        )
                    }
                }
                Row(modifier = Modifier
                    .fillMaxHeight(), verticalAlignment = Alignment.CenterVertically){
                    Box(modifier = Modifier
                        .fillMaxHeight()
                    ){
                        Text(
                            text = DateFormat.format("MMM dd", Date(todo.date)).toString(),
                            textAlign = TextAlign.Center
                        )
                    }
                    Row(){
                        IconButton(onClick = { navigate("${Routes.Create.route}/${todo.id}") }){ Icon(Icons.Filled.Edit,"update") }
                        IconButton(onClick = { navigate("${Routes.Delete.route}/${todo.id}") }){ Icon(Icons.Filled.Delete,"delete",tint = Red) }
                    }
                }
            }
        }
    }
}