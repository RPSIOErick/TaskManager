package com.example.taskmanager.ui.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanager.R
import com.example.taskmanager.data.Task
import com.example.taskmanager.ui.AppViewModelProvider
import com.example.taskmanager.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object TaskDetailsDestination : NavigationDestination {
    override val route = "item_details"
    override val titleRes = R.string.task_details_title
    const val taskIdArg = "taskId"
    val routeWithArgs = "$route/{$taskIdArg}"
}


@Composable
fun TaskDetailsScreen(
    navigateToEditTask: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,

    viewModel: TaskDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)

){

    val bgColor = Color(0xFFF6F6F6)
    val fabColor = Color(0xFFF4AD3A)

    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Box(
        Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(bgColor),
        ) {
            Spacer(modifier = Modifier.height(70.dp))

            Row(
                Modifier
                    .fillMaxWidth(),
                Arrangement.Center
            ) {
                Text(
                    text = "Task Manager",
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            TaskDetailsBody(
                taskDetailsUiState = uiState.value,
                onCheckTask = { viewModel.checkTask() },
                onDelete = {
                    coroutineScope.launch {
                        viewModel.deleteTask()
                        navigateBack()
                    }
                }
            )

        }

        FloatingActionButton(
            onClick = { navigateToEditTask(uiState.value.taskDetails.id) },
            containerColor = fabColor,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .padding(16.dp)
                .size(70.dp)
                .align(Alignment.BottomEnd)
                .offset(y = (-70.dp), x = (-30).dp)
        ) {
            Icon(
                Icons.Default.Edit,
                contentDescription = "Adicionar tarefa",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
    }

}

@Composable
fun TaskDetailsBody(
    taskDetailsUiState: TaskDetailsUiState,
    onCheckTask: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        val yellowButtonColor = Color(0xFFF4AD3A)
        val disabledYellowButtonColor = Color(0xFFE09479)

        TaskDetails(
            task = taskDetailsUiState.taskDetails.toTask(),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp)),
        )

        if (!taskDetailsUiState.alreadyDone)
        {
            Button(
                onClick = onCheckTask,
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = yellowButtonColor,
                    contentColor = Color.White,
                ),
                enabled = true
            ) {

                Text(text = "CONCLUIR", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            }
        }
        else
        {
            Button(
                onClick = onCheckTask,
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = yellowButtonColor,
                    contentColor = Color.White,
                    disabledContainerColor = disabledYellowButtonColor,
                ),
                enabled = false
            ) {

                Text(text = "CONCLUÍDA", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)

            }
        }


        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            enabled = true
        ) {
            Text(text = "DELETAR", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }

        if (deleteConfirmationRequired){
            DeleteConfirmDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(16.dp)
            )
        }

    }
}

@Composable
fun TaskDetails(
    task: Task,
    modifier: Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF346DE9),
                        Color(0xFF1D3D83)
                    )
                )
            )
        ,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
    ){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),

        ){

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                Arrangement.Center
            ) {
                Text(
                    text = task.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            ) {
                Text(
                    text = task.description,
                    color = Color.White,
                    fontSize = 20.sp,
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            ) {
                Text(
                    text = "Prazo: ",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                Text(
                    text = task.date,
                    color = Color.White,
                    fontSize = 20.sp,
                )
            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            ) {
                Text(
                    text = "Status: ",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                if (!task.done){
                    Text(
                        text = "Tarefa não feita!",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                    )
                }
                else {
                    Text(
                        text = "Tarefa feita!",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                    )
                }
            }
        }
    }
}
@Composable
private fun DeleteConfirmDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(stringResource(R.string.yes))
            }
        })
}

@Preview
@Composable
fun TaskDetailScreenPrewview() {
    TaskDetailsBody(
        TaskDetailsUiState(
            taskDetails = TaskDetails(1, "Task Manager", "Construir uma aplicação em JetPack Compose que gerencie tarefas!", "3 de setembro", false),
            alreadyDone = true,
        ),
        onCheckTask = {},
        onDelete = {}
    )
}