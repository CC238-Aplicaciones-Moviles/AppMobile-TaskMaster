package com.example.taskmaster.viewmodel.data.projects


data class ProjectDto(
    val id: Long,
    val projectId: Long,
    val key: String,
    val leaderId: Long,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val budget: Double,
    val status: String,
    val startDate: String,
    val endDate: String
)

data class ProjectCreateRequest(
    val name: String,
    val description: String,
    val imageUrl : String,
    val budget: Double,
    val endDate: String
)

data class ProjectUpdateRequest(
    val name: String,
    val description: String,
    val imageUrl : String,
    val budget: Double,
    val status: String,
    val endDate: String
)

data class ProjectCodeRequest(val code: String)
