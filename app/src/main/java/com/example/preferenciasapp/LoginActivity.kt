package com.example.preferenciasapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    lateinit var tv_info: TextView
    lateinit var et_documento: EditText
    lateinit var preferencias: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initGUI()
        loadInfo()
    }

    fun initGUI() {
        et_documento = findViewById(R.id.et_documento)
        preferencias = getSharedPreferences("info", Context.MODE_PRIVATE)
    }

    fun goToRegister(view: View) {
        startActivity(Intent(applicationContext, RegisterActivity::class.java))
        finish()
    }

    fun loadInfo() {
        val doc = preferencias.getString("documento", "")
        et_documento.setText(doc)
    }

}
