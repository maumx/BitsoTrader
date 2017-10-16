package com.maumx.bitsotrader.DataBase;

import com.maumx.bitsotrader.DataBase.OpenOrderContract;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Mauricio on 13/04/2017.
 */


public class OpenOrderReaderDbHelper extends SQLiteOpenHelper {


    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "PruebaOpenOrder.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + OpenOrderContract.OpenOrderEntry.TABLE_NAME + " (" +
                    OpenOrderContract.OpenOrderEntry._ID + " INTEGER PRIMARY KEY," +
                    OpenOrderContract.OpenOrderEntry.COLUMN_NAME_OID + TEXT_TYPE + COMMA_SEP +
                    OpenOrderContract.OpenOrderEntry.COLUMN_NAME_PRICE + TEXT_TYPE + COMMA_SEP +
                    OpenOrderContract.OpenOrderEntry.COLUMN_NAME_SIDE + TEXT_TYPE + COMMA_SEP +
                    OpenOrderContract.OpenOrderEntry.COLUMN_NAME_ORIGINAL_AMOUNT + TEXT_TYPE + COMMA_SEP +
                    OpenOrderContract.OpenOrderEntry.COLUMN_NAME_ORIGINAL_VALUE + TEXT_TYPE + COMMA_SEP +
                    OpenOrderContract.OpenOrderEntry.COLUMN_NAME_UNFILLED_AMOUNT + TEXT_TYPE + COMMA_SEP +
                    OpenOrderContract.OpenOrderEntry.COLUMN_NAME_STATUS + TEXT_TYPE + COMMA_SEP +
                    OpenOrderContract.OpenOrderEntry.COLUMN_NAME_BOOK + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + OpenOrderContract.OpenOrderEntry.TABLE_NAME;

    public OpenOrderReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }






    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
