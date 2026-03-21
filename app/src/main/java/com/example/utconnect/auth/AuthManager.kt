package com.example.utconnect.auth

<<<<<<< HEAD
import com.example.utconnect.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthManager {
    private val auth = supabase.auth

    suspend fun signUp(email: String, password: String) {
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun signIn(email: String, password: String) {
        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun signOut() {
        auth.signOut()
    }

    val currentUser = auth.currentUserOrNull()
=======
class AuthManager {
    // Simulamos un usuario logueado para que la app no intente conectar a Supabase por ahora
    fun signUp(email: String, password: String) {
        // Mock
    }

    fun signIn(email: String, password: String) {
        // Mock
    }

    fun signOut() {
        // Mock
    }

    val currentUser: String? = "Usuario de Prueba"
>>>>>>> 2a84b58246ea5ec549d55d9ef95d8801c3333cba
}
