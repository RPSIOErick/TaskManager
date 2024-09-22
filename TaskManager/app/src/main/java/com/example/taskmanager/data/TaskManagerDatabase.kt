package com.example.taskmanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 2, exportSchema = false)
abstract class TaskManagerDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {

        @Volatile
        private var Instance: TaskManagerDatabase? = null

        fun getDatabase(context: Context): TaskManagerDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, TaskManagerDatabase::class.java, "task_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }

    }

}