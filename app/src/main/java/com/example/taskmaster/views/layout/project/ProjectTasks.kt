package com.example.taskmaster.views.layout.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmaster.R
import com.example.taskmaster.views.layout.task.EmptyTask
import com.example.taskmaster.viewmodel.model.ProjectsViewModel
import com.example.taskmaster.viewmodel.model.TasksViewModel
import com.example.taskmaster.views.layout.task.TaskList
enum class PriorityFilter(val labels: List<String>) {
    HIGH(listOf("Alta", "High", "HIGH")),
    MEDIUM(listOf("Media", "Medium", "MEDIUM")),
    LOW(listOf("Baja", "Low", "LOW"))
}

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

    var query by remember { mutableStateOf("") }
    var filterMenu by remember { mutableStateOf(false) }
    var priorityFilter by remember { mutableStateOf<PriorityFilter?>(null) }

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
        Column(
            Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            // Barra superior: búsqueda + acciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var queryText by remember { mutableStateOf("") }

                TextField(
                    value = queryText,
                    onValueChange = { queryText = it; query = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 6.dp, bottom = 12.dp)
                        .heightIn(min = 40.dp),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    placeholder = {
                        Text(
                            "Buscar tareas",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = "Buscar",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor   = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        focusedTextColor   = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor        = MaterialTheme.colorScheme.primary,
                        focusedIndicatorColor   = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor  = Color.Transparent,
                        errorIndicatorColor     = Color.Transparent
                    )
                )

                Box {
                    IconButton(
                        onClick = { filterMenu = true },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_filter),
                            contentDescription = "Filtrar"
                        )
                    }
                    DropdownMenu(
                        expanded = filterMenu,
                        onDismissRequest = { filterMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Todas") },
                            onClick = {
                                priorityFilter = null
                                filterMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Alta") },
                            onClick = {
                                priorityFilter = PriorityFilter.HIGH
                                filterMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Media") },
                            onClick = {
                                priorityFilter = PriorityFilter.MEDIUM
                                filterMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Baja") },
                            onClick = {
                                priorityFilter = PriorityFilter.LOW
                                filterMenu = false
                            }
                        )
                    }

                }

                IconButton(
                    onClick = { nav.navigate("taskCreate/$projectId") },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = "Agregar"
                    )
                }
            }


            Spacer(Modifier.height(12.dp))


            Box(Modifier.fillMaxSize()) {
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
                        val filtered = tasks
                            .filter { t ->
                                val q = query
                                val title = t.title ?: ""
                                val desc  = t.description ?: ""
                                title.contains(q, true) || desc.contains(q, true)
                            }
                            .filter { t ->
                                priorityFilter?.let { pf ->
                                    val p = (t.priority?.toString() ?: "").trim().lowercase()

                                    when (pf) {
                                        PriorityFilter.HIGH   -> p in listOf("alta", "high", "high_priority", "highpriority")
                                        PriorityFilter.MEDIUM -> p in listOf("media", "medium", "medium_priority", "mediumpriority")
                                        PriorityFilter.LOW    -> p in listOf("baja", "low", "low_priority", "lowpriority")
                                    }
                                } ?: true
                            }

                        TaskList(
                            tasks = filtered,
                            onTaskClick = { }
                        )
                    }

                }
            }
        }
    }
}
