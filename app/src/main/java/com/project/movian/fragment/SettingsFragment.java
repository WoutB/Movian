package com.project.movian.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.widget.Toast;

import com.project.movian.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState,
                                    String rootKey) {
        setPreferencesFromResource(R.xml.pref_main, rootKey);


        Preference language=(Preference)findPreference((getResources().getString(R.string.language_app)));
        language.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
            public boolean onPreferenceChange(Preference preference, Object newValue){
                String stringValue = newValue.toString();

                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                // Set the summary to reflect the new value.
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getResources().getString(R.string.language_app), index);
                editor.commit();

                Toast.makeText(getActivity(), getResources().getString(R.string.pl_restart), Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }
}
