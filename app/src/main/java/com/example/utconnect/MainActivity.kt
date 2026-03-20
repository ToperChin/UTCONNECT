package com.example.utconnect

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.utconnect.ui.SettingsActivity
import com.example.utconnect.ui.theme.UTCONNECTTheme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

val supabase = createSupabaseClient(
    supabaseUrl = "https://nxkofxmhmbiendydgqgr.supabase.co",
    supabaseKey = "sb_publishable_LVBwW9iUa6u0yZEuaMRBww_i7emLp2q"
) {
    install(Auth)
}

enum class Screen {
    USUARIO, CONFIGURACION, INICIO, MENSAJES, NOTIFICACIONES
}

data class Post(
    val title: String,
    val description: String,
    val category: String,
    val time: String = "Recién publicado"
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UTCONNECTTheme {
                var currentScreen by remember { mutableStateOf(Screen.INICIO) }
                var showCreatePostDialog by remember { mutableStateOf(false) }
                
                // Lista de posts en estado para que sea reactiva
                val posts = remember { mutableStateListOf<Post>(
                    Post("Bienvenida", "Bienvenidos a UT Connect", "Anuncios", "Hace 1 hora"),
                    Post("Venta de Libros", "Libros de programación en buen estado", "Ventas", "Hace 3 horas")
                ) }
                
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        if (currentScreen == Screen.INICIO) {
                            FloatingActionButton(
                                onClick = { showCreatePostDialog = true },
                                containerColor = Color(0xFF008080),
                                contentColor = Color.White
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Añadir actividad")
                            }
                        }
                    },
                    bottomBar = {
                        CustomBottomNavigation(currentScreen) { currentScreen = it }
                    }
                ) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        when (currentScreen) {
                            Screen.INICIO -> FeedScreen(posts)
                            Screen.NOTIFICACIONES -> NotificationsScreen()
                            Screen.USUARIO -> ProfileScreen()
                            Screen.CONFIGURACION -> {
                                val context = LocalContext.current
                                LaunchedEffect(Unit) {
                                    context.startActivity(Intent(context, SettingsActivity::class.java))
                                    currentScreen = Screen.INICIO
                                }
                            }
                            Screen.MENSAJES -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("Pantalla de Mensajes (Próximamente)")
                            }
                        }
                    }

                    if (showCreatePostDialog) {
                        CreatePostDialog(
                            onDismiss = { showCreatePostDialog = false },
                            onPostCreated = { newPost ->
                                posts.add(0, newPost) // Añadir al inicio de la lista
                                showCreatePostDialog = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FeedScreen(posts: List<Post>) {
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val categories = listOf("Anuncios", "Ventas", "Eventos", "Administracion")

    // Filtrar posts según la categoría seleccionada
    val filteredPosts = if (selectedCategory == null) {
        posts
    } else {
        posts.filter { it.category == selectedCategory }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar actividades...") },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
            shape = RoundedCornerShape(28.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color(0xFF008080)
            )
        )
        
        Row(
            Modifier
                .padding(vertical = 12.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            categories.forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = if (selectedCategory == category) null else category },
                    label = { Text(category) },
                    modifier = Modifier.padding(end = 8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF008080),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        if (filteredPosts.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay publicaciones en esta categoría", color = Color.Gray)
            }
        } else {
            LazyColumn {
                items(filteredPosts) { post ->
                    ActivityCard(post)
                }
            }
        }
    }
}

@Composable
fun ActivityCard(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFFEEEEEE)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.PhotoCamera, null, Modifier.size(48.dp), tint = Color.Gray)
                    Text("Toca para añadir o ver imagen", color = Color.Gray, fontSize = 12.sp)
                }
            }
            
            Row(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(post.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(post.description, color = Color.DarkGray, fontSize = 14.sp)
                    Text(post.time, color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                }
                Surface(
                    color = Color(0xFFE0F2F1),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        post.category,
                        color = Color(0xFF008080),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CreatePostDialog(onDismiss: () -> Unit, onPostCreated: (Post) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Anuncios") }
    val categories = listOf("Anuncios", "Ventas", "Eventos", "Administracion")
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Actividad", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                Spacer(Modifier.height(8.dp))
                
                // Selector de Categoría
                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Categoría: $selectedCategory")
                            Icon(Icons.Default.ArrowDropDown, null)
                        }
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Image, null, tint = Color.Gray)
                        Text(" Añadir Foto", color = Color.Gray)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotEmpty() && description.isNotEmpty()) {
                        onPostCreated(Post(title, description, selectedCategory))
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008080))
            ) {
                Text("Publicar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun ProfileScreen() {
    val user = supabase.auth.currentUserOrNull()
    val userName = user?.userMetadata?.get("full_name")?.toString()?.replace("\"", "") ?: "Usuario UT"

    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.size(100.dp),
            shape = RoundedCornerShape(50.dp),
            color = Color(0xFF008080)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(userName.take(1).uppercase(), color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Bold)
            }
        }
        
        Spacer(Modifier.height(16.dp))
        Text(userName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(user?.email ?: "correo@ut.edu.mx", color = Color.Gray)
        
        Spacer(Modifier.height(32.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))
        
        Text("Tus Actividades Recientes", modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.Bold)
        LikesScreenContent()
    }
}

@Composable
fun LikesScreenContent() {
    LazyColumn {
        items(listOf("Post de Bienvenida", "Ganador Ajedrez", "Venta de Libros")) { item ->
            ListItem(
                headlineContent = { Text(item) },
                supportingContent = { Text("Hace 2 días") },
                leadingContent = { Icon(Icons.Default.Star, null, tint = Color(0xFF008080)) }
            )
        }
    }
}

@Composable
fun NotificationsScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Notificaciones", color = Color(0xFF008080), fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        
        LazyColumn {
            item { NotificationItem("Adrian Cardenas", "Comenzó a Seguirte", "Hace 20 min") }
            item { NotificationItem("Andres Ramos", "Realizó una Nueva Publicación", "Hoy a las 8:20 AM") }
        }
    }
}

@Composable
fun NotificationItem(name: String, action: String, time: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Surface(Modifier.size(48.dp), shape = RoundedCornerShape(24.dp), color = Color(0xFFE0F2F1)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, tint = Color(0xFF008080))
            }
        }
        Column(Modifier.padding(start = 16.dp)) {
            Text(name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(action, fontSize = 14.sp, color = Color.DarkGray)
            Text(time, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun CustomBottomNavigation(selectedScreen: Screen, onScreenSelected: (Screen) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(90.dp)
    ) {
        val items = listOf(
            Triple(Screen.USUARIO, Icons.Default.Person, "Usuario"),
            Triple(Screen.CONFIGURACION, Icons.Default.Settings, "Config"),
            Triple(Screen.INICIO, Icons.Default.Home, "Inicio"),
            Triple(Screen.MENSAJES, Icons.Default.MailOutline, "Mensajes"),
            Triple(Screen.NOTIFICACIONES, Icons.Default.Notifications, "Notif")
        )

        items.forEach { (screen, icon, label) ->
            NavigationBarItem(
                selected = selectedScreen == screen,
                onClick = { onScreenSelected(screen) },
                icon = { Icon(icon, null, modifier = Modifier.size(26.dp)) },
                label = { Text(label, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF008080),
                    selectedTextColor = Color(0xFF008080),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color(0xFFE0F2F1)
                )
            )
        }
    }
}
