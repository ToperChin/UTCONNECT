package com.example.utconnect.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.utconnect.feed.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaSearch(onNavigateTo: (String) -> Unit = {}) {
    var query by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Buscar en UTConnect") })
        },
        bottomBar = {
            BottomNavigationBar(currentRoute = "Search", onNavigateTo = onNavigateTo)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar estudiantes, grupos...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = MaterialTheme.shapes.medium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Tendencias en tu facultad", style = MaterialTheme.typography.titleMedium)
        }
    }
}
