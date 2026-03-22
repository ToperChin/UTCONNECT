package com.example.login.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

// 1. Objeto de configuración con tus credenciales
object SupabaseConfig {
    const val URL = "https://nxkofxmhmbiendydgqgr.supabase.co"
    const val ANON_KEY = "sb_publishable_LVBwW9iUa6u0yZEuaMRBww_i7emLp2q"
}

// 2. Cliente de Supabase listo para usar en toda la App
val supabase = createSupabaseClient(
    supabaseUrl = SupabaseConfig.URL,
    supabaseKey = SupabaseConfig.ANON_KEY
) {
    // Instalamos los módulos que necesitas
    install(Auth)
    install(Postgrest)
}
