package com.example.preferenciasapp

import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var etDocumento: EditText
    private lateinit var etNombre: EditText
    private lateinit var etClave: EditText
    private lateinit var etClave2: EditText
    private lateinit var preferencias: SharedPreferences
    private lateinit var databaseReference: DatabaseReference
    private lateinit var dbHelper: MyDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initViews()
        loadDataFromPreferences()

        databaseReference = FirebaseDatabase.getInstance().reference.child("usuarios")
        dbHelper = MyDbHelper(this)
    }

    private fun initViews() {
        etDocumento = findViewById(R.id.et_documento)
        etNombre = findViewById(R.id.et_nombre)
        etClave = findViewById(R.id.et_clave)
        etClave2 = findViewById(R.id.et_clave2)
        preferencias = getSharedPreferences("info", MODE_PRIVATE)
    }

    private fun loadDataFromPreferences() {
        etDocumento.setText(preferencias.getString("documento", ""))
        etNombre.setText(preferencias.getString("nombre", ""))
        etClave.setText(preferencias.getString("clave", ""))
        etClave2.setText(preferencias.getString("clave2", ""))
    }

    fun saveInfo(view: View) {
        val doc = etDocumento.text.toString()
        val nom = etNombre.text.toString()
        val clave1 = etClave.text.toString()
        val clave2 = etClave2.text.toString()

        if (clave1 != clave2) {
            etClave2.error = "Las claves deben ser iguales"
            return
        }

        try {
            if (saveToSharedPreferences(doc, nom, clave1, clave2) &&
                saveToFirebase(doc, nom, clave1, clave2) &&
                saveToSQLite(doc, nom, clave1, clave2)) {
                navigateToDisplayDataActivity()
                limpiarCampos()
            } else {
                showToast("Error al guardar el registro")
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            showToast("Ocurrió un error al guardar los datos")
        }
    }

    private fun saveToSharedPreferences(doc: String, nom: String, clave1: String, clave2: String): Boolean {
        return try {
            preferencias.edit().apply {
                putString("documento", doc)
                putString("nombre", nom)
                putString("clave", clave1)
                putString("clave2", clave2)
                apply()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun saveToFirebase(doc: String, nom: String, clave1: String, clave2: String): Boolean {
        return try {
            databaseReference.push().apply {
                child("documento").setValue(doc)
                child("nombre").setValue(nom)
                child("clave").setValue(clave1)
                child("clave2").setValue(clave2)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun saveToSQLite(doc: String, nom: String, clave1: String, clave2: String): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val values = ContentValues().apply {
                put(MyDbHelper.COLUMN_DOCUMENTO, doc)
                put(MyDbHelper.COLUMN_NOMBRE, nom)
                put(MyDbHelper.COLUMN_CLAVE1, clave1)
                put(MyDbHelper.COLUMN_CLAVE2, clave2)
            }
            val newRowId = db.insert(MyDbHelper.TABLE_NAME, null, values)
            newRowId != -1L
        } finally {
            db.close()
        }
    }

    private fun navigateToDisplayDataActivity() {
        val intent = Intent(this, ReciclerActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun goToLogin(view: View) {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
    }

    fun limpiarCampos() {
        etDocumento.text.clear()
        etNombre.text.clear()
        etClave.text.clear()
        etClave2.text.clear()
    }
}
