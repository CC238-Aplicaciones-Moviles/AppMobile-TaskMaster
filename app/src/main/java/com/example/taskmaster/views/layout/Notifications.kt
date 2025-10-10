package com.example.taskmaster.views.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.taskmaster.R

@Composable

fun Notifiations(){


    Column {
        Text(
            text = "Notificaciones",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(15.dp)
        )
        Column(modifier = Modifier
            .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly) {


            var query by remember { mutableStateOf("") }
            TextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 12.dp)
                    .heightIn(min = 40.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge,
                placeholder = {
                    Text(
                        "Buscar notificaciones",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                },
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,

                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.primary,

                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),


                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(R.drawable.ic_notifications),
                    contentDescription = null,
                    modifier = Modifier
                        .size(140.dp)
                        .padding(top = 24.dp, bottom = 12.dp)
                )

                Text(
                    text = "¡No cuentas con ninguna notificación !",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 8.dp)
                )


            }

        }
    }


}