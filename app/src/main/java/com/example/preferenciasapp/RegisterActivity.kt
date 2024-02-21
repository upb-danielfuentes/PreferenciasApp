package com.example.preferenciasapp

import AppDatabase
import android.content.ContentValues
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
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room

class RegisterActivity : AppCompatActivity() {
    lateinit var et_documento: EditText
    lateinit var et_nombre: EditText
    lateinit var et_clave: EditText
    lateinit var et_clave2: EditText
    lateinit var preferencias: SharedPreferences
    lateinit var databaseReference: DatabaseReference
    lateinit var db: AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initGUI()
        loadInfo()
        databaseReference = FirebaseDatabase.getInstance().reference.child("usuarios")

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "ormtest"
        ).build()

        val usuarioDao = db.usuarioDao()
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
            saveToSQLite(doc, nom, clave1, clave2)
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.putExtra("documento", doc)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            Toast.makeText(this, "Ocurrió un error al guardar los datos", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveToSharedPreferences(doc: String, nom: String, clave1: String, clave2: String) {
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

    private fun saveToSQLite(doc: String, nom: String, clave1: String, clave2: String) {
        val dbHelper = MyDbHelper(this)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(MyDbHelper.COLUMN_DOCUMENTO, doc)
            put(MyDbHelper.COLUMN_NOMBRE, nom)
            put(MyDbHelper.COLUMN_CLAVE1, clave1)
            put(MyDbHelper.COLUMN_CLAVE2, clave2)
        }

        val newRowId = db?.insert(MyDbHelper.TABLE_NAME, null, values)

        db?.close()
    }

    fun goToLogin(view: View) {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
    }


}
