package xyz.cybersapien.newsdroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ogcybersapien on 7/10/16.
 * Settings activity for the project
 */

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class StoryPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            //Setting up Sections Preferences
            Preference sections = findPreference(getString(R.string.settings_section_key));
            bindPreferenceSummaryToValue(sections);

            //Setting up Number of Items Preferences
            Preference numberOfItems = findPreference(getString(R.string.settings_number_key));
            bindPreferenceSummaryToValue(numberOfItems);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
            if (preference instanceof ListPreference){
                ListPreference sectionsPreference = (ListPreference) preference;

                int prefIndex = sectionsPreference.findIndexOfValue(stringValue);
                if (prefIndex>=0){
                    CharSequence[] labels = sectionsPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else{
                preference.setSummary(stringValue);
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());

            String preferenceString = preferences.getString(preference.getKey(),"");

            onPreferenceChange(preference, preferenceString);
        }
    }
}
