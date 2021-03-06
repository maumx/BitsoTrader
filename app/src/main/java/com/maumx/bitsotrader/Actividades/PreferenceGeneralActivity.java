package com.maumx.bitsotrader.Actividades;


import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.maumx.bitsotrader.R;

public class PreferenceGeneralActivity extends PreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

    }


    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_general);
        }
    }
}
