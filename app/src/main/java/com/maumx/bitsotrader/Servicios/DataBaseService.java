package com.maumx.bitsotrader.Servicios;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maumx.bitsotrader.DataBase.DaoOpenOrder;
import com.maumx.bitsotrader.BitsoEntities.Orden;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mauricio on 13/04/2017.
 */

public class DataBaseService extends IntentService {

    public  DataBaseService()
    {
        super("DataBase service");
    }


    @Override
    protected void onHandleIntent( Intent intent) {

        SQLiteOpenHelper dbHelper =null   ;

        String tipo= intent.getStringExtra("tipo");

        Intent resultado = new Intent("com.maumx.bitsotrader.BROADCAST_"+tipo.toUpperCase());
        String jsonValue="{}";
        try {


            List<Orden> posturas=  new DaoOpenOrder(getBaseContext()).ObtenerConjunto();

            Type ls= new TypeToken<ArrayList<Orden>>(){}.getType();


                   String json=  new Gson().toJson(   posturas,ls);



            resultado.putExtra("tipoOrdenBook","posibleventa");
            resultado.putExtra("com.maumx.bitsotrader.PAYLOAD",json);





        }catch (Exception ex)
        {

            Log.i("DB",ex.getMessage());

            intent.putExtra("error",ex.getMessage());

        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(resultado);


    }



}
