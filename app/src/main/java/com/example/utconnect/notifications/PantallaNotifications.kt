package com.example.utconnect.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.utconnect.feed.BottomNavigationBar

data class Notification(val id: Int, val user: String, val action: String, val time: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaNotifications(onNavigateTo: (String) -> Unit = {}) {
    val notifications = listOf(
        Notification(1, "Ana Garcia", "le gustó tu publicación", "2m"),
        Notification(2, "Luis Perez", "comentó: '¡Excelente!'", "15m"),
        Notification(3, "Marta Gomez", "te empezó a seguir", "1h"),
        Notification(4, "Carlos Ruiz", "compartió tu post", "3h")
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Notificaciones", fontWeight = FontWeight.Bold) })
        },
        bottomBar = {
            BottomNavigationBar(currentRoute = "Noit", onNavigateTo = onNavigateTo)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            items(notifications) { notification ->
                NotificationItem(notification)
                HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
            }
        }
    }
}

@Composable
fun NotificationItem(notification: Notification) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${notification.user} ${notification.action}",
                fontSize = 14.sp
            )
            Text(text = notification.time, color = Color.Gray, fontSize = 12.sp)
        }
    }
}
