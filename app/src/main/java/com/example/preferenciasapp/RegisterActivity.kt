package com.example.preferenciasapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    lateinit var et_documento: EditText
    lateinit var et_nombre: EditText
    lateinit var et_clave: EditText
    lateinit var et_clave2: EditText
    lateinit var preferencias: SharedPreferences
    lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initGUI()
        loadInfo()
        databaseReference = FirebaseDatabase.getInstance().reference.child("usuarios")
    }

    private fun initGUI() {
        et_documento = findViewById(R.id.et_documento)
        et_nombre = findViewById(R.id.et_nombre)
        et_clave = findViewById(R.id.et_clave)
        et_clave2 = findViewById(R.id.et_clave2)
        preferencias = getSharedPreferences("info", MODE_PRIVATE)
    }

    private fun loadInfo() {
        val doc = preferencias.getString("documento", "")
        val nom = preferencias.getString("nombre", "")
        val clave1 = preferencias.getString("clave", "")
        val clave2 = preferencias.getString("clave2", "")

        et_documento.setText(doc)
        et_nombre.setText(nom)
        et_clave.setText(clave1)
        et_clave2.setText(clave2)
    }

    fun saveInfo(v: View) {
        val doc = et_documento.text.toString()
        val nom = et_nombre.text.toString()
        val clave1 = et_clave.text.toString()
        val clave2 = et_clave2.text.toString()

        if (clave2 != clave1) {
            et_clave2.error = "Las claves deben ser iguales"
            return
        }
        saveToSharedPreferences(doc, nom, clave1, clave2)

        try {
            saveToFirebase(doc, nom, clave1, clave2)
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.putExtra("documento", doc)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            Toast.makeText(this, "Ocurrió un error al guardar los datos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveToSharedPreferences(doc: String, nom: String, clave1: String, clave2: String) {
        val editor = preferencias.edit()
        editor.putString("documento", doc)
        editor.putString("nombre", nom)
        editor.putString("clave", clave1)
        editor.putString("clave2", clave2)
        editor.apply()
    }

    private fun saveToFirebase(doc: String, nom: String, clave1: String, clave2: String) {
        val nuevaEntrada = databaseReference.push()
        nuevaEntrada.child("documento").setValue(doc)
        nuevaEntrada.child("nombre").setValue(nom)
        nuevaEntrada.child("clave").setValue(clave1)
        nuevaEntrada.child("clave2").setValue(clave2)
    }

    fun goToLogin(view: View) {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
    }
}
