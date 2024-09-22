package com.example.taskmanager.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmanager.R
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanager.data.Task
import com.example.taskmanager.ui.AppViewModelProvider
import com.example.taskmanager.ui.navigation.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToTaskEntry: () -> Unit,
    navigateToTaskDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,

    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val bgColor = Color(0xFFF6F6F6)
    val fabColor = Color(0xFFF4AD3A)

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val homeUiState by viewModel.homeUiState.collectAsState()


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

            HomeBody(
                taskList = homeUiState.taskList,
                onTaskClick = navigateToTaskDetails,
                modifier = modifier.fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            )

        }


        FloatingActionButton(
            onClick = navigateToTaskEntry,
            containerColor = fabColor,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .padding(16.dp)
                .size(70.dp)
                .align(Alignment.BottomEnd)
                .offset(y = (-70.dp), x = (-30).dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Adicionar tarefa",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}


@Composable
private fun HomeBody(
    taskList: List<Task>,
    onTaskClick: (Int) -> Unit,
    modifier: Modifier
){
    if(taskList.isEmpty()) {

        Spacer(modifier = Modifier.height(100.dp))

        Row(
            Modifier
                .fillMaxWidth(),
            Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.sitting_question),
                contentDescription = "Sitting man questioning",
                modifier = Modifier.size(280.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            Arrangement.Center,
        ) {
            Text(
                text = """
                    Você ainda não tem tarefas criadas.
                    Clique no "+" para uma nova tarefa!
                """.trimIndent(),
                fontSize = 20.sp,
                color = Color.Black,
            )
        }
    }
    else {
        TaskList(
            taskList = taskList,
            onTaskClick = { onTaskClick(it.id) },
            modifier = Modifier
        )

    }
}

@Composable
private fun TaskList(
    taskList: List<Task>,
    onTaskClick: (Task) -> Unit,
    modifier: Modifier
) {
    LazyColumn (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(items = taskList, key = { it.id }) { task ->
            TaskItem(task = task,
                onTaskClick = { onTaskClick(task) },
                modifier = Modifier
            )
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }

    }
}

@Composable
private fun TaskItem(
    task: Task,
    onTaskClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {

    val undoneColor = Color(0xFFF95757)
    val doneColor = Color(0xFF68C52E)

    Card (
        modifier = Modifier
            .padding(20.dp)
            .clickable {
                onTaskClick(task)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column (
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF346DE9),
                            Color(0xFF1D3D83)
                        )
                    )
                )
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, start = 10.dp, end = 10.dp)
            ) {
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth().padding(5.dp).weight(1f)
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = task.date,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth().padding(5.dp).weight(1f)
                )
            }

            if (!task.done) {
                Column(
                    modifier = Modifier
                        .padding(start = 50.dp, end = 50.dp, bottom = 20.dp, top = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            undoneColor
                        ),
                    ) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {

                        Text(
                            text = "Não feita!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(5.dp)
                                .weight(1f)

                        )

                    }

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Ícone de Não feita",
                            modifier = Modifier
                                .size(35.dp)
                                .weight(1f),
                            tint = Color.Black
                        )
                    }
                }

            } else {

                Column(
                    modifier = Modifier
                        .padding(start = 50.dp, end = 50.dp, bottom = 20.dp, top = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            doneColor
                        ),
                ) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {

                        Text(
                            text = "Feita!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(5.dp)
                                .weight(1f)

                        )

                    }

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Ícone de Não feita",
                            modifier = Modifier
                                .size(35.dp)
                                .weight(1f),
                            tint = Color.Black
                        )
                    }
                }

            }
        }

    }
}

@Preview
@Composable
fun HomeBodyPreview() {
    HomeBody(
        taskList = listOf(
            Task(1, "Tarefa foda", "Descrição foda", "3 de setembro", false),
            Task(2, "Outra tarefa", "Descrição da outra tarefa", "4 de setembro", true)
        ),
        onTaskClick = { /* Ação ao clicar na tarefa */ },
        modifier = Modifier.fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
fun HomeBodyEmptyListPreview() {
    HomeBody(listOf(), onTaskClick = {}, modifier = Modifier)
}

@Preview
@Composable
fun TaskItemPreview() {
    TaskItem(
        Task(1, "Teste", "É uma das tarefas", "27 de setembro", true), onTaskClick = {}
    )
}