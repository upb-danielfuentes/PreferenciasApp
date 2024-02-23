package com.example.preferenciasapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import androidx.recyclerview.widget.RecyclerView

class ReciclerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recicler)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Asumiendo que tienes una función getUsuariosFromDatabase() que retorna una List<Usuario>
        val usuarios = getUsuariosFromDatabase(this)
        recyclerView.adapter = UsuarioAdapter(usuarios)


    }

    fun getUsuariosFromDatabase(context: Context): List<Usuario> {
        val dbHelper = MyDbHelper(context)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            BaseColumns._ID, // Asegúrate de que estos coincidan con las definiciones en MyDbHelper
            MyDbHelper.COLUMN_DOCUMENTO,
            MyDbHelper.COLUMN_NOMBRE,
            MyDbHelper.COLUMN_CLAVE1,
            MyDbHelper.COLUMN_CLAVE2
        )

        val cursor = db.query(
            MyDbHelper.TABLE_NAME,   // La tabla a consultar
            projection,              // Las columnas a retornar
            null,           // La columna para la cláusula WHERE
            null,       // Los valores para la cláusula WHERE
            null,           // No agrupar las filas
            null,            // No filtrar por grupos de filas
            null            // El orden del sorteo
        )

        val usuarios = mutableListOf<Usuario>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                val documento = getString(getColumnIndexOrThrow(MyDbHelper.COLUMN_DOCUMENTO))
                val nombre = getString(getColumnIndexOrThrow(MyDbHelper.COLUMN_NOMBRE))
                val clave1 = getString(getColumnIndexOrThrow(MyDbHelper.COLUMN_CLAVE1))
                val clave2 = getString(getColumnIndexOrThrow(MyDbHelper.COLUMN_CLAVE2))
                usuarios.add(Usuario(id, documento, nombre, clave1, clave2))
            }
        }
        cursor.close()

        return usuarios
    }

}

class Usuario(id: Int, documento: String?, nombre: String?, clave1: String?, clave2: String?) {
    // Propiedades de la clase
    val id: Int
    val documento: String?
    val nombre: String?
    val clave1: String?
    val clave2: String?

    // Inicializador primario
    init {
        this.id = id
        this.documento = documento
        this.nombre = nombre
        this.clave1 = clave1
        this.clave2 = clave2
    }
}
