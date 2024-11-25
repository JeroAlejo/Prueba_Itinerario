package ec.edu.epn.prueba

import android.app.DownloadManager.COLUMN_DESCRIPTION
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context,
    DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {

        const val DATABASE_NAME = "PuntosInteres.db"
        const val TABLE_NAME = "puntos_interes"
        const val DATABASE_VERSION = 1
        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "nombre_evento"
        const val COLUMN_PLACE = "lugar"
        const val COLUMN_DATE = "fecha"
        const val COLUMN_NUMBER = "asistentes"
        const val COLUMN_PHOTO = "foto"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
        CREATE TABLE $TABLE_NAME (
         $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
         $COLUMN_NAME TEXT NOT NULL,
         $COLUMN_PLACE TEXT NOT NULL,
         $COLUMN_DATE TEXT NOT NULL, 
         $COLUMN_NUMBER NUMBER NOT NULL,
         $COLUMN_PHOTO TEXT NOT NULL)
         """
        db.execSQL(createTable)
    }

    fun insertPuntoInteres(nombre_evento: String, lugar: String, fecha:
    String, asistentes : Int, foto : String ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, nombre_evento)
            put(COLUMN_PLACE, lugar)
            put(COLUMN_DATE, fecha)
            put(COLUMN_NUMBER, asistentes)
            put(COLUMN_PHOTO, foto)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllPuntosInteres(): Cursor {
        val db = this.readableDatabase
        val projection = arrayOf(COLUMN_NAME, COLUMN_PLACE,
            COLUMN_DATE, COLUMN_NUMBER, COLUMN_PHOTO)
        return db.query(TABLE_NAME, null, null, null, null, null, null)
    }


    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}