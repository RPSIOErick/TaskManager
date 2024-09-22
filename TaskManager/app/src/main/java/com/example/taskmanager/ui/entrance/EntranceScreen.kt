import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmanager.R
import com.example.taskmanager.ui.navigation.NavigationDestination

object EntranceDestination : NavigationDestination {
    override val route = "entrance"
    override val titleRes = R.string.app_name
}

@Composable
fun EntranceScreen(
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier
){

    val homeRowColor = Color(0xFFF6F6F6)
    val yellowButtonColor = Color(0xFFF4AD3A)

    Column (
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF346DE9),
                        Color(0xFF1D3D83)
                    ),
                    startY = 0f

                )
            )
    ) {

        Spacer(modifier = Modifier.height(70.dp))

        Row (
            Modifier
                .fillMaxWidth(),
            Arrangement.Center
        ) {
            Text(
                text = "Task Manager",
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row (
            Modifier
                .fillMaxWidth()
                .padding(20.dp),
            Arrangement.Center
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Marque suas ")
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append("tarefas")
                    pop()
                    append(" e esteja sempre ")
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append("organizado")
                    pop()
                    append("!")
                },
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                color = Color.White
            )
        }

        Row (
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.clock_man),
                contentDescription = "Clock Man",
                modifier = Modifier.size(390.dp).offset(y = 40.dp)
            )
        }

        Row (
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(homeRowColor, shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Text(
                    text = "Organize seu dia, conclua suas tarefas facilmente e alcance suas metas!",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.offset(y = (-40).dp)
                )

                Button(
                    onClick = navigateToHome,
                    colors = ButtonDefaults.buttonColors(containerColor = yellowButtonColor),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text(
                        text = "INICIAR",
                        fontSize = 25.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TaskManagerAppPreview(){
    EntranceScreen(navigateToHome = {})
}