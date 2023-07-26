package com.example.todolist.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.todolist.data.TodoDatabaseWorker.Companion.KEY_FILENAME
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "todos")
data class Todo constructor(val title: String, val description: String, val date: Long,
                            var isDone: Boolean,
                            @PrimaryKey(autoGenerate = true) val id: Int = 0)

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos")
    fun getTodos(): Flow<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(todos: List<Todo>)

    @Query("UPDATE todos SET title=:title,description=:description,date=:date,isDone=:isDone WHERE id = :id")
    suspend fun update(title: String, description: String, date: Long,isDone: Boolean,id: Int)

    @Query("UPDATE todos SET isDone=:isDone WHERE id = :id")
    suspend fun updateDone(isDone: Boolean,id: Int)

    @Query("DELETE FROM todos WHERE id = :id")
    suspend fun deleteTodo(id: Int)
}

@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    companion object {

        // For Singleton instantiation
        @Volatile private var instance: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): TodoDatabase {
            return Room.databaseBuilder(context, TodoDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<TodoDatabaseWorker>()
                                .setInputData(workDataOf(KEY_FILENAME to TODO_DATA_FILENAME))
                                .build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )
                .build()
        }
    }
}