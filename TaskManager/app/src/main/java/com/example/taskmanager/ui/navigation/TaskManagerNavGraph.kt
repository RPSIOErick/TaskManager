package com.example.taskmanager.ui.navigation

import EntranceDestination
import EntranceScreen
import TaskEditDestination
import TaskEditScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.taskmanager.ui.home.HomeDestination
import com.example.taskmanager.ui.home.HomeScreen
import com.example.taskmanager.ui.task.TaskDetailsDestination
import com.example.taskmanager.ui.task.TaskDetailsScreen
import com.example.taskmanager.ui.task.TaskEntryDestination
import com.example.taskmanager.ui.task.TaskEntryScreen

@Composable
fun TaskManagerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = EntranceDestination.route,
        modifier = modifier
    ) {
        composable(route = EntranceDestination.route) {
            EntranceScreen(
                navigateToHome = {navController.navigate(HomeDestination.route)}
            )
        }

        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToTaskEntry = {navController.navigate(TaskEntryDestination.route)},
                navigateToTaskDetails = {
                    navController.navigate("${TaskDetailsDestination.route}/${it}")
                }
            )
        }

        composable(route = TaskEntryDestination.route) {
            TaskEntryScreen(
                navigateBack = { navController.popBackStack() },
            )
        }

        composable(
            route = TaskDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(TaskDetailsDestination.taskIdArg) {
                type = NavType.IntType
            })
        ) {
            TaskDetailsScreen(
                navigateToEditTask = { navController.navigate("${TaskEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }

        composable(
            route = TaskEditDestination.routeWithArgs,
            arguments = listOf(navArgument(TaskEditDestination.taskIdArg) {
                type = NavType.IntType
            })
        ) {
            TaskEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}