package com.example.taskmanager.ui.task

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanager.R
import com.example.taskmanager.ui.AppViewModelProvider
import com.example.taskmanager.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch


object TaskEntryDestination : NavigationDestination {
    override val route = "task_entry"
    override val titleRes = R.string.task_entry_title
}

@Composable
fun TaskEntryScreen(
    navigateBack: () -> Unit,
    canNavigateBack: Boolean = true,

    viewModel: TaskEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
)
{

    val bgColor = Color(0xFFF6F6F6)

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(bgColor),
    ) {
        Spacer(Modifier.height(10.dp))

        Row (
            Modifier
                .fillMaxWidth()
                .absoluteOffset(x = 10.dp, y = 50.dp),
            Arrangement.Start
        ) {
            if (canNavigateBack) {
                IconButton(
                    onClick = {
                        navigateBack()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back_arrow),
                        contentDescription = "Voltar",
                        modifier = Modifier.size(35.dp),
                        tint = Color.Black
                    )
                }
            }
        }

        Row (
            Modifier
                .fillMaxWidth()
                .offset(y = 10.dp),
            Arrangement.Center
        ) {
            Text(
                text = stringResource(TaskEntryDestination.titleRes),
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        val coroutineScope = rememberCoroutineScope()

        TaskEntryBody(
            taskUiState = viewModel.taskUiState,
            onTaskValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveTask()
                    navigateBack()
                }
            },
            modifier = Modifier
        )
    }
}

@Composable
fun TaskEntryBody(
    taskUiState: TaskUiState,
    onTaskValueChange: (TaskDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val yellowButtonColor = Color(0xFFF4AD3A)
    val disabledYellowButton = Color(0xFFFFE7BF)
    val focusManager = LocalFocusManager.current

    val bgColor = Color(0xFFF6F6F6)

    Column (
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 20.dp)
            .background(bgColor)
            .clickable { focusManager.clearFocus() },
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TaskInputForm(
            taskDetails = taskUiState.taskDetails,
            onValueChange = onTaskValueChange,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onSaveClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = yellowButtonColor,
                disabledContainerColor = disabledYellowButton
            ),
            shape = RoundedCornerShape(50),
            modifier = Modifier.fillMaxWidth(0.5f),
            enabled = taskUiState.isEntryValid
        ) {
            Text(
                text = "SALVAR",
                fontSize = 25.sp,
                color = Color.White
            )
        }

        Row (
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.sitting_idea),
                contentDescription = "Sitting man with an idea",
                modifier = Modifier
                    .size(280.dp)
                    .align(Alignment.Bottom)
                    .offset(y = (-60).dp)
            )
        }
    }
}

@Composable
fun TaskInputForm(
    taskDetails: TaskDetails,
    modifier: Modifier = Modifier,
    onValueChange: (TaskDetails) -> Unit = {},
    enabled: Boolean = true
) {

    val filledBorderColor = Color(0xFF079D77)
    val bgColor = Color(0xFFF6F6F6)

    Row (
        Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
    ) {
        OutlinedTextField(
            value = taskDetails.name,
            onValueChange = { onValueChange(taskDetails.copy(name = it)) },
            label = {
                Text(text = "Nome da Tarefa", fontSize = 15.sp, color = Color.Black)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedBorderColor = if (taskDetails.name.isNotEmpty()) {
                    filledBorderColor
                } else {
                    Color.Gray
                }
            ),
            modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 15.dp),
            shape = RoundedCornerShape(15),
            enabled = enabled,
            singleLine = true,
        )
    }

    Row (
        Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
    ) {
        OutlinedTextField(
            value = taskDetails.description,
            onValueChange = { onValueChange(taskDetails.copy(description = it)) },
            label = {
                Text(text = "Descrição da Tarefa", fontSize = 15.sp, color = Color.Black)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedBorderColor = if (taskDetails.description.isNotEmpty()) {
                    filledBorderColor
                } else {
                    Color.Gray
                }
            ),
            modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 15.dp),
            shape = RoundedCornerShape(15),
            enabled = enabled,
            singleLine = true
        )
    }

    Row  (
        Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
    ){
        OutlinedTextField(
            value = taskDetails.date,
            onValueChange = { onValueChange(taskDetails.copy(date = it)) },
            label = {
                Text(text = "Prazo", fontSize = 15.sp, color = Color.Black)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedBorderColor = if (taskDetails.date.isNotEmpty()) {
                    filledBorderColor
                } else {
                    Color.Gray
                }
            ),
            modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 15.dp),
            shape = RoundedCornerShape(15),
            enabled = enabled,
            singleLine = true
        )
    }

    Spacer(modifier = Modifier.height(20.dp))
}