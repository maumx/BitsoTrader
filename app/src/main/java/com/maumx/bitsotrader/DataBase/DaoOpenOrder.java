package com.maumx.bitsotrader.DataBase;

/**
 * Created by Mauricio on 13/04/2017.
 */

import com.maumx.bitsotrader.BitsoEntities.Orden;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;


public class DaoOpenOrder {

    public  DaoOpenOrder(Context contexto)
    {
     this.contexto=contexto;
        dbConexion= new OpenOrderReaderDbHelper(contexto);
    }
   private Context  contexto;
private SQLiteOpenHelper dbConexion ;
public void Insertar(Orden orden)
{

    SQLiteDatabase db= dbConexion.getWritableDatabase();
    ContentValues values = new ContentValues();

try {


    values.put(OpenOrderContract.OpenOrderEntry.COLUMN_NAME_OID,  orden.getOid());
    values.put(OpenOrderContract.OpenOrderEntry.COLUMN_NAME_BOOK, orden.getBook());
    values.put(OpenOrderContract.OpenOrderEntry.COLUMN_NAME_SIDE, orden.getSide());
    values.put(OpenOrderContract.OpenOrderEntry.COLUMN_NAME_PRICE, orden.getPrice().setScale(2, BigDecimal.ROUND_UNNECESSARY).toString());
    values.put(OpenOrderContract.OpenOrderEntry.COLUMN_NAME_STATUS, orden.getStatus());
    values.put(OpenOrderContract.OpenOrderEntry.COLUMN_NAME_ORIGINAL_AMOUNT, orden.getOriginal_amount ().setScale(8, BigDecimal.ROUND_FLOOR).toString());
    values.put(OpenOrderContract.OpenOrderEntry.COLUMN_NAME_ORIGINAL_VALUE, orden.getOriginal_value ().setScale(2, BigDecimal.ROUND_CEILING).toString());
    values.put(OpenOrderContract.OpenOrderEntry.COLUMN_NAME_UNFILLED_AMOUNT, orden.getUnfilled_amount ().setScale(8, BigDecimal.ROUND_FLOOR).toString());
    db.insert(OpenOrderContract.OpenOrderEntry.TABLE_NAME,null,values);
}catch (Exception ex)
{
    Log.d("DB",ex.getMessage(),ex.fillInStackTrace());

}

}





private  ArrayList<Orden>  OnObtenerConjunto()
{
    ArrayList<Orden> lista = new ArrayList<Orden>();
    SQLiteDatabase db= dbConexion.getReadableDatabase();
    String[] projection = {
            OpenOrderContract.OpenOrderEntry._ID,
            OpenOrderContract.OpenOrderEntry.COLUMN_NAME_OID,
            OpenOrderContract.OpenOrderEntry.COLUMN_NAME_PRICE,
            OpenOrderContract.OpenOrderEntry.COLUMN_NAME_STATUS,
            OpenOrderContract.OpenOrderEntry.COLUMN_NAME_UNFILLED_AMOUNT,
            OpenOrderContract.OpenOrderEntry.COLUMN_NAME_ORIGINAL_AMOUNT,
            OpenOrderContract.OpenOrderEntry.COLUMN_NAME_ORIGINAL_VALUE,
    };

    Cursor c = db.query(
            OpenOrderContract.OpenOrderEntry.TABLE_NAME,                     // The table to query
            projection,                               // The columns to return
            null,                                // The columns for the WHERE clause
            null,                            // The values for the WHERE clause
            null,                                     // don't group the rows
            null,                                     // don't filter by row groups
            null                                 // The sort order
    );


    while ( c.moveToNext())
    {
        Orden elemento= new Orden();
        elemento.setOid(c.getString(  c.getColumnIndexOrThrow(  OpenOrderContract.OpenOrderEntry.COLUMN_NAME_OID)));
        elemento.setPrice(   BigDecimal.valueOf( Double.parseDouble(  c.getString(   c.getColumnIndexOrThrow(  OpenOrderContract.OpenOrderEntry.COLUMN_NAME_PRICE)))));
        elemento.setOriginal_value(   BigDecimal.valueOf( Double.parseDouble(  c.getString(   c.getColumnIndexOrThrow(  OpenOrderContract.OpenOrderEntry.COLUMN_NAME_ORIGINAL_VALUE)))));
        elemento.setOriginal_amount(   BigDecimal.valueOf( Double.parseDouble(  c.getString(   c.getColumnIndexOrThrow(  OpenOrderContract.OpenOrderEntry.COLUMN_NAME_ORIGINAL_AMOUNT)))));
        elemento.setUnfilled_amount(   BigDecimal.valueOf( Double.parseDouble(  c.getString(   c.getColumnIndexOrThrow(  OpenOrderContract.OpenOrderEntry.COLUMN_NAME_UNFILLED_AMOUNT)))));
        lista.add(elemento);
    }

    return lista;
}


public ArrayList<Orden> ObtenerConjunto()
    {
        return OnObtenerConjunto();
    }

public Orden Obtener()
{
return ObtenerConjunto().get(0);
}







}







