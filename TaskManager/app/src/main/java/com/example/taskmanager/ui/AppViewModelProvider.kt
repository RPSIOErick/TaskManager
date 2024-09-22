package com.example.taskmanager.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.taskmanager.TaskManagerApplication
import com.example.taskmanager.ui.home.HomeViewModel
import com.example.taskmanager.ui.task.TaskDetailsViewModel
import com.example.taskmanager.ui.task.TaskEditViewModel
import com.example.taskmanager.ui.task.TaskEntryViewModel

object AppViewModelProvider {

    val Factory = viewModelFactory {
        initializer {
            TaskEntryViewModel(taskManagerApplication().container.tasksRepository)
        }

        initializer {
            HomeViewModel(taskManagerApplication().container.tasksRepository)
        }

        initializer {
            TaskDetailsViewModel(
                this.createSavedStateHandle(),
                taskManagerApplication().container.tasksRepository
            )

        }

        initializer {
            TaskEditViewModel(
                this.createSavedStateHandle(),
                taskManagerApplication().container.tasksRepository
            )
        }
    }

}

fun CreationExtras.taskManagerApplication(): TaskManagerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TaskManagerApplication)