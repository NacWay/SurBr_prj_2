package com.tamplate

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBase(val context: Context) :
    SQLiteOpenHelper(context, db_name, null, db_ver) {

    companion object {
        private const val db_name = "myDb"
        private const val db_ver = 1
        private const val db_table = "history"
        private const val db_columnWin = "win"
        private const val db_columnIcon1 = "icon1"
        private const val db_columnIcon2 = "icon2"
        private const val db_columnIcon3 = "icon3"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val query = String.format(
            "CREATE TABLE %s (%s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL);",
            db_table,
            db_columnWin,
            db_columnIcon1,
            db_columnIcon2,
            db_columnIcon3
        )
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val query = String.format("DELETE TABLE IF EXISTS %s", db_table)
        db.execSQL(query)
        onCreate(db)
    }

    fun insertData(win: String?, icon1: String?, icon2: String?, icon3: String?) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(db_columnWin, win)
        values.put(db_columnIcon1, icon1)
        values.put(db_columnIcon2, icon2)
        values.put(db_columnIcon3, icon3)
        db.insertWithOnConflict(
            db_table,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        ) //Добавляем запись в таблицу
        db.close()
    }

    fun deleteData(win: String?, icon1: String?, icon2: String?, icon3: String?) {
        val db = this.writableDatabase
        db.delete(db_table, db_columnWin + " = ?", arrayOf(win))
        db.delete(db_table, db_columnIcon1 + " = ?", arrayOf(icon1))
        db.delete(db_table, db_columnIcon2 + " = ?", arrayOf(icon2))
        db.delete(db_table, db_columnIcon3 + " = ?", arrayOf(icon3))
        db.close()
    }

    val getWin: ArrayList<String>
        get() {
            val winList = ArrayList<String>()
            val db = this.readableDatabase
            val cursor = db.query(db_table, arrayOf(db_columnWin), null, null, null, null, null)
            while (cursor.moveToNext()) {
                val index = cursor.getColumnIndex(db_columnWin)
                winList.add(cursor.getString(index))
            }
            cursor.close()
            db.close()
            return winList
        }

    val getIcon1: ArrayList<String>
        get() {
            val icon1List = ArrayList<String>()
            val db = this.readableDatabase
            val cursor = db.query(db_table, arrayOf(db_columnIcon1), null, null, null, null, null)
            //переводи полученные данные в список
            while (cursor.moveToNext()) {
                val index = cursor.getColumnIndex(db_columnIcon1)
                icon1List.add(cursor.getString(index))
            }
            cursor.close()
            db.close()
            return icon1List
        }

    val getIcon2: ArrayList<String>
        get() {
            val icon2List = ArrayList<String>()
            val db = this.readableDatabase

            val cursor = db.query(db_table, arrayOf(db_columnIcon2), null, null, null, null, null)

            while (cursor.moveToNext()) {
                val index = cursor.getColumnIndex(db_columnIcon2)
                icon2List.add(cursor.getString(index))
            }
            cursor.close()
            db.close()
            return icon2List
        }

    val getIcon3: ArrayList<String>
        get() {
            val icon3List = ArrayList<String>()
            val db = this.readableDatabase

            val cursor = db.query(db_table, arrayOf(db_columnIcon3), null, null, null, null, null)
            //переводи полученные данные в список
            while (cursor.moveToNext()) {
                val index = cursor.getColumnIndex(db_columnIcon3)
                icon3List.add(cursor.getString(index))
            }
            cursor.close()
            db.close()
            return icon3List
        }

}