package com.example.taskmanager.ui.task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.TasksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskDetailsViewModel (
    savedStateHandle: SavedStateHandle,
    private val tasksRepository: TasksRepository,
) : ViewModel() {

    private val taskId: Int = checkNotNull(savedStateHandle[TaskDetailsDestination.taskIdArg])

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val uiState: StateFlow<TaskDetailsUiState> =
        tasksRepository.getTaskStream(taskId)
            .filterNotNull()
            .map {
                TaskDetailsUiState(alreadyDone = it.done, taskDetails = it.toTaskDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = TaskDetailsUiState()
            )

    fun checkTask(){
        viewModelScope.launch {
            val currentTask = uiState.value.taskDetails.toTask()
            if(!currentTask.done) {
                tasksRepository.updateTask(currentTask.copy(done = true))
            }
        }
    }

    suspend fun deleteTask(){
        tasksRepository.deleteTask(uiState.value.taskDetails.toTask())
    }
}

data class TaskDetailsUiState(
    val alreadyDone: Boolean = false,
    val taskDetails: TaskDetails = TaskDetails()
)