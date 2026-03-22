package com.example.utconnect.record

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.utconnect.feed.BottomNavigationBar
import com.example.utconnect.feed.PostViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPublicarEstado(
    viewModel: PostViewModel,
    onNavigateTo: (String) -> Unit = {},
    onPublishSuccess: () -> Unit = {}
) {
    var textState by remember { mutableStateOf("") }
    var mediaSelected by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("Advertencia") },
            text = { Text("No se pueden publicar campos vacíos") }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            "Publicar Estado",
                            color = Color(0xFF008080),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { /* Back */ }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        TextButton(onClick = {
                            val trimmedText = textState.trim()
                            if (trimmedText.isBlank() && !mediaSelected) {
                                showDialog = true
                            } else {
                                // Guardar en ViewModel
                                viewModel.addPost(trimmedText, mediaSelected)
                                
                                scope.launch {
                                    snackbarHostState.showSnackbar("se ha publicado con éxito")
                                }
                                // Redirigir al feed
                                onPublishSuccess()
                            }
                        }) {
                            Text("Publicar", color = Color(0xFF008080), fontWeight = FontWeight.Bold)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
                HorizontalDivider(thickness = 0.5.dp, color = Color.Gray)
            }
        },
        bottomBar = {
            BottomNavigationBar(currentRoute = "Record", onNavigateTo = onNavigateTo)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDEE2E6))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Jesus Adrian Cardenas Calderon",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = textState,
                onValueChange = { textState = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                placeholder = { Text("¿Cuál es tu estado actual?") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = Color.Gray
                )
            )

            if (mediaSelected) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Imagen seleccionada ✓", color = Color(0xFF008080), fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8F9FA))
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StatusOptionItem("Subir imagen") {
                    mediaSelected = !mediaSelected
                }
                StatusOptionItem("Sentimiento") {}
                StatusOptionItem("Acontecimiento importante") {}
                StatusOptionItem("Dar aviso") {}
            }
        }
    }
}

@Composable
fun StatusOptionItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
}
