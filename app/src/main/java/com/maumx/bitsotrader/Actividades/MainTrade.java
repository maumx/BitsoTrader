package com.maumx.bitsotrader.Actividades;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maumx.bitsotrader.BitsoEntities.Balance;
import com.maumx.bitsotrader.BitsoEntities.Fondeo;
import com.maumx.bitsotrader.BitsoEntities.GranBalance;
import com.maumx.bitsotrader.BitsoEntities.Fee;
import com.maumx.bitsotrader.BitsoEntities.FeeValue;
import com.maumx.bitsotrader.BitsoEntities.Retiro;
import com.maumx.bitsotrader.JsonResponse.GeneralResponse;
import com.maumx.bitsotrader.R;
import com.maumx.bitsotrader.JsonResponse.ResultadoP;
import com.maumx.bitsotrader.Servicios.DataBaseService;
import com.maumx.bitsotrader.Servicios.SolicitudBitso;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.w3c.dom.Text;


public class MainTrade extends Activity {


    private BroadcastReceiver RecibidorBD = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            if (intent.hasExtra("error"))
            {
                Toast.makeText(context, intent.getStringExtra("error"), Toast.LENGTH_SHORT).show();
            }

            if (intent.hasExtra("oid"))
            {
                Toast.makeText(context, intent.getStringExtra("oid"), Toast.LENGTH_SHORT).show();
            }

        }
    };


    private BroadcastReceiver RecibidorTotalTransacciones = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {



            String valor = String.valueOf(intent.getDoubleExtra("com.maumx.bitsotrader.PAYLOAD",0.0d));

            TextView volumen= (TextView) findViewById( R.id.lblMainVolumen );

            volumen.setText( valor );




        }
    };

    private BroadcastReceiver RecibidorEstado = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String bitsoBook = SP.getString("bitsoBook", "eth_mxn");





            TextView txtSaldoMxn = (TextView) findViewById(R.id.txtSaldoMxn);
            TextView txtSaldoCripto = (TextView) findViewById(R.id.txtSaldoCripto);


            TextView txtComprometidoMxn = (TextView) findViewById(R.id.txtComprometidoMXN);
            TextView txtComprometidoCripto = (TextView) findViewById(R.id.txtComprometidoCripto);


            ///llave con la que se guardó JSON
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

                if (  String.format("%s_mxn",balance.getCurrency()).equals(bitsoBook)) {
                    criptoMoneda = balance;


                }


            }


            txtSaldoMxn.setText(pesos.getAvailable().setScale(2, BigDecimal.ROUND_DOWN).toString() );
            txtSaldoCripto.setText(criptoMoneda.getAvailable().setScale(8,BigDecimal.ROUND_UNNECESSARY).toString());
            txtComprometidoMxn.setText(pesos.getLocked().setScale(8,BigDecimal.ROUND_DOWN).toString());
            txtComprometidoCripto.setText(criptoMoneda.getLocked().setScale(8,BigDecimal.ROUND_UNNECESSARY).toString());


        }
    };


    private BroadcastReceiver FeeReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String bitsoBook = SP.getString("bitsoBook", "eth_mxn");
            TextView lblComision = (TextView) findViewById(R.id.lblMainComision);


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
                        lblComision.setText(String.valueOf(comision.getFee_percent() * 100));
                    }

                }

            }


        }
    };



    private BroadcastReceiver RecibidorGanancia = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String valor = String.valueOf(intent.getStringExtra("com.maumx.bitsotrader.PAYLOAD"));

            String[] valores = valor.split("\\|");


            Type tipo = new TypeToken<List<Fondeo>>() {
            }.getType();

            List<Fondeo> listF = new Gson().fromJson(valores[0], tipo);

            tipo = new TypeToken<List<Retiro>>() {
            }.getType();

            List<Retiro> listR = new Gson().fromJson(valores[1], tipo);


            double sumaFondeos = 0, sumaRetiros = 0, gananciaRelativa = 0, inversion = 0;

            for (Fondeo f : listF) {
                if (f.getMethod().equals("sp")) {
                    sumaFondeos += f.getAmount().setScale(2, BigDecimal.ROUND_CEILING).doubleValue();
                }
            }

            for (Retiro r : listR) {
                if (r.getMethod().equals("sp")) {
                    sumaRetiros += r.getAmount().setScale(2, BigDecimal.ROUND_CEILING).doubleValue();
                }
            }

                inversion = sumaFondeos - sumaRetiros;

                TextView txtSaldoMXN = (TextView) findViewById(R.id.txtSaldoMxn);
                TextView txtFondos = (TextView) findViewById(R.id.lblMainFondeos);
                TextView txtRetiros = (TextView) findViewById(R.id.lblMainRetiros);
                TextView txtInversion = (TextView) findViewById(R.id.lblMainInvertido);
                TextView txtGanancia = (TextView) findViewById(R.id.lblMainGananciaRelativa);


                gananciaRelativa =   Double.valueOf(txtSaldoMXN.getText().toString())  - inversion;

txtFondos.setText(String.format("%,.2f",  sumaFondeos));
                txtRetiros.setText(String.format("%,.2f",  sumaRetiros));
                txtInversion.setText(String.format("%,.2f",  inversion));
                txtGanancia.setText(String.format("%,.2f", gananciaRelativa));


        }


    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_trade);
        CambiarLibro();

        TabHost host = (TabHost) findViewById(R.id.MainTabHost);
        host.setup();


        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Saldo");
        spec.setContent(R.id.mainTabSaldo);
        spec.setIndicator("Saldo");
        host.addTab(spec);


        //Tab 1
        spec = host.newTabSpec("Compra");
        spec.setContent(R.id.mainTabCompra);
        spec.setIndicator("Compra");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Venta");
        spec.setContent(R.id.mainTabVenta);
        spec.setIndicator("Venta");
        host.addTab(spec);


        IntentFilter statusIntentFilter = new IntentFilter(
                "com.maumx.bitsotrader.BROADCAST_ACTION");


        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                RecibidorEstado,
                statusIntentFilter);


        IntentFilter feeIntentFilter = new IntentFilter(
                "com.maumx.bitsotrader.BROADCAST_FEE");


        IntentFilter dbServiceIntentFilter = new IntentFilter(
                "com.maumx.bitsotrader.DBSERVICE");


        IntentFilter TotalTransaccionesIntentFilter = new IntentFilter(
                "com.maumx.bitsotrader.BROADCAST_VOLUMENTRANSACCION");

        IntentFilter TotalGanancia = new IntentFilter(
                "com.maumx.bitsotrader.BROADCAST_CALCULOGANANCIA");


        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                FeeReciver,
                feeIntentFilter);

        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                RecibidorBD,
                dbServiceIntentFilter);
        ;
        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                RecibidorTotalTransacciones,
                TotalTransaccionesIntentFilter);
        ;

        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                RecibidorGanancia,
                TotalGanancia);
        ;
        Fragment fragmento = new Fragment();
        fragmento = new ListaPosturaFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Bundle arg1 = new Bundle();
        arg1.putString("tipoOrdenLibro", "orderbook_compra");
        fragmento.setArguments(arg1);
        ft.replace(R.id.fragmentCompra, fragmento);
        ft.commit();

        Fragment fragmento2 = new Fragment();
        fragmento2 = new ListaPosturaFragment();
        arg1 = new Bundle();
        arg1.putString("tipoOrdenLibro", "orderbook_venta");
        fragmento2.setArguments(arg1);
        ft = fm.beginTransaction();
        ft.replace(R.id.fragmentVenta, fragmento2);
        ft.commit();

        Fragment fragmentOrdenesActivasCompra = new Fragment();
        fragmentOrdenesActivasCompra = new ListaPosturaFragment();
        arg1 = new Bundle();
        arg1.putString("tipoOrdenLibro", "openorder_compra");
        fragmentOrdenesActivasCompra.setArguments(arg1);
        ft = fm.beginTransaction();
        ft.replace(R.id.fragmentAbiertaCompra, fragmentOrdenesActivasCompra);
        ft.commit();


        Fragment fragmentOrdenesActivasVenta = new Fragment();
        fragmentOrdenesActivasVenta = new ListaPosturaFragment();
        arg1 = new Bundle();
        arg1.putString("tipoOrdenLibro", "openorder_venta");
        fragmentOrdenesActivasVenta.setArguments(arg1);
        ft = fm.beginTransaction();
        ft.replace(R.id.fragmentOpenVenta, fragmentOrdenesActivasVenta);
        ft.commit();


        Fragment fragmentPosiblesVentas = new Fragment();
        fragmentPosiblesVentas = new ListaPosturaFragment();
        arg1 = new Bundle();
        arg1.putString("tipoOrdenLibro", "posibleventa");
        fragmentPosiblesVentas.setArguments(arg1);
        ft = fm.beginTransaction();
        ft.replace(R.id.fragmentCompraPorVender, fragmentPosiblesVentas);
        ft.commit();





    }


    public void Boton_Onclick(View v) {

        String tipoOrdenLibro = "orderbook_compra";

        Intent bitso1 = new Intent(this, SolicitudBitso.class);
        bitso1.putExtra("tipo", tipoOrdenLibro);
        startService(bitso1);


        tipoOrdenLibro = "openorder_compra";

        bitso1 = new Intent(this, SolicitudBitso.class);
        bitso1.putExtra("tipo", tipoOrdenLibro);
        startService(bitso1);


    }

    public void BotonActualizarVenta_Onclick(View v) {

        String tipoOrdenLibro = "orderbook_venta";

        Intent bitso1 = new Intent(this, SolicitudBitso.class);
        bitso1.putExtra("tipo", tipoOrdenLibro);
        startService(bitso1);


        tipoOrdenLibro = "openorder_venta";

        bitso1 = new Intent(this, SolicitudBitso.class);
        bitso1.putExtra("tipo", tipoOrdenLibro);
        startService(bitso1);

        tipoOrdenLibro = "posibleventa";

        bitso1 = new Intent(this, SolicitudBitso.class);
        bitso1.putExtra("tipo", tipoOrdenLibro);
        startService(bitso1);


    }


    public void CompraVenta_OnClick(View v) {
        Button btn = (Button) v;

        Intent compraVentaIntent = new Intent(this, PlaceOrder.class);

        if (btn.getText().equals("Comprar")) {
            compraVentaIntent.putExtra("tipo", "compra");
        } else {
            compraVentaIntent.putExtra("tipo", "venta");
        }
        startActivity(compraVentaIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_general, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        boolean regreso = super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_general_settings:
                Toast.makeText(this, "Menu general", Toast.LENGTH_SHORT).show();
                Intent preferencia = new Intent(this, PreferenceGeneralActivity.class);
                startActivity(preferencia);
                regreso = true;
                break;

            case R.id.menu_general_actualizar:
                Toast.makeText(this, "Actualizando", Toast.LENGTH_SHORT).show();
                regreso = true;

                ActualizacionGeneral();

                break;


            default:
                break;

        }

        return regreso;
    }


    private void ActualizacionGeneral() {

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String bitsoKey = SP.getString("bitsoKey", "0");
        String bitsoSecret = SP.getString("bitsoSecret", "0");

        if (bitsoKey.equals("0") || bitsoSecret.equals("0")) {

            Toast.makeText(this, "Registre su BitsoKey y BitsoSecret en la sección de configuración", Toast.LENGTH_SHORT).show();
        } else {


            Intent bitso = new Intent(this, SolicitudBitso.class);
            bitso.setData(Uri.parse(("x")));
            bitso.putExtra("tipo", "balance");
            startService(bitso);

            Intent feeIntent = new Intent(this, SolicitudBitso.class);
            feeIntent.setData(Uri.parse(("x")));
            feeIntent.putExtra("tipo", "fee");
            startService(feeIntent);


            Intent VolumenIntent = new Intent(this, SolicitudBitso.class);
            VolumenIntent.setData(Uri.parse(("x")));
            VolumenIntent.putExtra("tipo", "volumentransaccion");
            startService(VolumenIntent);

            Intent gananciaIntent = new Intent(this, SolicitudBitso.class);
            gananciaIntent.setData(Uri.parse(("x")));
            gananciaIntent.putExtra("tipo", "calculoganancia");
            startService(gananciaIntent);

        }
    }


    private void CambiarLibro() {

        TextView txtSaldoCriptoMoneda = (TextView) findViewById(R.id.lblSaldoCriptoMoneda);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String bitsoBook = SP.getString("bitsoBook", "eth_mxn");
        String bookReducido = null;
        if (bitsoBook.equals("eth_mxn")) {
            bookReducido = "ETH";
        } else {
            bookReducido = "BTC";
        }
        txtSaldoCriptoMoneda.setText(bookReducido);


    }


    public  void btnCrearDB_OnClick(View v)
    {



        Intent solicitudBase = new Intent(this,DataBaseService.class);
        solicitudBase.setData(Uri.parse(("x")));
        startService(solicitudBase);





    }

}
