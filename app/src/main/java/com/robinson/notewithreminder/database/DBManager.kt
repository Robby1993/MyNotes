package com.robinson.notewithreminder.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DBManager(context: Context) {
    private val sqLiteDatabase: SQLiteDatabase

    internal class DatabaseHelperUser(var context: Context) : SQLiteOpenHelper(
        context, databaseName, null, DBVersion
    ) {
        override fun onCreate(db: SQLiteDatabase) {
            Toast.makeText(context, "Keep Notes", Toast.LENGTH_LONG).show()
            db.execSQL(CreateTable)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("Drop table IF EXISTS " + TableName)
            onCreate(db)
        }
    }

    init {
        val db = DatabaseHelperUser(context)
        sqLiteDatabase = db.writableDatabase
    }

    fun insertReminder(values: ContentValues?): Long {
        return sqLiteDatabase.insert(TableName, "", values)
    }

    // In sql data in the form of table is handled using Cursor
    // Projection no. means no. of columns, Selection is selecting a specific column
    fun query(
        Projection: Array<String?>?,
        Selection: String?,
        SelectionArgs: Array<String?>?,
        SortOrder: String?
    ): Cursor {
        val qb = SQLiteQueryBuilder()
        qb.tables = TableName
        return qb.query(sqLiteDatabase, Projection, Selection, SelectionArgs, null, null, SortOrder)
    }

    fun deleteReminder(Selection: String?, SelectionArgs: Array<String?>?): Int {
        return sqLiteDatabase.delete(TableName, Selection, SelectionArgs)
    }

    fun updateReminder(
        values: ContentValues?,
        Selection: String?,
        SelectionArgs: Array<String?>?
    ): Int {
        return sqLiteDatabase.update(TableName, values, Selection, SelectionArgs)
    }

    fun RowCount(): Long {
        return DatabaseUtils.queryNumEntries(sqLiteDatabase, TableName)
    }

    companion object {
        const val databaseName = "Keep_Notes"
        const val TableName = "Notes"
        const val ColDateTime = "DateTime"
        const val ColTitle = "Title"
        const val ColDescription = "Description"
        const val ColRemTime = "Time"
        const val ColRemDate = "Date"
        const val ColID = "ID"
        const val DBVersion = 1
        const val CreateTable =
            "Create table IF NOT EXISTS " + TableName + "(ID integer primary key autoincrement," + ColDateTime +
                    " text," + ColTitle + " text," + ColDescription + " text," + ColRemTime + " text," + ColRemDate + " text);"
    }
}