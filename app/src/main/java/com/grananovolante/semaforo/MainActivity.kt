package com.grananovolante.semaforo
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 60, 40, 40)
        }
        val titulo = TextView(this).apply {
            text = "Semáforo de Viagens"
            textSize = 22f
        }
        val btnPermissao = Button(this).apply {
            text = "1. Permitir Sobrepor à Tela"
            setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this@MainActivity)) {
                    startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
                }
            }
        }
        val btnServico = Button(this).apply {
            text = "2. Ativar Leitura de Corridas"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
        }
        layout.addView(titulo)
        layout.addView(btnPermissao)
        layout.addView(btnServico)
        setContentView(layout)
    }
}
