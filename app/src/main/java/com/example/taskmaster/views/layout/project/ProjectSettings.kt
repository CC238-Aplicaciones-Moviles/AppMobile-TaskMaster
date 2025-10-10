// views/layout/project/ProjectSettings.kt
package com.example.taskmaster.views.layout.project

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.taskmaster.viewmodel.model.ProjectsViewModel
import java.util.Calendar

@Composable
fun ProjectSettings(
    nav: NavHostController,
    projectId: Long,
    vm: ProjectsViewModel = remember { ProjectsViewModel() }
) {
    val isLoading by vm.isLoading.collectAsState()
    val error by vm.error.collectAsState()
    val project by vm.current.collectAsState()

    LaunchedEffect(projectId) { vm.loadById(projectId) }

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var budgetText by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("PLANNED") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    LaunchedEffect(project) {
        project?.let {
            name        = it.name
            description = it.description
            budgetText  = it.budget.toString()
            imageUrl    = it.imageUrl.orEmpty()
            status      = it.status
            startDate   = it.startDate.take(10)
            endDate     = it.endDate.take(10)
        }
    }

    Scaffold(
        topBar = {
            Column {
                ProjectTopBar(
                    title = project?.name ?: "",
                    onBack = { nav.popBackStack() }
                )
                ProjectMiniTabs(
                    selected = ProjectSection.SETTINGS,
                    onSelect = {
                        when (it) {
                            ProjectSection.TASKS    -> nav.navigate("projectTasks/$projectId") { launchSingleTop = true }
                            ProjectSection.STATS    -> nav.navigate("projectStats/$projectId") { launchSingleTop = true }
                            ProjectSection.SETTINGS -> Unit
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                AsyncImage(
                    model = imageUrl.ifBlank { project?.imageUrl },
                    contentDescription = null,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.height(4.dp))
                Text("#${project?.key.orEmpty()}", style = MaterialTheme.typography.labelLarge)
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    Label("Nombre del proyecto")
                    TextField(
                        value = name, onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true, shape = RoundedCornerShape(8.dp),
                        colors = fieldColors()
                    )
                    Divider(color = MaterialTheme.colorScheme.outline)

                    Label("Descripción")
                    TextField(
                        value = description, onValueChange = { description = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = fieldColors()
                    )
                    Divider(color = MaterialTheme.colorScheme.outline)

                    Label("Presupuesto")
                    TextField(
                        value = budgetText,
                        onValueChange = { if (it.matches(Regex("""\d*\.?\d*"""))) budgetText = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        prefix = { Text("S/ ") },
                        colors = fieldColors()
                    )
                    Divider(color = MaterialTheme.colorScheme.outline)

                    Label("Estado")
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        TextField(
                            value = status,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expanded = true },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = fieldColors()
                        )
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            listOf("PLANNED", "IN_PROGRESS", "COMPLETED").forEach { s ->
                                DropdownMenuItem(
                                    onClick = { status = s; expanded = false },
                                    text = { Text(s) }
                                )
                            }
                        }
                    }
                    Divider(color = MaterialTheme.colorScheme.outline)

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.weight(1f)) {
                            Label("Fecha inicio")
                            DateField(value = startDate, onPick = { startDate = it }, enabled = false)
                        }
                        Column(Modifier.weight(1f)) {
                            Label("Fecha fin")
                            DateField(value = endDate, onPick = { endDate = it })
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { /* miembros */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) { Text("Lista de miembros") }

                    Button(
                        onClick = {
                            val budgetDouble = budgetText.toDoubleOrNull() ?: project?.budget ?: 0.0
                            vm.update(
                                id = projectId,
                                name = name,
                                description = description,
                                imageUrl = imageUrl,
                                budget = budgetDouble,
                                endDate = endDate,
                                status = status
                            )
                        },
                        enabled = !isLoading
                    ) { Text("Guardar Cambios") }
                }
            }

            item {
                TextButton(onClick = { /* confirmar y borrar */ }) {
                    Text("Borrar proyecto", color = MaterialTheme.colorScheme.error)
                }
            }

            item { Spacer(Modifier.height(84.dp)) }

            if (!error.isNullOrBlank()) {
                item { Text(error ?: "", color = MaterialTheme.colorScheme.error) }
            }
        }
    }
}


@Composable
fun Label(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = Modifier.padding(start = 6.dp, top = 6.dp, bottom = 4.dp)
    )
}

@Composable
fun fieldColors() = TextFieldDefaults.colors(
    focusedContainerColor   = MaterialTheme.colorScheme.secondaryContainer,
    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
    disabledContainerColor  = MaterialTheme.colorScheme.secondaryContainer,
    focusedTextColor        = MaterialTheme.colorScheme.onSurface,
    unfocusedTextColor      = MaterialTheme.colorScheme.onSurface,
    cursorColor             = MaterialTheme.colorScheme.primary
)

@Composable
fun DateField(
    value: String,
    onPick: (String) -> Unit,
    enabled: Boolean = true
) {
    val ctx = LocalContext.current
    val cal = Calendar.getInstance()

    fun openDialog() {
        DatePickerDialog(
            ctx,
            { _, y, m, d ->
                val mm = (m + 1).toString().padStart(2, '0')
                val dd = d.toString().padStart(2, '0')
                onPick("$y-$mm-$dd")
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    TextField(
        value = value,
        onValueChange = { },
        readOnly = true,
        enabled = enabled,
        trailingIcon = {
            if (enabled) {
                IconButton(onClick = { openDialog() }) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = fieldColors()
    )
}
