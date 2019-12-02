package com.quickstart.androidform.repositories.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.quickstart.androidform.utils.ConstantKey;

public class SQLiteDAO {

    private static SQLiteDAO mSqLiteDAO;
    private static SQLiteDatabase mDatabase;

    public static synchronized SQLiteDAO getInstance(Context context) {
        if (mSqLiteDAO == null) {
            mSqLiteDAO = new SQLiteDAO();
            mDatabase = helperObject(context);
        }
        return mSqLiteDAO;
    }

    private static SQLiteDatabase helperObject(Context context) {
        SQLiteOpenHelper mHelper = new SQLiteOpenHelper(context, ConstantKey.DATABASE_NAME, null, ConstantKey.DATABASE_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(ConstantKey.CREATE_USERS_TABLE);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL(ConstantKey.DROP_USERS_TABLE);
            }
        };
        return mHelper.getWritableDatabase();
    }

    public long addData(String tableName, ContentValues values) {
        long data = this.mDatabase.insert(tableName, null, values);
        //this.database.close();
        return data;
    }

    public long deleteById(String tableName, final String id) {
        long data = this.mDatabase.delete(tableName, id, null);
        //this.database.close();
        return data;
    }

    public long deleteDataById(String tableName, final String id) {
        long data = this.mDatabase.delete(tableName, ConstantKey.COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
        //this.database.close();
        return data;
    }

    public Cursor getAllData(String query) {
        final Cursor cursor = this.mDatabase.rawQuery(query,null);
        //this.database.close();
        return cursor;
    }

    public long updateById(String tableName, ContentValues values, String id) {
        long data = this.mDatabase.update(tableName, values, ConstantKey.COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
        //this.database.close();
        return data;
    }

    public Cursor getDataByUserPass(String userName, String passWord) {
        Cursor cursor = this.mDatabase.rawQuery("SELECT * FROM " + ConstantKey.USERS_TABLE_NAME + " WHERE " + ConstantKey.USERS_COLUMN6 + "=? AND " + ConstantKey.USERS_COLUMN7 + "=?", new String[] {userName, passWord});
        //SELECT username, password FROM login_table WHERE username=userName AND password=passWord
        //final Cursor cursor = this.database.query(ConstantKey.LOGIN_TABLE_NAME, new String[]{"id, username, password"}, "username=? AND password=?", new String[]{userName, passWord}, null, null, null);
        //this.database.close();
        return cursor;
    }
}
