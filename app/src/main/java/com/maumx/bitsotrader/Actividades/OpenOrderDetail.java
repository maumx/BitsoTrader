package com.maumx.bitsotrader.Actividades;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maumx.bitsotrader.R;
import com.maumx.bitsotrader.JsonResponse.ResultadoP;
import com.maumx.bitsotrader.Servicios.SolicitudBitso;

import java.lang.reflect.Type;
import java.util.List;

public class OpenOrderDetail extends Activity {


    private  String  oid;

    private BroadcastReceiver RecibidorEstado = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String valor =intent.getStringExtra("com.maumx.bitsotrader.PAYLOAD");

            Gson gson= new Gson();
            Type listType = new TypeToken<ResultadoP<List<String >>>(){}.getType();
            ResultadoP<List<String >> elemento=   gson.fromJson(valor,listType);
            List<String> resultado=  elemento.getResult();


            TextView lbl = (TextView)findViewById(R.id.lblEstadoCancelacion);

            if (    elemento.getSuccess()==true)
            {
                lbl.setText("Eliminación correcta");
                finish();

            }else
                {
                    lbl.setText("Ocurrio un problema en la eliminación");
                }



        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_open_order_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);


            TextView txtOpenOrderId=  (TextView) findViewById(R.id.txtOpenOrderId);

        oid=getIntent().getStringExtra("id");

             txtOpenOrderId.setText( oid);
        IntentFilter statusIntentFilter = new IntentFilter(
                "com.maumx.bitsotrader.BROADCAST_OPENORDER_CANCELACION");
        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                RecibidorEstado,
                statusIntentFilter);
    }



    public  void btn_CancelarOpenOrder(View v)
    {
 TextView lblEstadoCancelacion = (TextView) findViewById(R.id.lblEstadoCancelacion);

        lblEstadoCancelacion.setText("cancelando orden");

        Intent solicitud = new Intent( this, SolicitudBitso.class);

        solicitud.putExtra("tipo","openorder_cancelacion");
        solicitud.putExtra("oid",oid);


        startService(solicitud);
    }

}
