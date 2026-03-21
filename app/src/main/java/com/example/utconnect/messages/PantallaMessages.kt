package com.example.utconnect.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

data class Contact(val id: Int, val name: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMessages(
    viewModel: ChatViewModel,
    onNavigateTo: (String) -> Unit = {},
    onContactClick: (Int, String) -> Unit = { _, _ -> }
) {
    val contacts = listOf(
        Contact(1, "Andres Flores"),
        Contact(2, "Toper"),
        Contact(3, "Roberto")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mensajes", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomNavigationBar(currentRoute = "Mensajes", onNavigateTo = onNavigateTo)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            items(contacts) { contact ->
                val messages = viewModel.getMessagesForContact(contact.id)
                val lastMessageText = if (messages.isNotEmpty()) {
                    messages.last().text
                } else {
                    "Inicia una conversación con ${contact.name}"
                }
                val lastMessageTime = if (messages.isNotEmpty()) {
                    messages.last().time
                } else {
                    ""
                }
                val isPlaceholder = messages.isEmpty()

                ContactItem(
                    name = contact.name,
                    lastMsg = lastMessageText,
                    time = lastMessageTime,
                    isPlaceholder = isPlaceholder
                ) {
                    onContactClick(contact.id, contact.name)
                }
                HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
            }
        }
    }
}

@Composable
fun ContactItem(
    name: String,
    lastMsg: String,
    time: String,
    isPlaceholder: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color(0xFFB0BEC5))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = time, color = Color.Gray, fontSize = 12.sp)
            }
            Text(
                text = lastMsg,
                fontSize = 14.sp,
                color = if (isPlaceholder) Color.LightGray else Color.DarkGray,
                maxLines = 1
            )
        }
    }
}
