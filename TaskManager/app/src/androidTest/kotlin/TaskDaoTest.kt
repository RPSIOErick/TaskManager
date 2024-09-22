import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskDao
import com.example.taskmanager.data.TaskManagerDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {

    private lateinit var taskDao: TaskDao
    private lateinit var taskManagerDatabase: TaskManagerDatabase

    @Before
    fun createDb(){
        val context: Context = ApplicationProvider.getApplicationContext()

        taskManagerDatabase = Room.inMemoryDatabaseBuilder(context, TaskManagerDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        taskDao = taskManagerDatabase.taskDao()

    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        taskManagerDatabase.close()
    }

    private var task1 = Task(1, "Criar projeto", "Terminar projeto de Kotlin", "23 de setembro", false)
    private var task2 = Task(2, "Teste", "Descrição", "23 de setembro", false)

    private suspend fun addOneTaskToDb(){
        taskDao.insert(task1)
    }

    private suspend fun addTwoTasksToDb(){
        taskDao.insert(task1)
        taskDao.insert(task2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsertInDB() = runBlocking {
        addOneTaskToDb()

        val allTasks = taskDao.getAllTasks().first()
        assertEquals(allTasks[0], task1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllTasksFromDB() = runBlocking {
        addTwoTasksToDb()

        val allTasks = taskDao.getAllTasks().first()
        assertEquals(allTasks[0], task1)
        assertEquals(allTasks[1], task2)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateTaskInDB() = runBlocking {
        addTwoTasksToDb()

        taskDao.update(Task(1, "Entregar projeto", "Só falta enviar", "23 de setembro", true))
        taskDao.update(Task(2, "Teste2", "Teste2", "4 de setembro", false))

        val allTasks = taskDao.getAllTasks().first()
        assertEquals(allTasks[0], Task(1, "Entregar projeto", "Só falta enviar", "23 de setembro", true))
        assertEquals(allTasks[1], Task(2, "Teste2", "Teste2", "4 de setembro", false))
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteTaskFromSB() = runBlocking {
        addTwoTasksToDb()

        taskDao.delete(task1)
        taskDao.delete(task2)

        val allTasks = taskDao.getAllTasks().first()
        assertTrue(allTasks.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoGeTaskFromDB() = runBlocking {

        addOneTaskToDb()

        val task = taskDao.getTask(1)

        assertEquals(task.first(), task1)

    }

}