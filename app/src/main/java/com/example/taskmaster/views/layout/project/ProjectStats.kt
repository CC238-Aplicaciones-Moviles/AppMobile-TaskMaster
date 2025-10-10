package com.example.taskmaster.views.layout.project

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmaster.viewmodel.model.ProjectsViewModel

@Composable
fun ProjectStats(
    nav: NavHostController,
    projectId: Long,
    vm: ProjectsViewModel = remember { ProjectsViewModel() }
) {
    val project by vm.current.collectAsState()
    LaunchedEffect(projectId) { vm.loadById(projectId) }

    Scaffold(
        topBar = {
            Column {
                ProjectTopBar(
                    title = project?.name ?: "",
                    onBack = { nav.popBackStack() }
                )
                ProjectMiniTabs(
                    selected = ProjectSection.STATS,
                    onSelect = {
                        when (it) {
                            ProjectSection.TASKS    -> nav.navigate("projectTasks/$projectId") { launchSingleTop = true }
                            ProjectSection.STATS    -> Unit
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
                .padding(inner),
            contentAlignment = Alignment.Center
        ) {
            Text("Pantalla de Estadísticas (placeholder)")
        }
    }
}
