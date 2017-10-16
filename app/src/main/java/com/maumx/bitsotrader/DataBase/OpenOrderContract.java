package com.maumx.bitsotrader.DataBase;

import android.provider.BaseColumns;

import java.lang.reflect.Constructor;

/**
 * Created by Mauricio on 13/04/2017.
 */

public class OpenOrderContract
{
    private OpenOrderContract()
    {
    }

    public static class OpenOrderEntry implements  BaseColumns
    {
        public static final  String TABLE_NAME="OpenOrder";
        public static final  String COLUMN_NAME_OID="oid";
        public static final  String COLUMN_NAME_BOOK="book";
        public static final  String COLUMN_NAME_SIDE="side";
        public static final  String COLUMN_NAME_PRICE="price";
        public static final  String COLUMN_NAME_STATUS="status";
        public  static  final  String COLUMN_NAME_ORIGINAL_AMOUNT = "originalAmount";
        public  static  final  String COLUMN_NAME_ORIGINAL_VALUE = "originalValue";
        public  static  final  String COLUMN_NAME_UNFILLED_AMOUNT = "unfilledAmount";
    }






}
