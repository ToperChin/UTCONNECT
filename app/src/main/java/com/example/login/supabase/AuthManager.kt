package com.example.login.supabase

import android.util.Log
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthManager {
    
    // Usamos el cliente 'supabase' que definimos en SupabaseConfig.kt
    suspend fun iniciarSesion(emailDigitado: String, passwordDigitado: String): Boolean {
        return try {
            supabase.auth.signInWith(Email) {
                email = emailDigitado
                password = passwordDigitado
            }
            Log.d("SUPABASE", "Login exitoso")
            true
        } catch (e: Exception) {
            Log.e("SUPABASE", "Error al iniciar sesión: ${e.message}")
            false
        }
    }
}
