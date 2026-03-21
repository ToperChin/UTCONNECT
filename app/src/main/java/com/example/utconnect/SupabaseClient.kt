package com.example.utconnect

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

// NOTA: Reemplaza estas cadenas con tus valores reales de Supabase Dashboard
val supabase = createSupabaseClient(
    supabaseUrl = "https://nxkofxmhmbiendydgqgr.supabase.co",
    supabaseKey = "sb_publishable_LVBwW9iUa6u0yZEuaMRBww_i7emLp2q"
) {
    install(Auth)
    install(Postgrest)
    install(Realtime)
    install(Storage)
}
