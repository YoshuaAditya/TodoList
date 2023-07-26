package com.example.todolist

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asLiveData
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todolist.data.Todo
import com.example.todolist.data.TodoDatabase
import com.example.todolist.data.TodoRepository
import com.example.todolist.ui.theme.Green
import com.example.todolist.ui.theme.Red
import com.example.todolist.ui.theme.TodoListTheme
import com.example.todolist.views.CreateOrEditTodo
import com.example.todolist.views.MainActivityContent
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database=TodoDatabase.getInstance(this)
        val repository=TodoRepository(database.todoDao())
        val mainViewModel= MainViewModel(repository)
        setContent {
            NavigationHost(mainViewModel) { onBackPressed() }
        }
    }
}

@Composable
fun NavigationHost(mainViewModel: MainViewModel, onBackPress: () -> Unit) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Main.route) {
        composable(Routes.Main.route) {
            MainActivityContent(mainViewModel){navController.navigate(
                it,
                NavOptions.Builder().setLaunchSingleTop(true).build())
            }
        }
        composable("${Routes.Create.route}/{id}",arguments = listOf(navArgument("id") { defaultValue = "0" })
        ) {
            it.arguments?.getString("id")?.let { id ->
                CreateOrEditTodo(Integer.valueOf(id),mainViewModel,onBackPress)
            }
        }

        composable("${Routes.Delete.route}/{id}",arguments = listOf(navArgument("id") { defaultValue = "0" })
        ){
            it.arguments?.getString("id")?.let { id ->
                mainViewModel.deleteTodo(Integer.valueOf(id))
            }
            LaunchedEffect(Unit){
                navController.navigate(Routes.Main.route){popUpTo(navController.graph.findStartDestination().id)}
                onBackPress()
            }
        }
    }
}
