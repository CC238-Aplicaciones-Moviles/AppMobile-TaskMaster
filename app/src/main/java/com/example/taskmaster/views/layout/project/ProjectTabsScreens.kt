package com.example.taskmaster.views.layout.project

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun ProjectTasksScreen(nav: NavHostController, projectId: Long, title: String) {
    Scaffold(
        topBar = {
            Column {
                ProjectTopBar(title = title, onBack = { nav.popBackStack() })
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
        Box(Modifier.fillMaxSize().padding(inner), contentAlignment = Alignment.Center) {
            Text("Pantalla de Tareas")
        }
    }
}

@Composable
fun ProjectStatsScreen(nav: NavHostController, projectId: Long, title: String) {
    Scaffold(
        topBar = {
            Column {
                ProjectTopBar(title = title, onBack = { nav.popBackStack() })
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
        Box(Modifier.fillMaxSize().padding(inner), contentAlignment = Alignment.Center) {
            Text("Pantalla de Estadísticas")
        }
    }
}
