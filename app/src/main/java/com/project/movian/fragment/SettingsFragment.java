package com.project.movian.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.project.movian.LocaleHelper;
import com.project.movian.R;
import com.project.movian.RateActivity;

import io.paperdb.Paper;

/**
 * Settingspage tutorial gevolgd:
 * https://google-developer-training.gitbooks.io/android-developer-fundamentals-course-practicals/content/en/Unit%204/92_p_adding_settings_to_an_app.html
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

                Paper.book().write(getResources().getString(R.string.language_app), stringValue);
                updateView(Paper.book().read(getResources().getString(R.string.language_app)).toString());

                Toast.makeText(getActivity(), getResources().getString(R.string.pl_restart), Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        Preference rateButton = findPreference(getString(R.string.rate_us));
        rateButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), RateActivity.class));
                return true;
            }
        });
    }
    private void updateView(String lang) {
        Context context = LocaleHelper.setLocale(getContext(), lang);
        Resources resources = context.getResources();

    }
}
