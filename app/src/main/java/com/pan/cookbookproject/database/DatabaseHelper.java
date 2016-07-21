package com.pan.cookbookproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pan on 16/7/13.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * 数据库的版本
     */
    public static final int COOKBOOK_DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "cook_book";
    public static final String COOK_BOOK_TABLE_NAME = "cook_book_info";

    public static final String CREATE_TABLE = "CREATE TABLE "+COOK_BOOK_TABLE_NAME+" (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name varchar(50), " +
            "seasoning varchar(255)," +
            "method varchar(255)," +
            "remark varchar(100))";

    /**
     * 删除表的SQL语句
     */
    public static final String DROP_POI_INFO_TABLE = "DROP TABLE IF EXISTS " + COOK_BOOK_TABLE_NAME;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_POI_INFO_TABLE);
        onCreate(sqLiteDatabase);
    }
}
