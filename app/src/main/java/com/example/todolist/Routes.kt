package com.example.todolist

sealed class Routes (val route: String) {
    object Main : Routes("main")
    object Create : Routes("create")
    object Delete : Routes("delete")
    object Update : Routes("update")
}