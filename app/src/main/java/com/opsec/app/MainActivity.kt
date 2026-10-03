package com.opsec.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.*
import android.content.Intent
import android.provider.Settings

class MainActivity : Activity() {
    private lateinit var log: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(buildUi())
        appendLog("OPSEC ready")
        appendLog("Mode: BYPASS / no partition flashing")
    }

    private fun buildUi(): View {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 24, 28, 24)
            setBackgroundColor(Color.rgb(7, 6, 12))
        }

        val title = TextView(this).apply {
            text = "OPSEC"
            textSize = 34f
            setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD
        }
        root.addView(title)

        root.addView(TextView(this).apply {
            text = "Quest bypass utility"
            textSize = 14f
            setTextColor(Color.rgb(160, 150, 180))
        })

        val status = TextView(this).apply {
            text = "● ADB: not connected"
            textSize = 15f
            setTextColor(Color.rgb(255, 180, 70))
            setPadding(0, 18, 0, 18)
        }
        root.addView(status)

        root.addView(button("CONNECT ADB") {
            appendLog("ADB connection requested")
            status.text = "● ADB: waiting for connection"
            try { startActivity(Intent(Settings.ACTION_SETTINGS)) } catch (_: Exception) {}
        })

        root.addView(section("BYPASS"))
        root.addView(button("CHECK STATUS") {
            appendLog("Checking headset status...")
            appendLog("No protected-partition operation is performed by OPSEC.")
        })
        root.addView(button("RUN BYPASS") {
            appendLog("Bypass requested.")
            appendLog("A verified runtime agent is required before attaching.")
        })

        root.addView(section("TOOLS"))
        root.addView(button("START FRIDA SERVER") {
            appendLog("Frida server start requested.")
            appendLog("Transport is intended to remain localhost-only.")
        })
        root.addView(button("OPEN WIRELESS DEBUGGING") {
            try { startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS)) }
            catch (_: Exception) { startActivity(Intent(Settings.ACTION_SETTINGS)) }
        })

        root.addView(section("LOG"))
        log = TextView(this).apply {
            textSize = 12f
            setTextColor(Color.rgb(205, 198, 220))
            setBackgroundColor(Color.rgb(14, 12, 22))
            setPadding(16, 14, 16, 14)
        }
        val scroll = ScrollView(this)
        scroll.addView(log)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        return root
    }

    private fun section(text: String) = TextView(this).apply {
        this.text = text
        textSize = 12f
        setTextColor(Color.rgb(155, 105, 255))
        typeface = Typeface.DEFAULT_BOLD
        setPadding(0, 22, 0, 8)
    }

    private fun button(text: String, action: () -> Unit) = Button(this).apply {
        this.text = text
        isAllCaps = false
        setTextColor(Color.WHITE)
        setOnClickListener { action() }
        layoutParams = LinearLayout.LayoutParams(-1, 54).apply { bottomMargin = 8 }
    }

    private fun appendLog(text: String) {
        if (::log.isInitialized) log.append("[$" + System.currentTimeMillis() / 1000 + "] " + text + "\n")
    }
}
