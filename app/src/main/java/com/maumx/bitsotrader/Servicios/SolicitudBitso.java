package com.maumx.bitsotrader.Servicios;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import 	android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.maumx.bitsotrader.BitsoCreator;
import com.maumx.bitsotrader.BitsoEntities.Fondeo;
import com.maumx.bitsotrader.BitsoEntities.Retiro;
import com.maumx.bitsotrader.BitsoEntities.Trade;
import com.maumx.bitsotrader.DataBase.DaoOpenOrder;
import com.maumx.bitsotrader.JsonResponse.GeneralResponse;
import com.maumx.bitsotrader.JsonResponse.OidResponse;
import com.maumx.bitsotrader.BitsoEntities.Orden;
import com.maumx.bitsotrader.JsonResponse.ResultadoP;

/**
 * Created by Mauricio on 02/04/2017.
 */

public class SolicitudBitso extends IntentService {

    public  SolicitudBitso()
    {
        super("Servicio Bitso");

    }

    @Override
    protected void onHandleIntent(Intent intent)
    {

        GeneralResponse respuesta = new GeneralResponse();
        String dataString = intent.getDataString();



        String valor=null;
        Intent reportarReso =  null ;

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String bitsoKey = SP.getString("bitsoKey","0");
        String bitsoSecret = SP.getString("bitsoSecret","0");
        String bitsoBook = SP.getString("bitsoBook","eth_mxn");



        BitsoCreator bc= new BitsoCreator(bitsoKey,bitsoSecret );
        try {
            if (  intent.getStringExtra("tipo").equals("balance") ){

                try {


                    valor = bc.ObtenerBalance();
                    reportarReso = new Intent("com.maumx.bitsotrader.BROADCAST_ACTION");
                    reportarReso.putExtra("com.maumx.bitsotrader.BALANCE", valor);
                }catch (Exception ex)
                {
                    String x= ex.getMessage();
                    Log.d("Solicitud_Balance",ex.getMessage(),ex.getCause());

                }

            }

            if (  intent.getStringExtra("tipo").equals("fee") ){

                try
                {
                    respuesta=   bc.ObtenerFee ();
                    reportarReso=new Intent("com.maumx.bitsotrader.BROADCAST_FEE");
                    reportarReso.putExtra("com.maumx.bitsotrader.PAYLOAD",respuesta.getmJson());
                    reportarReso.putExtra("com.maumx.bitsotrader.STATUSCODE",String.valueOf(respuesta.getmHttpStatusCode()) );


                }catch (Exception ex)
                {
                    String x= ex.getMessage();
                    Log.d("Solicitud_fee",ex.getMessage(),ex.getCause());

                }

            }


            if (  intent.getStringExtra("tipo").equals("orderbook_compra") ){


                try{
                    valor=   bc.ObtenerLibroOrdenes(bitsoBook);
                    reportarReso=new Intent("com.maumx.bitsotrader.BROADCAST_ORDERBOOK_COMPRA");
                    reportarReso.putExtra("com.maumx.bitsotrader.PAYLOAD",valor);
                    reportarReso.putExtra("tipoOrdenBook","orderbook_compra");
                }catch (Exception ex)
                {
                    String x= ex.getMessage();
                    Log.d("Solicitud_OB_compra",ex.getMessage(),ex.getCause());

                }




            }
            if (  intent.getStringExtra("tipo").equals("openorder_compra") ){

                try {

                    valor = bc.ObtenerOpenOrders(bitsoBook);
                    reportarReso = new Intent("com.maumx.bitsotrader.BROADCAST_OPENORDER_COMPRA");
                    reportarReso.putExtra("com.maumx.bitsotrader.PAYLOAD", valor);
                    reportarReso.putExtra("tipoOrdenBook", "openorder_compra");

                }catch (Exception ex)
                {
                    String x= ex.getMessage();
                    Log.d("Solicitud_OO_compra",ex.getMessage(),ex.getCause());

                }


            }
            if (intent.getStringExtra("tipo").equals("orderbook_venta")) {
                try {

                    valor = bc.ObtenerLibroOrdenes(bitsoBook);
                reportarReso = new Intent("com.maumx.bitsotrader.BROADCAST_ORDERBOOK_VENTA");
                reportarReso.putExtra("com.maumx.bitsotrader.PAYLOAD", valor);
                reportarReso.putExtra("tipoOrdenBook", "orderbook_venta");
                }catch (Exception ex)
                {
                    String x= ex.getMessage();
                    Log.d("Solicitud_OO_compra",ex.getMessage(),ex.getCause());

                }
            }



            if (  intent.getStringExtra("tipo").equals("openorder_venta") ){

                try
                {
                    valor=   bc.ObtenerOpenOrders(bitsoBook);
                    reportarReso=new Intent("com.maumx.bitsotrader.BROADCAST_OPENORDER_VENTA");
                    reportarReso.putExtra("com.maumx.bitsotrader.PAYLOAD",valor);
                    reportarReso.putExtra("tipoOrdenBook","openorder_venta");
                }catch (Exception ex)
                {
                    String x= ex.getMessage();
                    Log.d("Solicitud_OO_venta",ex.getMessage(),ex.getCause());

                }


            }
            if (  intent.getStringExtra("tipo").equals("openorder_cancelacion") ){

                try{
                    String oid= intent.getStringExtra ("oid");
                    valor=   bc.CancelarOrden (oid);
                    reportarReso=new Intent("com.maumx.bitsotrader.BROADCAST_OPENORDER_CANCELACION");
                    reportarReso.putExtra("com.maumx.bitsotrader.PAYLOAD",valor);
                    reportarReso.putExtra("tipoOrdenBook","openorder_cancelacion");
                }catch (Exception ex)
                {
                    String x= ex.getMessage();
                    Log.d("Solicitud_ordercancel",ex.getMessage(),ex.getCause());

                }

            }



            if (  intent.getStringExtra("tipo").equals("placeorder_compra") ||  intent.getStringExtra("tipo").equals("placeorder_venta") ){

                try{
                    String price= intent.getStringExtra ("price");
                    String book= intent.getStringExtra ("book");
                    String side= intent.getStringExtra ("side");


                    String major= "";
                    String type= intent.getStringExtra ("type");
                    if (   intent.hasExtra("major") )
                    {
                        major= intent.getStringExtra("major");
                    }

                    String minor= "";
                    if (   intent.hasExtra("minor") ){
                        minor= intent.getStringExtra("minor");
                    }





                    respuesta= bc.PlaceOrder (bitsoBook,side,type,major,minor,price);

                    ArrayList<Orden> listOrden = null;

                    if ( respuesta.getmHttpStatusCode()==200   )
                    {

                        Type t= new TypeToken<ResultadoP<OidResponse>>(){}.getType();
                        ResultadoP<OidResponse> generalOrden=   new Gson().fromJson( respuesta.getmJson(), t);
                        OidResponse xx=   generalOrden.getResult();
                        GeneralResponse reponseConsultar=bc.ObtenerConsultarOrden( xx.getOid());

                        if ( reponseConsultar.getmHttpStatusCode()==200  ) {
                            t = new TypeToken<ResultadoP<List<Orden>>>() {}.getType();
                            ResultadoP<List<Orden>> orden = new Gson().fromJson(reponseConsultar.getmJson(), t);
                            new DaoOpenOrder(getBaseContext()).Insertar(orden.getResult().get(0));
                        }
                    }

                    reportarReso=new Intent("com.maumx.bitsotrader.BROADCAST_"  + intent.getStringExtra("tipo").toUpperCase());
                    reportarReso.putExtra("com.maumx.bitsotrader.PAYLOAD",respuesta.getmJson());
                    reportarReso.putExtra("com.maumx.bitsotrader.STATUSCODE",String.valueOf(respuesta.getmHttpStatusCode()) );
                    reportarReso.putExtra("tipoOrdenBook","placeorder_" + intent.getStringExtra("tipo").toLowerCase() );

                }catch (Exception ex)
                {
                    String x= ex.getMessage();
                    Log.d("Solicitud_PO_Compra",ex.getMessage(),ex.getCause());

                }


            }
            if (  intent.getStringExtra("tipo").equals("posibleventa") )
            {
                try{
                    GeneralResponse gR= bc.ObtenerUserTrades(bitsoBook,"");
                    Type t= new TypeToken<ResultadoP<  List<Trade>>>(){}.getType();

                    ResultadoP<  List<Trade>> resultado=   new Gson().fromJson(gR.getmJson(),t);

                    List<Trade> lista= resultado.getResult();


                    if (lista!=null)
                    {



                    HashMap<String,List<Trade>> m= new HashMap<String,List<Trade>>();
                    for ( Trade orden : lista)
                    {
                        if (  orden.getSide().equals("sell")  )
                            continue;

                        if(!m.containsKey(orden.getOid()))
                        {

                            ArrayList<Trade> listaOrdenes= new ArrayList<Trade>();

                            listaOrdenes.add(orden);
                            m.put(orden.getOid(),listaOrdenes);
                        }else
                        {
                            List<Trade> listaOrdenes=m.get(orden.getOid());
                            listaOrdenes.add(orden);
                        }

                    }

                    Set<String> listaLlaves= m.keySet();
                    t= new TypeToken<ResultadoP<List< Orden>>>(){}.getType();

                    ArrayList<Orden> listaOrdenes= new ArrayList<Orden>();
                    for ( String llaves:  listaLlaves ) {
                        gR = bc.ObtenerConsultarOrden(llaves);

                        ResultadoP<List <Orden>> p=     new Gson().fromJson(gR.getmJson(),t);
    if (p.getSuccess()==false)
    {
        continue;//si hubo un error al consultar entonces pasa a la siguiente
    }
                        Orden ordenReconstruida= p.getResult().get(0);

                        ordenReconstruida.setOriginal_amount(BigDecimal.valueOf(0.0f));
                        for ( Trade tr :  lista  )
                        {
                            if (    tr.getOid().equals( llaves))
                            {
                                ordenReconstruida.setPrice( tr.getPrice()  );
                                ordenReconstruida.setOriginal_amount(      BigDecimal.valueOf(  ordenReconstruida.getOriginal_amount().setScale(8, BigDecimal.ROUND_FLOOR ).doubleValue() +   tr.getMajor().setScale(8, BigDecimal.ROUND_FLOOR).doubleValue() -tr.getFees_amount().setScale(8, BigDecimal.ROUND_FLOOR) .doubleValue() ) )  ;


                            }

                        }

                        listaOrdenes.add(ordenReconstruida);

                    }


                    Collections.sort(listaOrdenes,  new Comparator<Orden>() {
                        public int compare(Orden o1, Orden o2) {
                            return o1.getCreated_at().compareTo( o2.getCreated_at()) *-1;
                        }
                    });


                    Type lsType = new TypeToken<ArrayList<Orden>>(){}.getType();
                    reportarReso=new Intent("com.maumx.bitsotrader.BROADCAST_"  + intent.getStringExtra("tipo").toUpperCase());

                    reportarReso.putExtra("com.maumx.bitsotrader.PAYLOAD", new Gson().toJson(listaOrdenes,lsType) );
                    reportarReso.putExtra("tipoOrdenBook","posibleventa");

                    }

                }catch (Exception ex)
                {
                    String x= ex.getMessage();
                    Log.d("Solicitud_PO_venta",ex.getMessage(),ex.getCause());

                }
            }


            if (  intent.getStringExtra("tipo").equals("volumentransaccion") )
            {
                try{
                    List<Trade> lista=null;
                    boolean esFechaCorrecta=false;
                    GregorianCalendar fecha = new GregorianCalendar();

                    fecha.add(Calendar.MONTH,-1);
                    do {


                        GeneralResponse gR= bc.ObtenerUserTrades(bitsoBook,"");
                        Type t= new TypeToken<ResultadoP<  List<Trade>>>(){}.getType();

                        ResultadoP<  List<Trade>> resultado=   new Gson().fromJson(gR.getmJson(),t);

                        lista= resultado.getResult();

                        if ( lista.size()>0 && lista.get( lista.size()-1) .getCreated_at().after (fecha.getTime()) )
                        {
                            gR= bc.ObtenerUserTrades(bitsoBook, String.valueOf( lista.get( lista.size()-1).getTid()));
                            resultado=   new Gson().fromJson(gR.getmJson(),t);

                            lista.addAll( resultado.getResult());
                        }else
                        {
                            esFechaCorrecta=true;
                        }
                    }while(esFechaCorrecta==true);

                    Collections.sort(lista,  new Comparator<Trade>() {
                        public int compare(Trade o1, Trade o2) {
                            return o1.getCreated_at().compareTo( o2.getCreated_at()) *-1;
                        }
                    });



                    double sumaMovimientos=0.0f;
                    for (int i =0; i<lista.size();i++)
                    {

                        if ( lista.get(i).getCreated_at().after(fecha.getTime()))

                        {

                            double val=lista.get(i).getMajor().setScale(8,BigDecimal.ROUND_FLOOR).doubleValue();

                            if (   val<0 )
                            {
                                val*=-1;
                            }
                            sumaMovimientos+=val ;
                        }


                    }



                    Type lsType = new TypeToken<ArrayList<Orden>>(){}.getType();
                    reportarReso=new Intent("com.maumx.bitsotrader.BROADCAST_"  + intent.getStringExtra("tipo").toUpperCase());

                    reportarReso.putExtra("com.maumx.bitsotrader.PAYLOAD",   sumaMovimientos  ) ;

                }catch (Exception ex)
                {
                    String x= ex.getMessage();
                    Log.d("Solicitud_columen",ex.getMessage(),ex.getCause());

                }


            }


            if (  intent.getStringExtra("tipo").equals("calculoganancia") )
            {

                try{
                    StringBuffer conjunto = new StringBuffer();

                    GeneralResponse rp=   bc.ObtenerFondeos();

                    Type  t= new TypeToken<ResultadoP<List<Fondeo>>>(){}.getType();
                    ResultadoP<List<Fondeo>>  f= new Gson().fromJson(rp.getmJson(),t);

                    Type tfondeo= new  TypeToken<List<Fondeo>>(){}.getType();


                    String parte1=  new Gson().toJson(  f.getResult(),tfondeo).toString();

                    conjunto.append(parte1);
                    conjunto.append("|");

                    rp=   bc.ObtenerRetiros();


                    t= new TypeToken<ResultadoP<List<Retiro>>>(){}.getType();
                    ResultadoP<List<Retiro>>  r= new Gson().fromJson(rp.getmJson(),t);


                    Type tRetiro= new  TypeToken<List<Retiro>>(){}.getType();
                    String parte2=  new Gson().toJson(  r.getResult(),tRetiro);

                    conjunto.append(parte2);

                    reportarReso=new Intent("com.maumx.bitsotrader.BROADCAST_"  + intent.getStringExtra("tipo").toUpperCase());

                    reportarReso.putExtra("com.maumx.bitsotrader.PAYLOAD",   conjunto.toString()) ;

                }catch (Exception ex)
                {
                    String x= ex.getMessage();
                    Log.d("Solicitud_ganancia",ex.getMessage(),ex.getCause());

                }
            }






            if (  reportarReso!=null  )
            LocalBroadcastManager.getInstance(this).sendBroadcast(reportarReso);
        }
        catch (Exception ex)
        {
            String x= ex.getMessage();
            Log.d("ERROR",ex.getMessage(),ex.getCause());

        }









    }
}


