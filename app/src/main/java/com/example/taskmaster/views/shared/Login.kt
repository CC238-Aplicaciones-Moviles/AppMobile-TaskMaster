package com.example.taskmaster.views.shared

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmaster.R
import com.example.taskmaster.viewmodel.data.net.TokenStore
import com.example.taskmaster.viewmodel.model.AuthViewModel
import com.example.taskmaster.viewmodel.sharedPreferences.Prefs

@Composable
fun Login(context: Context, nav: NavHostController) {

    val vm = remember { AuthViewModel() }
    val isLoading by vm.isLoading.collectAsState()
    val error by vm.error.collectAsState()
    val token by vm.token.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        Prefs.loadEmail(context)?.let { email = it }
        Prefs.loadPassword(context)?.let { password = it }

    }

    var justPressedLogin by remember { mutableStateOf(false) }
    LaunchedEffect(token) {
        val t = token
        if (justPressedLogin && !t.isNullOrBlank()) {
            if (rememberMe) {
                Prefs.saveAll(context, email, password, t)
            } else {
                Prefs.saveEmailAndPassword(context, email, password)
                Prefs.clearToken(context)
            }
            TokenStore.token = t
            nav.navigate("projects") {
                popUpTo("login") { inclusive = true }
                launchSingleTop = true
            }
            justPressedLogin = false
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(contentAlignment = Alignment.TopCenter) {

                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.50f),
                    painter = painterResource(id = R.drawable.shape),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )

                Column(
                    modifier = Modifier
                        .padding(top = 35.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier= Modifier
                            .size(140.dp)
                            .padding(top = 45.dp),
                        painter = painterResource(id = R.drawable.taskmaster_logoblanco),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "TaskMaster",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(120.dp))
                    Text(
                        text = "Inicia Sesión",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(45.dp))

                    // EMAIL
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 15.dp)
                            .width(350.dp),
                        placeholder = {
                            Text(
                                text = "Correo",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Email,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        )
                    )

                    // PASSWORD
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 15.dp)
                            .width(350.dp),
                        placeholder = {
                            Text(
                                text = "Contraseña",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        },
                        visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passVisible = !passVisible }) {
                                Image(
                                    modifier = Modifier.size(25.dp),
                                    painter = painterResource(
                                        if (passVisible) R.drawable.visibilityoff else R.drawable.visibility
                                    ),
                                    contentDescription = null
                                )
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        )
                    )

                    Spacer(modifier = Modifier.height(25.dp))
                    Button(
                        onClick = {
                            if (email.isNotBlank() && password.isNotBlank()) {
                                justPressedLogin = true
                                vm.signIn(email, password)
                            }
                        },
                        modifier = Modifier.width(180.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(Modifier.width(10.dp))
                        }
                        Text(
                            text = if (isLoading) "Ingresando..." else "Iniciar Sesión",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.padding(horizontal = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it }
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "Recordar credenciales",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    Spacer(Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "¿No tienes una cuenta?",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            "Crea una",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.clickable { nav.navigate("register") }
                        )
                    }

                }
            }
        }
    }
}
