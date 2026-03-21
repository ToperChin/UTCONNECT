package com.example.utconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UTConnectApp()
        }
    }
}

@Composable
fun UTConnectApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "qr_card") {
        composable("qr_card") {
            QRCardScreen(onNavigateToProfile = { navController.navigate("profile") })
        }
        composable("profile") {
            ProfileScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}

@Composable
fun QRCardScreen(onNavigateToProfile: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF00897B)), // Teal color from image
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth()
                .aspectRatio(0.7f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp),
            onClick = onNavigateToProfile
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Datos personales",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Start)
                )

                // Placeholder for QR Code
                Icon(
                    imageVector = Icons.Default.QrCode2,
                    contentDescription = "QR Code",
                    modifier = Modifier.size(200.dp),
                    tint = Color.Black
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Andres Ramos Flores",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "24393165",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Ingeniería", fontWeight = FontWeight.Medium)
                    Text(text = "Desarrollo de Software", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "LITIID42", fontWeight = FontWeight.Bold)
                    Text(text = "H210", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(icon = { Icon(Icons.Default.Home, "Inicio") }, label = { Text("Inicio") }, selected = true, onClick = {})
                NavigationBarItem(icon = { Icon(Icons.Default.Search, "Search") }, label = { Text("Search") }, selected = false, onClick = {})
                NavigationBarItem(icon = { Icon(Icons.Default.Mic, "Record") }, label = { Text("Record") }, selected = false, onClick = {})
                NavigationBarItem(icon = { Icon(Icons.Default.Notifications, "Notif") }, label = { Text("Notif") }, selected = false, onClick = {})
                NavigationBarItem(icon = { Icon(Icons.Default.Email, "Mensajes") }, label = { Text("Mensajes") }, selected = false, onClick = {})
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header Image
            Box(modifier = Modifier.height(150.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color.LightGray)
                )
                
                // Profile Picture
                Surface(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.BottomCenter)
                        .offset(y = 20.dp),
                    shape = CircleShape,
                    border = androidx.compose.foundation.BorderStroke(4.dp, Color.White),
                    color = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "JESUS ADRIAN CARDENAS CALDERON",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "LITIID 42",
                    color = Color(0xFF00897B),
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = {}, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                        Text("Crear anuncio")
                    }
                    Button(onClick = {}, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                        Text("Calendario")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreHoriz, null)
                    }
                }

                Button(
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Editar Perfil")
                }
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Text(
                text = "Información académica",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontWeight = FontWeight.Bold
            )
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Estudiante")
                Text("4° Cuatrimestre")
                Text("Club de programación")
                Text("Jefe de grupo")
            }

            Divider()
            
            Text(
                text = "Publicaciones",
                modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold
            )

            // Post Placeholder
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = Color.LightGray) {}
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Jesus Adrián Cardenas Calderon", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.weight(1f))
                        Text("7h", color = Color.Gray)
                    }
                    Text(
                        "Quiero felicitar a mi querido amigo el cual fue el ganador del torneo de Ajedrez!!!",
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(Color.LightGray, RoundedCornerShape(8.dp))
                    )
                    Row(modifier = Modifier.padding(top = 8.dp)) {
                        Icon(Icons.Default.FavoriteBorder, null, modifier = Modifier.size(20.dp))
                        Text(" 2434", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(Icons.Default.ChatBubbleOutline, null, modifier = Modifier.size(20.dp))
                        Text(" 2434", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
