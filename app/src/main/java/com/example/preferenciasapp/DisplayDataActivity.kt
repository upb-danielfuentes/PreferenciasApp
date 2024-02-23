package com.example.preferenciasapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView

class DisplayDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_data)

        val tableLayout = findViewById<TableLayout>(R.id.tablita)
        displayDatabaseInfo(tableLayout)
    }

    private fun displayDatabaseInfo(tableLayout: TableLayout) {
        val dbHelper = MyDbHelper(this)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID, MyDbHelper.COLUMN_DOCUMENTO, MyDbHelper.COLUMN_NOMBRE, MyDbHelper.COLUMN_CLAVE1, MyDbHelper.COLUMN_CLAVE2)
        val cursor = db.query(
            MyDbHelper.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null)

        try {
            // Índices para las columnas
            val idColumnIndex = cursor.getColumnIndex(BaseColumns._ID)
            val documentoColumnIndex = cursor.getColumnIndex(MyDbHelper.COLUMN_DOCUMENTO)
            val nombreColumnIndex = cursor.getColumnIndex(MyDbHelper.COLUMN_NOMBRE)
            val clave1ColumnIndex = cursor.getColumnIndex(MyDbHelper.COLUMN_CLAVE1)
            val clave2ColumnIndex = cursor.getColumnIndex(MyDbHelper.COLUMN_CLAVE2)

            // Iterar a través de todos los registros retornados
            while (cursor.moveToNext()) {
                val currentID = cursor.getInt(idColumnIndex)
                val currentDocumento = cursor.getString(documentoColumnIndex)
                val currentNombre = cursor.getString(nombreColumnIndex)
                val currentClave1 = cursor.getString(clave1ColumnIndex)
                val currentClave2 = cursor.getString(clave2ColumnIndex)

                // Agregar fila por cada registro
                val row = TableRow(this).apply {
                    addView(TextView(context).apply { text = currentID.toString() })
                    addView(TextView(context).apply { text = currentDocumento })
                    addView(TextView(context).apply { text = currentNombre })
                    addView(TextView(context).apply { text = currentClave1 })
                    addView(TextView(context).apply { text = currentClave2 })
                }
                tableLayout.addView(row)
            }
        } finally {
            // Cerrar el cursor para liberar recursos
            cursor.close()
        }
    }
}
