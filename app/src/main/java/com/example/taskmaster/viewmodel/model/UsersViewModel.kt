// viewmodel/ui/users/UsersViewModel.kt
package com.example.taskmaster.viewmodel.ui.users

import androidx.lifecycle.ViewModel
import com.example.taskmaster.viewmodel.data.repo.UsersRepository
import com.example.taskmaster.viewmodel.data.users.UserDto
import com.example.taskmaster.viewmodel.data.users.UserUpdateRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsersViewModel(
    private val repo: UsersRepository = UsersRepository()
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _user = MutableStateFlow<UserDto?>(null)
    val user: StateFlow<UserDto?> = _user

    fun loadByEmail(email: String) {
        scope.launch {
            try {
                _isLoading.value = true
                _user.value = repo.getByEmail(email)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateProfile(username: String, imageUrl: String?, salary: Double?) = scope.launch {
        try {
            val (name, lastName) = splitUsername(username)
            _isLoading.value = true
            val updated = repo.update(
                UserUpdateRequest(
                    name = name,
                    lastName = lastName,
                    imageUrl = imageUrl,
                    salary = salary
                )
            )
            _user.value = updated
            _error.value = null
        } catch (e: Exception) {
            _error.value = e.message
        } finally {
            _isLoading.value = false
        }
    }

    private fun splitUsername(username: String): Pair<String, String> {
        val parts = username.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
        val name = parts.firstOrNull() ?: "Usuario"
        val last = if (parts.size > 1) parts.drop(1).joinToString(" ") else "Nuevo"
        return name to last
    }
}
