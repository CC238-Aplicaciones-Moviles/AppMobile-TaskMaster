package com.example.taskmaster.views.layout.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.taskmaster.viewmodel.data.tasks.TaskDto

@Composable
fun TaskList(
    tasks: List<TaskDto>,
    onTaskClick: (TaskDto) -> Unit = {}
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(items = tasks, key = { it.taskId }) { task ->
            CardTask(task = task) { onTaskClick(task) }
        }
    }
}
