package com.example.utconnect.ui

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.utconnect.R
import com.example.utconnect.auth.LoginActivity
import com.example.utconnect.supabase
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_settings)

            findViewById<ImageButton>(R.id.btnBack)?.setOnClickListener { finish() }

            findViewById<android.view.View>(R.id.btnDatosPersonales)?.setOnClickListener {
                showEditNameDialog()
            }

            findViewById<android.view.View>(R.id.btnSeguridad)?.setOnClickListener {
                showSimpleDialog("Seguridad", "Configuración de seguridad vinculada a Supabase Auth.")
            }

            findViewById<android.view.View>(R.id.btnNotificaciones)?.setOnClickListener {
                showNotificationsDialog()
            }

            findViewById<android.view.View>(R.id.btnPrivacidad)?.setOnClickListener {
                showSimpleDialog("Privacidad", "Tus datos están protegidos por las políticas de Supabase.")
            }

            findViewById<android.view.View>(R.id.btnAyudaSoporte)?.setOnClickListener {
                showSimpleDialog("Ayuda y Soporte", "Contacta con nosotros en soporte@utconnect.com")
            }

            findViewById<android.view.View>(R.id.btnTerminosPoliticas)?.setOnClickListener {
                showSimpleDialog("Términos y Políticas", "Consulta los términos legales en nuestro sitio web.")
            }

            findViewById<android.view.View>(R.id.btnReportarProblema)?.setOnClickListener {
                showReportProblemDialog()
            }

            findViewById<android.view.View>(R.id.btnAnadirCuenta)?.setOnClickListener {
                showToast("Función multi-cuenta en desarrollo.")
            }

            findViewById<android.view.View>(R.id.btnCerrarSesion)?.setOnClickListener {
                showSignOutConfirmation()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error al cargar configuración: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun showEditNameDialog() {
        val input = EditText(this)
        input.hint = "Ingresa tu nuevo nombre"
        val container = LinearLayout(this)
        container.orientation = LinearLayout.VERTICAL
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(60, 20, 60, 0)
        input.layoutParams = params
        container.addView(input)

        MaterialAlertDialogBuilder(this)
            .setTitle("Actualizar Perfil")
            .setView(container)
            .setPositiveButton("Actualizar") { _, _ ->
                val newName = input.text.toString()
                if (newName.isNotEmpty()) updateUserName(newName)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun updateUserName(name: String) {
        lifecycleScope.launch {
            try {
                // Actualiza el nombre en los metadatos de Supabase Auth
                supabase.auth.updateUser {
                    data = buildJsonObject {
                        put("full_name", name)
                    }
                }
                showToast("Perfil actualizado correctamente")
            } catch (e: Exception) {
                showToast("Error de conexión: ${e.message}")
            }
        }
    }

    private fun showReportProblemDialog() {
        val input = EditText(this)
        input.hint = "Describe el problema..."
        input.minLines = 3
        val container = LinearLayout(this)
        container.orientation = LinearLayout.VERTICAL
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(60, 20, 60, 0)
        input.layoutParams = params
        container.addView(input)

        MaterialAlertDialogBuilder(this)
            .setTitle("Enviar Reporte")
            .setView(container)
            .setPositiveButton("Enviar") { _, _ ->
                val reportText = input.text.toString()
                if (reportText.isNotEmpty()) sendReportToSupabase(reportText)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun sendReportToSupabase(content: String) {
        lifecycleScope.launch {
            try {
                val user = supabase.auth.currentUserOrNull()
                if (user != null) {
                    // Intenta insertar el reporte en la tabla 'reports' de Supabase
                    supabase.postgrest["reports"].insert(buildJsonObject {
                        put("content", content)
                        put("user_id", user.id)
                    })
                    showToast("Reporte enviado a la base de datos")
                } else {
                    showToast("Debes iniciar sesión para reportar")
                }
            } catch (e: Exception) {
                showToast("Error al enviar reporte: ${e.message}")
            }
        }
    }

    private fun showNotificationsDialog() {
        val options = arrayOf("Push", "Email", "Alertas")
        val checked = booleanArrayOf(true, false, true)
        MaterialAlertDialogBuilder(this)
            .setTitle("Notificaciones")
            .setMultiChoiceItems(options, checked) { _, _, _ -> }
            .setPositiveButton("Guardar", null)
            .show()
    }

    private fun showSimpleDialog(title: String, message: String) {
        MaterialAlertDialogBuilder(this).setTitle(title).setMessage(message).setPositiveButton("OK", null).show()
    }

    private fun showSignOutConfirmation() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Quieres salir de UT Connect?")
            .setPositiveButton("Salir") { _, _ -> cerrarSesion() }
            .setNegativeButton("Volver", null)
            .show()
    }

    private fun cerrarSesion() {
        lifecycleScope.launch {
            try {
                supabase.auth.signOut()
                val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                showToast("Error: ${e.message}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
