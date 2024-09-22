package com.example.taskmanager.ui.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TasksRepository

class TaskEntryViewModel(private val tasksRepository: TasksRepository) : ViewModel() {

    var taskUiState by mutableStateOf(TaskUiState())
        private set

    fun updateUiState(taskDetails: TaskDetails){
        taskUiState =
            TaskUiState(taskDetails = taskDetails, isEntryValid = validateInput(taskDetails))
    }

    private fun validateInput(uiState: TaskDetails = taskUiState.taskDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && description.isNotBlank() && date.isNotBlank()
        }
    }

    suspend fun saveTask(){
        if(validateInput()){
            tasksRepository.insertTask(taskUiState.taskDetails.toTask())
        }
    }

}

data class TaskUiState(
    val taskDetails: TaskDetails = TaskDetails(),
    val isEntryValid: Boolean = false
)

data class TaskDetails(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val date: String = "",
    val done: Boolean = false
)

fun TaskDetails.toTask(): Task = Task(
    id = id,
    name = name,
    description = description,
    date = date,
    done = done
)

fun Task.toUiState(isEntryValid: Boolean = false): TaskUiState = TaskUiState(
    taskDetails = this.toTaskDetails(),
    isEntryValid = isEntryValid
)

fun Task.toTaskDetails(): TaskDetails = TaskDetails(
    id = id,
    name = name,
    description = description,
    date = date,
    done = done
)