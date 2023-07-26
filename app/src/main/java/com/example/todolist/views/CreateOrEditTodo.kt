package com.example.todolist.views

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.TextFieldValue
import com.example.todolist.MainViewModel
import com.example.todolist.data.Todo
import com.example.todolist.ui.theme.Red
import com.example.todolist.ui.theme.TodoListTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CreateOrEditTodo(id: Int?, mainViewModel: MainViewModel, onBackPress: () -> Unit) {
    val title = remember { mutableStateOf(TextFieldValue()) }
    val description = remember { mutableStateOf(TextFieldValue()) }
    var error = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var selectedDateText by remember { mutableStateOf("Date") }
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
                    TopAppBar(title = { Text("Create") })
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
                        Button(
                            modifier = Modifier,
                            onClick = {
                                if(title.value.text.isNotBlank()&&description.value.text.isNotBlank()&&!selectedDateText.equals("Date")){
                                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                                    val date: Date = sdf.parse(selectedDateText)
                                    val todo= Todo(title.value.text, description.value.text,date.time)
                                    mainViewModel.insert(todo)
                                    onBackPress()
                                }
                                else error.value=true
                            }) {
                            Text(
                                text = "Submit",
                            )
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