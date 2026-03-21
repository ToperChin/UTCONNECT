package com.example.utconnect.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFeedEliminar(
    viewModel: PostViewModel,
    onNavigateTo: (String) -> Unit = {},
    onCommentClick: (Int) -> Unit = {}
) {
    val posts = viewModel.posts

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            "UTConnect",
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
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
                HorizontalDivider(thickness = 0.5.dp, color = Color.Gray)
            }
        },
        bottomBar = {
            BottomNavigationBar(currentRoute = "Inicio", onNavigateTo = onNavigateTo)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            items(posts) { post ->
                PostItem(
                    post = post,
                    onDelete = { viewModel.deletePost(post.id) },
                    onLikeClick = { viewModel.toggleLike(post.id) },
                    onCommentClick = { onCommentClick(post.id) }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)
            }
        }
    }
}

@Composable
fun PostItem(post: Post, onDelete: () -> Unit, onLikeClick: () -> Unit, onCommentClick: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = post.user, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = post.time, color = Color.Gray, fontSize = 14.sp)
            }
            
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Outlined.MoreVert, contentDescription = "Opciones")
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(
                        text = { Text("Borrar") },
                        onClick = { onDelete(); showMenu = false },
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = post.content, fontSize = 16.sp, lineHeight = 22.sp)

        if (post.hasImage) {
            Spacer(modifier = Modifier.height(12.dp))
            AsyncImage(
                model = post.imageUrl ?: "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=1000&q=80",
                contentDescription = "Post image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onLikeClick) {
                Icon(
                    imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (post.isLiked) Color(0xFFFF69B4) else Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(" ${post.likes}", fontSize = 14.sp)
            
            Spacer(modifier = Modifier.width(32.dp))
            
            IconButton(onClick = onCommentClick) {
                Icon(Icons.AutoMirrored.Outlined.Chat, contentDescription = "Comments", modifier = Modifier.size(22.dp))
            }
            Text(" ${post.commentCount}", fontSize = 14.sp)
        }
    }
}

@Composable
fun BottomNavigationBar(currentRoute: String, onNavigateTo: (String) -> Unit) {
    val tealColor = Color(0xFF008080)
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        val items = listOf(
            NavigationItem("Inicio", Icons.Outlined.Home, "Inicio", false),
            NavigationItem("Search", Icons.Outlined.Search, "Search", false),
            NavigationItem("Subir", Icons.Outlined.AddBox, "Record", true),
            NavigationItem("Noit", Icons.Outlined.Notifications, "Noit", false),
            NavigationItem("Mensajes", Icons.AutoMirrored.Outlined.Chat, "Mensajes", true)
        )
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigateTo(item.route) },
                icon = { Icon(item.icon, contentDescription = item.title, tint = if (item.isTeal) tealColor else if (currentRoute == item.route) Color.Black else Color.Gray) },
                label = { Text(item.title, fontSize = 10.sp, color = if (item.isTeal) tealColor else if (currentRoute == item.route) Color.Black else Color.Gray) },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )
        }
    }
}

data class NavigationItem(val title: String, val icon: ImageVector, val route: String, val isTeal: Boolean)
