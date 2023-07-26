package com.example.todolist.views

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asLiveData
import com.example.todolist.MainViewModel
import com.example.todolist.data.Todo
import com.example.todolist.ui.theme.Red
import com.example.todolist.ui.theme.TodoListTheme
import kotlinx.coroutines.flow.collect
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CreateOrEditTodo(id: Int?, mainViewModel: MainViewModel, onBackPress: () -> Unit) {
    var titleValue=""
    var descriptionValue=""
    var dateValue="Date"
    var scaffoldTitle="Create"

    id?.let { todoId ->
        val todo:Todo?=mainViewModel.todos.value?.find {
            it.id==todoId
        }
        todo?.let {
            titleValue=it.title
            descriptionValue=it.description
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            dateValue=sdf.format(Date(it.date))
            scaffoldTitle="Edit"
        }
    }

    val title = remember { mutableStateOf(TextFieldValue(titleValue)) }
    val description = remember { mutableStateOf(TextFieldValue(descriptionValue)) }
    val error = remember { mutableStateOf(false) }
    var selectedDateText by remember { mutableStateOf(dateValue) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            selectedDateText = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
        }, year, month, dayOfMonth
    )
    TodoListTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text(scaffoldTitle) })
                },
                content = {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextField(
                            value = title.value,
                            onValueChange = { title.value = it },
                            modifier = Modifier.semantics { contentDescription = "title" },
                            placeholder = { Text(text = "Title") },
                        )
                        TextField(
                            value = description.value,
                            onValueChange = { description.value = it },
                            modifier = Modifier.semantics { contentDescription = "description" },
                            placeholder = { Text(text = "description") },
                        )
                        Button(
                            modifier = Modifier,
                            onClick = {
                                datePicker.show()
                            }) {
                            Text(
                                text = selectedDateText,
                            )
                        }
                        Row(){
                            Button(
                                modifier = Modifier.padding(5.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
                                onClick = {
                                    if(title.value.text.isNotBlank()&&description.value.text.isNotBlank()&& selectedDateText != "Date"){
                                        val sdf = SimpleDateFormat("dd/MM/yyyy")
                                        try {
                                            val date: Date = sdf.parse(selectedDateText)
                                            val todo= Todo(title.value.text, description.value.text,date.time,false)
                                            if (id==0)mainViewModel.insert(todo) else id?.let {
                                                val todoTemp:Todo?=mainViewModel.todos.value?.find { todo->
                                                    todo.id==it
                                                }
                                                todoTemp?.isDone=false
                                                mainViewModel.updateTodo(todo,it)
                                            }
                                        }
                                        catch (e:ParseException){
                                            println("Wrong date format")
                                        }
                                        onBackPress()
                                    }
                                    else error.value=true
                                }) {
                                Text(
                                    text = "Submit",
                                    color = Color.White
                                )
                            }
                            Button(
                                modifier = Modifier.padding(5.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                                onClick = {
                                        onBackPress()
                                }) {
                                Text(
                                    text = "Cancel",
                                    color = Color.White
                                )
                            }
                        }
                        if(error.value){
                            Text(
                                text = "Please fill all the input fields",
                                color = Red
                            )
                        }
                    }
                }
            )
        }
    }
}