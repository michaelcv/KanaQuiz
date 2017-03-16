package com.noprestige.kanaquiz;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class OptionsFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference onIncorrect = findPreference(getResources().getString(R.string.prefid_on_incorrect));

        setSummary(onIncorrect, OptionsControl.getString(R.string.prefid_on_incorrect));

        onIncorrect.setOnPreferenceChangeListener(
            new Preference.OnPreferenceChangeListener()
            {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue)
                {
                    return setSummary(preference, newValue);
                }
            }
        );
    }

    private boolean setSummary(Preference preference, Object newValue)
    {
        if (getResources().getString(R.string.prefid_on_incorrect_default).equals(newValue))
            preference.setSummary(R.string.incorrect_option_move_on);
        else if (getResources().getString(R.string.prefid_on_incorrect_show_answer).equals(newValue))
            preference.setSummary(R.string.incorrect_option_show_answer);
        else if (getResources().getString(R.string.prefid_on_incorrect_retry).equals(newValue))
            preference.setSummary(R.string.incorrect_option_retry);
        else
            return false;
        return true;
    }
}