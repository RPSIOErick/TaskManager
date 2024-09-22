import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.taskmanager.ui.task.TaskEditViewModel
import com.example.taskmanager.ui.task.TaskEntryBody
import kotlinx.coroutines.launch

object TaskEditDestination : NavigationDestination {
    override val route = "task_edit"
    override val titleRes = R.string.task_edit_title
    const val taskIdArg = "taskId"
    val routeWithArgs = "$route/{$taskIdArg}"
}

@Composable
fun TaskEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: TaskEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val coroutineScope = rememberCoroutineScope()

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
                text = stringResource(TaskEditDestination.titleRes),
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        TaskEntryBody(
            taskUiState = viewModel.taskUiState,
            onTaskValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateTask()
                    navigateBack()
                }
            },
            modifier = Modifier
        )

    }


}