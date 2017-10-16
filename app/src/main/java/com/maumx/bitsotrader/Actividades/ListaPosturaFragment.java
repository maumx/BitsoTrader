package com.maumx.bitsotrader.Actividades;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.view.ViewGroup;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maumx.bitsotrader.BitsoEntities.Orden;
import com.maumx.bitsotrader.BitsoEntities.OrdenLibro;
import com.maumx.bitsotrader.BitsoEntities.PosturaLibro;
import com.maumx.bitsotrader.R;
import com.maumx.bitsotrader.JsonResponse.ResultadoP;
import com.maumx.bitsotrader.Servicios.SolicitudBitso;
import com.maumx.bitsotrader.OrdenCustomAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaPosturaFragment extends Fragment {

private ListView lstView=null;
    public ListaPosturaFragment() {
        // Required empty public constructor
    }


private String tipoPostura;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



     View vista= inflater.inflate(R.layout.list_view_posturas,container,false);

        lstView = (ListView) vista.findViewById(R.id.lstViewPosturas);


            String tipoOrdenLibro=    getArguments().getString("tipoOrdenLibro");

        Intent  bitso1= new  Intent(getActivity(), SolicitudBitso.class);

        bitso1.putExtra("tipo", tipoOrdenLibro);
        getActivity().startService(bitso1  );

        IntentFilter statusIntentFilter1 = new IntentFilter(
                "com.maumx.bitsotrader.BROADCAST_"+tipoOrdenLibro.toUpperCase());



        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                RecibidorEstado,
                statusIntentFilter1);





        return vista;

    }



    private BroadcastReceiver RecibidorEstado = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, final Intent intent) {


    ListAdapter adaptador = null;

    String valor = intent.getStringExtra("com.maumx.bitsotrader.PAYLOAD");
    String tipoLibro = intent.getStringExtra("tipoOrdenBook");

    Gson gson = new Gson();
    ArrayList<HashMap<String, String>> feedList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> map = new HashMap<String, String>();

    if (tipoLibro.contains("orderbook")) {

        Type listType = new TypeToken<ResultadoP<OrdenLibro>>() {
        }.getType();
        ResultadoP<OrdenLibro> elemento = gson.fromJson(valor, listType);
        map = new HashMap<String, String>();
        map.put("precio", "Precio");
        map.put("cantidad", "Cantidad");
        map.put("valor", "Valor");
        feedList.add(map);
        List<PosturaLibro> posturas = null;

        if (intent.getStringExtra("tipoOrdenBook").equals("orderbook_venta")) {
            posturas = elemento.getResult().getAsks();

        } else if (intent.getStringExtra("tipoOrdenBook").equals("orderbook_compra")) {


            posturas = elemento.getResult().getBids();
        }
        for (PosturaLibro postura : posturas) {
            map = new HashMap<String, String>();
            map.put("precio", String.format("%s MXN", postura.getPrice().setScale(2, BigDecimal.ROUND_CEILING).toString()));
            map.put("cantidad", String.format("%s", postura.getAmount().setScale(8, BigDecimal.ROUND_UNNECESSARY).toString()));
            map.put("valor", String.format("%s MXN", postura.getAmount().setScale(8, BigDecimal.ROUND_UNNECESSARY).multiply(postura.getPrice().setScale(2, BigDecimal.ROUND_CEILING)).setScale(2, BigDecimal.ROUND_FLOOR).toString()));

            feedList.add(map);
        }

        adaptador = new SimpleAdapter(getActivity(), feedList, R.layout.view_item_postura, new String[]{"precio", "cantidad", "valor"}, new int[]{R.id.lblItemPrecio, R.id.lblItemCantidad, R.id.lblItemValor});


    }

    if (tipoLibro.contains("openorder")) {
        Type listType = new TypeToken<ResultadoP<List<Orden>>>() {
        }.getType();
        ResultadoP<List<Orden>> elemento = gson.fromJson(valor, listType);
        List<Orden> posturas = elemento.getResult();


        map = new HashMap<String, String>();

        map.put("precio", "Precio");
        map.put("cantidad", "Cantidad");
        map.put("valor", "Valor");

        for (Orden orden : posturas) {

            if ((tipoLibro.contains("openorder_compra") && orden.getSide().equals("buy") == false) ||

                    (tipoLibro.contains("openorder_venta") && orden.getSide().equals("sell") == false)) {
                continue;
            }


            map = new HashMap<String, String>();

            map.put("precio", String.format("%s MXN", orden.getPrice().setScale(2, BigDecimal.ROUND_CEILING)));
            map.put("cantidad", String.format("%s", orden.getUnfilled_amount().setScale(8, BigDecimal.ROUND_UNNECESSARY)));
            map.put("valor", String.format("%s MXN", orden.getOriginal_value().setScale(2, BigDecimal.ROUND_CEILING)));

            map.put("id", String.valueOf(orden.getOid()));
            map.put("status", String.valueOf(orden.getStatus()));
            map.put("original_value", String.valueOf(orden.getOriginal_amount()));
            map.put("unfilled_amount", String.valueOf(orden.getUnfilled_amount()));
            map.put("updated_at", String.valueOf(orden.getUpdated_at()));
            map.put("created_at", String.valueOf(orden.getCreated_at()));

            feedList.add(map);
        }

        adaptador = new SimpleAdapter(getActivity(), feedList, R.layout.view_item_postura, new String[]{"precio", "cantidad", "valor"}, new int[]{R.id.lblItemPrecio, R.id.lblItemCantidad, R.id.lblItemValor});


        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, String> entry = (HashMap<String, String>) parent.getAdapter().getItem(position);
                Toast.makeText(getActivity(), String.valueOf(entry.get("id")), Toast.LENGTH_SHORT).show();
                Intent intentOpenOrderDetail = new Intent(getActivity(), OpenOrderDetail.class);
                intentOpenOrderDetail.putExtra("id", entry.get("id"));
                intentOpenOrderDetail.putExtra("status", entry.get("status"));
                intentOpenOrderDetail.putExtra("original_value", entry.get("original_value"));
                intentOpenOrderDetail.putExtra("unfilled_amount", entry.get("unfilled_amount"));
                intentOpenOrderDetail.putExtra("updated_at", entry.get("updated_at"));
                intentOpenOrderDetail.putExtra("created_at", entry.get("created_at"));
                startActivity(intentOpenOrderDetail);
            }
        });


    }


    if (tipoLibro.contains("posibleventa")) {

        Type lis = new TypeToken<List<Orden>>() {
        }.getType();

        ArrayList<Orden> listaOrdenes = new ArrayList<>();


        List<Orden> posturas = new Gson().fromJson(valor, lis);


        if (posturas == null) {
            posturas = new ArrayList<Orden>();
        }

        map = new HashMap<String, String>();

        map.put("precio", "Precio");
        map.put("cantidad", "Cantidad");
        map.put("valor", "Valor");

        for (Orden orden : posturas) {

            if ((tipoLibro.contains("openorder_compra") && orden.getSide().equals("buy") == false) ||

                    (tipoLibro.contains("openorder_venta") && orden.getSide().equals("sell") == false)) {
                continue;
            }

            listaOrdenes.add(orden);
            map = new HashMap<String, String>();

            map.put("precio", String.format("%s MXN", orden.getPrice().setScale(2, BigDecimal.ROUND_CEILING).toString()));
            map.put("cantidad", String.format("%s", orden.getOriginal_amount().setScale(8, BigDecimal.ROUND_FLOOR).toString()));
            map.put("valor", String.format("%s MXN", orden.getOriginal_value().setScale(2, BigDecimal.ROUND_CEILING).toString()));

            map.put("id", String.valueOf(orden.getOid()));
            map.put("status", String.valueOf(orden.getStatus()));
            map.put("original_value", String.valueOf(orden.getOriginal_value()));
            map.put("unfilled_amount", String.valueOf(orden.getUnfilled_amount()));
            map.put("updated_at", String.valueOf(orden.getUpdated_at()));
            map.put("created_at", String.valueOf(orden.getCreated_at()));

            feedList.add(map);
        }

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Orden entry = (Orden) parent.getAdapter().getItem(position);
                // HashMap<String, String> entry= (HashMap<String, String> )parent.getAdapter().getItem(position);


                Toast.makeText(getActivity(), String.valueOf(entry), Toast.LENGTH_SHORT).show();
                Intent posibleVentaActivity = new Intent(getActivity(), PlaceOrder.class);

                posibleVentaActivity.putExtra("tipo", "venta");
                posibleVentaActivity.putExtra("origen", "posibleventa");


                posibleVentaActivity.putExtra("id", entry.getOid());
                posibleVentaActivity.putExtra("precio", String.valueOf(entry.getPrice()));
                posibleVentaActivity.putExtra("cantidad", entry.getOriginal_amount().setScale(8, BigDecimal.ROUND_FLOOR).toString());
                posibleVentaActivity.putExtra("original_value", entry.getOriginal_value().setScale(2, BigDecimal.ROUND_CEILING).toString());


       /*
                     posibleVentaActivity.putExtra("status",entry.get("status"));
                        posibleVentaActivity.putExtra("original_value",entry.get("original_value"));
                        posibleVentaActivity.putExtra("unfilled_amount",entry.get("unfilled_amount"));
                        posibleVentaActivity.putExtra("updated_at",entry.get("updated_at"));
                        posibleVentaActivity.putExtra("created_at",entry.get("created_at"));*/
                startActivity(posibleVentaActivity);
            }
        });

        try {
            adaptador = new OrdenCustomAdapter(getActivity(), R.layout.view_item_postura, listaOrdenes);

        } catch (Exception ex) {
            String sts = ex.getMessage();
        }


    }


    lstView.setItemsCanFocus(false);
    lstView.setAdapter(adaptador);



        }
    };




}
