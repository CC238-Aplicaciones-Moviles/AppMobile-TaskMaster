package com.example.taskmaster.views.layout.project

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmaster.views.layout.task.EmptyTask
import com.example.taskmaster.viewmodel.model.ProjectsViewModel
import com.example.taskmaster.viewmodel.model.TasksViewModel
import com.example.taskmaster.views.layout.task.TaskList

@Composable
fun ProjectTasks(
    nav: NavHostController,
    projectId: Long,
    pvm: ProjectsViewModel = remember { ProjectsViewModel() },
    tvm: TasksViewModel = remember { TasksViewModel() }
) {
    val project by pvm.current.collectAsState()
    val isLoading by tvm.isLoading.collectAsState()
    val error by tvm.error.collectAsState()
    val tasks by tvm.tasks.collectAsState()

    LaunchedEffect(projectId) {
        pvm.loadById(projectId)
        tvm.loadByProject(projectId)
    }

    Scaffold(
        topBar = {
            Column {
                ProjectTopBar(
                    title = project?.name ?: "",
                    onBack = { nav.popBackStack() }
                )
                ProjectMiniTabs(
                    selected = ProjectSection.TASKS,
                    onSelect = {
                        when (it) {
                            ProjectSection.TASKS    -> Unit
                            ProjectSection.STATS    -> nav.navigate("projectStats/$projectId") { launchSingleTop = true }
                            ProjectSection.SETTINGS -> nav.navigate("projectSettings/$projectId") { launchSingleTop = true }
                        }
                    }
                )
            }
        }
    ) { inner ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                !error.isNullOrBlank() -> {
                    Text(
                        text = error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                tasks.isEmpty() -> {
                    EmptyTask()
                }
                else -> {
                    TaskList(tasks = tasks) {  }
                }
            }
        }
    }
}
