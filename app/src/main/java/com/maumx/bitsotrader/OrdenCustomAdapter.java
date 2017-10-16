package com.maumx.bitsotrader;

import com.maumx.bitsotrader.BitsoEntities.Orden;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.maumx.bitsotrader.BitsoEntities.Trade;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Mauricio on 23/04/2017.
 */

public class OrdenCustomAdapter extends ArrayAdapter<Orden> {

    Context context;
    int layoutResourceId;
    ArrayList<Orden> ordenes = new ArrayList<Orden>();


    static class OrdenHolder
    {
        TextView txtPrecio;
        TextView txtCantidad;
        TextView txtValor;
        Button btnEliminar;
    }



    public OrdenCustomAdapter(Context context, int layoutResourceId, ArrayList<Orden> data){
 super(context,layoutResourceId);

        this.context=context;
        this.layoutResourceId=layoutResourceId;
        this.ordenes= data;



    }




    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View row= convertView;
        OrdenHolder  holder= null;

        if( row ==null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row= inflater.inflate(layoutResourceId,parent,false);
            holder  = new OrdenHolder();

            holder.txtPrecio= (TextView) row.findViewById(R.id.lblItemPrecio);
            holder.txtCantidad= (TextView) row.findViewById(R.id.lblItemCantidad);
            holder.txtValor= (TextView) row.findViewById(R.id.lblItemValor);
            holder.btnEliminar= (Button) row.findViewById(R.id.btnItemDelete);

            row.setTag(holder);


        }else
            {
                holder= (OrdenHolder)row.getTag();
            }

            Orden orden= ordenes.get(position);



                                                                                                                                                                                        holder.txtPrecio.setText(  orden.getPrice().setScale(2,BigDecimal.ROUND_CEILING).toString() + "MXN");
        holder.txtCantidad.setText( orden.getOriginal_amount().setScale(8, BigDecimal.ROUND_FLOOR) .toString());
        holder.txtValor.setText( orden.getOriginal_value().setScale(2,BigDecimal.ROUND_CEILING).toString() + "MXN");

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(parent.getContext(), "clickeado", Toast.LENGTH_SHORT).show();
                (( Button) v).setText("CLICKEADO");

            }
        });





        return row;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.ordenes.size();
    }

    @Override
    public Orden getItem(int position) {
        // TODO Auto-generated method stub
        return this.ordenes.get(position  );
    }

}
