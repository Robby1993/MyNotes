package com.robinson.notewithreminder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.Toast;

public class DBManager {
    private final SQLiteDatabase sqLiteDatabase;
    static final String databaseName = "Keep_Notes";
    public static final String TableName = "Notes";
    public static final String ColDateTime = "DateTime";
    public static final String ColTitle = "Title";
    public static final String ColDescription = "Description";
    public static final String ColRemTime = "Time";
    public  static final String ColRemDate = "Date";
    public static final String ColID = "ID";
    static final int DBVersion = 1;
    static final String CreateTable = "Create table IF NOT EXISTS " + TableName + "(ID integer primary key autoincrement," + ColDateTime +
            " text," + ColTitle + " text," + ColDescription + " text," + ColRemTime + " text," + ColRemDate + " text);";

    static class DatabaseHelperUser extends SQLiteOpenHelper {

        Context context;

        DatabaseHelperUser(Context context) {
            super(context, databaseName, null, DBVersion);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Toast.makeText(context, "Keep Notes", Toast.LENGTH_LONG).show();
            db.execSQL(CreateTable);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("Drop table IF EXISTS " + TableName);
            onCreate(db);
        }
    }

    public DBManager(Context context) {
        DatabaseHelperUser db = new DatabaseHelperUser(context);
        sqLiteDatabase = db.getWritableDatabase();
    }

    public long insertReminder(ContentValues values) {
        return sqLiteDatabase.insert(TableName, "", values);
    }

    // In sql data in the form of table is handled using Cursor
    // Projection no. means no. of columns, Selection is selecting a specific column
    public Cursor query(String[] Projection, String Selection, String[] SelectionArgs, String SortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TableName);
        return qb.query(sqLiteDatabase, Projection, Selection, SelectionArgs, null, null, SortOrder);
    }

    public int deleteReminder(String Selection, String[] SelectionArgs) {
        return sqLiteDatabase.delete(TableName, Selection, SelectionArgs);
    }

    public int updateReminder(ContentValues values, String Selection, String[] SelectionArgs) {
        return sqLiteDatabase.update(TableName, values, Selection, SelectionArgs);
    }

    public long RowCount() {
        return DatabaseUtils.queryNumEntries(sqLiteDatabase, TableName);
    }
}
