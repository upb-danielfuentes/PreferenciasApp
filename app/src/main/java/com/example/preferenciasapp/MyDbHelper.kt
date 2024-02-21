package com.example.preferenciasapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class MyDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Usuarios.db"
        const val TABLE_NAME = "usuarios"
        const val COLUMN_DOCUMENTO = "documento"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_CLAVE1 = "clave1"
        const val COLUMN_CLAVE2 = "clave2"
    }

    private val SQL_CREATE_ENTRIES = """
        CREATE TABLE $TABLE_NAME (
        ${BaseColumns._ID} INTEGER PRIMARY KEY,
        $COLUMN_DOCUMENTO TEXT,
        $COLUMN_NOMBRE TEXT,
        $COLUMN_CLAVE1 TEXT,
        $COLUMN_CLAVE2 TEXT)
    """.trimIndent()

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }
}
