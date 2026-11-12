package com.grananovolante.semaforo
import android.accessibilityservice.AccessibilityService
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.TextView
class LeitorService : AccessibilityService() {
    private var windowManager: WindowManager? = null
    private var bolinhaView: TextView? = null
    override fun onServiceConnected() {
        super.onServiceConnected()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.TOP or Gravity.END; x = 20; y = 200 }
        bolinhaView = TextView(this).apply {
            text = "🚦"
            textSize = 28f
            setPadding(20, 20, 20, 20)
            setBackgroundColor(Color.parseColor("#333333"))
        }
        try { windowManager?.addView(bolinhaView, params) } catch (e: Exception) {}
    }
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val root = rootInActiveWindow ?: return
        val sb = StringBuilder()
        extrairTextos(root, sb)
        val avaliacao = MotorRegras.avaliar(sb.toString())
        bolinhaView?.post {
            when (avaliacao.cor) {
                CorSemaforo.VERDE -> { bolinhaView?.setBackgroundColor(Color.parseColor("#10B981")); bolinhaView?.text = "🟢" }
                CorSemaforo.AMARELO -> { bolinhaView?.setBackgroundColor(Color.parseColor("#F59E0B")); bolinhaView?.text = "🟡" }
                CorSemaforo.VERMELHO -> { bolinhaView?.setBackgroundColor(Color.parseColor("#EF4444")); bolinhaView?.text = "🔴" }
            }
        }
    }
    private fun extrairTextos(node: AccessibilityNodeInfo, sb: StringBuilder) {
        if (!node.text.isNullOrEmpty()) sb.append(node.text).append(" ")
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            extrairTextos(child, sb)
        }
    }
    override fun onInterrupt() {}
    override fun onDestroy() {
        super.onDestroy()
        bolinhaView?.let { windowManager?.removeView(it) }
    }
}
