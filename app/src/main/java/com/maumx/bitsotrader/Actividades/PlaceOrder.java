package com.maumx.bitsotrader.Actividades;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maumx.bitsotrader.BitsoEntities.Balance;
import com.maumx.bitsotrader.Comun.Redondeo;
import com.maumx.bitsotrader.BitsoEntities.GranBalance;
import com.maumx.bitsotrader.BitsoEntities.Fee;
import com.maumx.bitsotrader.BitsoEntities.FeeValue;
import com.maumx.bitsotrader.R;
import com.maumx.bitsotrader.JsonResponse.ResultadoN;
import com.maumx.bitsotrader.JsonResponse.ResultadoP;
import com.maumx.bitsotrader.Servicios.DataBaseService;
import com.maumx.bitsotrader.Servicios.SolicitudBitso;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

public class PlaceOrder extends Activity {

    double mPrice;
    double mMonto;
    double mTotalMoneda;
    String mTipo;

    double mMajor;
    double    mMinor;
    private String mOrigenPosible;
    private String mInversionOriginal;

    private  FeeValue feeActual;
private  Boolean mEsOrdenPosibleCompreVenta;

    private BroadcastReceiver FeeReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String bitsoBook = SP.getString("bitsoBook", "eth_mxn");
            TextView lblComision= (TextView) findViewById(R.id.lblPlaceOrderPorcentajeComision);
            String statusCode = intent.getStringExtra("com.maumx.bitsotrader.STATUSCODE");
            String json = intent.getStringExtra("com.maumx.bitsotrader.PAYLOAD");


            Gson gson = new Gson();
            if (statusCode.equals("200")) {

                Type listType = new TypeToken<ResultadoP<Fee>>() {
                }.getType();
                ResultadoP<Fee> elemento = gson.fromJson(json, listType);
                Fee listaBalance = elemento.getResult();


                for (FeeValue comision : listaBalance.getFees()) {
                    if (comision.getBook().equals(bitsoBook)) {
                        feeActual=comision;
                        lblComision.setText(String.valueOf(feeActual.getFee_percent() * 100));
                    }

                }

            }


        }
    };

    private BroadcastReceiver RecibidorSaldoLibro = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
          String   mLibroDeseado= SP.getString("bitsoBook", "eth_mxn");
            EditText txtMonto = (EditText) findViewById(R.id.txtPlaceOrderMonto);
            ToggleButton tb= (ToggleButton)findViewById(R.id.tbnTipoMoneda);


            String valor = String.valueOf(intent.getStringExtra("com.maumx.bitsotrader.BALANCE"));
            Gson gson = new Gson();
            Type listType = new TypeToken<ResultadoP<GranBalance>>() {
            }.getType();
            ResultadoP<GranBalance> elemento = gson.fromJson(valor, listType);
            List<Balance> listaBalance = elemento.getResult().getBalances();

            Balance pesos, criptoMoneda;

            pesos = null;
            criptoMoneda = null;
            for (Balance balance : listaBalance) {
                if (balance.getCurrency().equals("mxn")) {
                    pesos = balance;


                }

                if (  String.format("%s_mxn",balance.getCurrency()).equals(mLibroDeseado)) {
                    criptoMoneda = balance;


                }




            }

            if (! tb.isChecked()  ) {
                txtMonto.setText(String.format("%s", pesos.getAvailable().setScale(2, BigDecimal.ROUND_FLOOR).toString()));
            }
            if ( tb.isChecked()  )
            {
                txtMonto.setText(String.format("%s",    criptoMoneda.getAvailable().setScale(8,BigDecimal.ROUND_CEILING).toString()));
            }


        }
    };


    private BroadcastReceiver RecibidorEstado = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String valor =intent.getStringExtra("com.maumx.bitsotrader.PAYLOAD");
            String code =intent.getStringExtra("com.maumx.bitsotrader.STATUSCODE");
            TextView lbl = (TextView)findViewById(R.id.lblEstadoCancelacion);


            Gson gson= new Gson();

            if ( code.equals("200"))
            {

                Toast.makeText(context, "Orden colocada correctamente." , Toast.LENGTH_SHORT).show();

                Intent solicitudBD= new Intent(getBaseContext() ,DataBaseService.class);

                solicitudBD.putExtra("tabla","OpenOrder");
                solicitudBD.putExtra("tipo","insertar");

                startService(solicitudBD);



            }
            else
                {
                    Type listType = new TypeToken<ResultadoN>(){}.getType();
                    ResultadoN elemento=   gson.fromJson(valor,listType);

                    Toast.makeText(context, elemento.getError().getmMessage() , Toast.LENGTH_SHORT).show();

                }






            finish();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        CambiarLibro();


        mEsOrdenPosibleCompreVenta=false;


        IntentFilter feeIntentFilter = new IntentFilter(
                "com.maumx.bitsotrader.BROADCAST_FEE");
        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                FeeReciver,
                feeIntentFilter);

        Intent feeIntent = new Intent(this, SolicitudBitso.class);
        feeIntent.setData(Uri.parse(("x")));
        feeIntent.putExtra("tipo", "fee");
        startService(feeIntent);



        mTotalMoneda=0.0f;

        mTipo = getIntent().getStringExtra("tipo");
  TextView lblTipo= (TextView) findViewById(R.id.lblPlaceOrderTipo);

        ToggleButton tb= (ToggleButton)findViewById(R.id.tbnTipoMoneda);
        EditText txtMonto = (EditText) findViewById(R.id.txtPlaceOrderMonto);

        EditText txtPrecioOriginal = (EditText) findViewById(R.id.txtPlaceOrderPrecioOriginal);
        EditText txtGananciaPerdida = (EditText) findViewById(R.id.txtPlaceOrderPrecioOriginal);


        txtPrecioOriginal.setEnabled(false);
        txtGananciaPerdida.setEnabled(false);


        if (  mTipo.equals("venta")  )
        {
            tb.setChecked(true);
        }

        if (  mTipo.equals("compra")  )
        {
            tb.setChecked(false);
            txtMonto.setHint("MXN");
        }

        lblTipo.setText(mTipo);




        IntentFilter statusIntentFilter = new IntentFilter(
                "com.maumx.bitsotrader.BROADCAST_PLACEORDER_" + mTipo.toUpperCase());



        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                RecibidorEstado,
                statusIntentFilter);





        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String bitsoBook = SP.getString("bitsoBook", "eth_mxn");
                String bookReducido = null;
                if (bitsoBook.equals("eth_mxn")) {
                    bookReducido = "ETH";
                } else {
                    bookReducido = "BTC";
                }

                ToggleButton tb = (ToggleButton)buttonView;
                EditText txtMonto = (EditText) findViewById(R.id.txtPlaceOrderMonto);
                if( tb.isChecked())
                {
                    txtMonto.setHint(bookReducido);


                }else
                {
                    txtMonto.setHint("MXN");
                }

            }
        });

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String bitsoBook = SP.getString("bitsoBook", "eth_mxn");
        String bookReducido = null;
        if (bitsoBook.equals("eth_mxn")) {
            bookReducido = "ETH";
        } else {
            bookReducido = "BTC";
        }
        EditText txtPrecio = (EditText) findViewById(R.id.txtPlaceOrderPrecio);
        if(getIntent().hasExtra("origen") && getIntent().getStringExtra ("origen").equals("posibleventa"))
        {
            mOrigenPosible=getIntent().getStringExtra ("origen");
            mEsOrdenPosibleCompreVenta=true;
            //txtPrecioOriginal.setEnabled(true);
            //txtGananciaPerdida.setEnabled(true);
            txtMonto.setText( getIntent().getStringExtra ("cantidad").replace(bookReducido,"") );
            txtPrecioOriginal.setText( getIntent().getStringExtra ("precio").replace("MXN","") );

            mInversionOriginal=  getIntent().getStringExtra ("original_value").replace("MXN","");
        }


    }




    public  void CaluclarOrden(View v)
    {

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String bitsoBook = SP.getString("bitsoBook", "eth_mxn").split("_")[0];


        ToggleButton tb = (ToggleButton) findViewById(R.id.tbnTipoMoneda);
        EditText txtMonto = (EditText) findViewById(R.id.txtPlaceOrderMonto);
        EditText txtPrecio =(EditText) findViewById(R.id.txtPlaceOrderPrecio);
        TextView lblTotal= (TextView) findViewById(R.id.lblPlaceOrderTotalMoneda);
        EditText txtPrecioOriginal = (EditText) findViewById(R.id.txtPlaceOrderPrecioOriginal);
        EditText txtGananciaPerdida = (EditText) findViewById(R.id.txtPlaceOrderGananciaPerdida);



        double totalMoneda=0.0f;
         double  monto= Double.parseDouble ( txtMonto.getText().toString());
        double precio= Double.parseDouble ( txtPrecio.getText().toString());



        mPrice=precio;
        //Calcular con pesos
        if (!tb.isChecked())
        {
            mTotalMoneda= monto/precio;
            mMinor=monto;
            mMajor=Redondeo.Redondeo(mTotalMoneda,bitsoBook).doubleValue();

            lblTotal.setText(Redondeo.Redondeo(mTotalMoneda,bitsoBook).toString() + " ETH");
        }
        else
            { //Calcular con ethers

                mTotalMoneda= monto*precio;
                lblTotal.setText(Redondeo.Redondeo(mTotalMoneda,"mxn").toString() + " MXN");
                mMajor=Redondeo.Redondeo(monto,bitsoBook).doubleValue();
                mMinor=0.0f;



                if (   mEsOrdenPosibleCompreVenta ) {

                    if (txtPrecio.getText() == null) {
                        Toast.makeText(this, "Ingrese un precio de venta ", Toast.LENGTH_SHORT).show();

                    }

double comisionVenta=  (monto * precio)*  feeActual.getFee_decimal();
                    double g = (monto * precio)-  Double.valueOf(mInversionOriginal) - comisionVenta;
                   BigDecimal gananciaPerdida=  Redondeo.Redondeo(g,"mxn");

                    txtGananciaPerdida.setText(gananciaPerdida.setScale(2,BigDecimal.ROUND_CEILING).toString());
                }


            }





    }


public void EnviarCompra_Onclick(View v)
{

if (    mTotalMoneda==0.0f)
{
    Toast.makeText(this, "Calcule el monto antes de envíar la orden", Toast.LENGTH_SHORT).show();
}else {

    Intent solicitudEnvio = new Intent(this, SolicitudBitso.class);
    solicitudEnvio.putExtra("tipo", "placeorder_" + mTipo.toLowerCase());
    solicitudEnvio.putExtra("book", "eth_mxn");
    solicitudEnvio.putExtra("type", "limit");

    if (mTipo.toLowerCase().equals("compra")) {
        solicitudEnvio.putExtra("side", "buy");
    } else {
        solicitudEnvio.putExtra("side", "sell");
    }
    if (mMajor != 0.0f) {
        solicitudEnvio.putExtra("major", String.valueOf(mMajor));
    }

    solicitudEnvio.putExtra("price", String.valueOf(mPrice));
    startService(solicitudEnvio);
    Button btn = (Button) findViewById(R.id.btnEnviarOrden);
    btn.setCursorVisible(true);


}

}


    private void CambiarLibro() {

        ToggleButton tb = (ToggleButton) findViewById(R.id.tbnTipoMoneda);
        EditText txtMonto = (EditText) findViewById(R.id.txtPlaceOrderMonto);
        EditText txtPrecio =(EditText) findViewById(R.id.txtPlaceOrderPrecio);
        TextView lblTotal= (TextView) findViewById(R.id.lblPlaceOrderTotalMoneda);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String bitsoBook = SP.getString("bitsoBook", "eth_mxn");
        String bookReducido = null;
        if (bitsoBook.equals("eth_mxn")) {
            bookReducido = "ETH";
        } else {
            bookReducido = "BTC";
        }

        tb.setTextOn(bookReducido);


    }


    public void btnMontoDisponible_onClick(View v)
    {


        Intent intentMontoDisponible= new Intent(this, SolicitudBitso.class);
        intentMontoDisponible.putExtra("tipo","balance");
        startService(intentMontoDisponible);


        IntentFilter statusIntentFilter = new IntentFilter(
                "com.maumx.bitsotrader.BROADCAST_ACTION");

        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                RecibidorSaldoLibro,
                statusIntentFilter);



    }

    @Override
    protected void onDestroy() {
        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                RecibidorSaldoLibro);


        super.onDestroy();

    }
}
