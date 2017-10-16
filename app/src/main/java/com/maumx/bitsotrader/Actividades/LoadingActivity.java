package com.maumx.bitsotrader.Actividades;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.maumx.bitsotrader.R;

public class LoadingActivity extends Activity {



    private BroadcastReceiver TradesReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {




        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

    }















}
