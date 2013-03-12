
package com.android.mms.themes;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Process;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.MenuItem;

import com.android.mms.R;
import com.android.mms.ui.ColorPickerPreference;

public class Themes extends PreferenceActivity implements
            OnPreferenceChangeListener {

    // restart mms
    private static final String PREF_RESTART_MMS = "pref_restart_mms";

    EditTextPreference mAddSignature;
    Preference mRestartMms;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        loadPrefs();

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerListeners();
    }

    public void loadPrefs() {
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences_themes);

        mRestartMms = (Preference) findPreference(PREF_RESTART_MMS);
        mAddSignature = (EditTextPreference) findPreference(Constants.PREF_SIGNATURE);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean result = false;

        if (preference == mAddSignature) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(Constants.PREF_SIGNATURE, (String) newValue);
            editor.commit();
        }
        return result;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        if (preference == mRestartMms) {
            Process.killProcess(Process.myPid());
            restartFirstActivity();
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // The user clicked on the Messaging icon in the action bar. Take them back from
                // wherever they came from
                finish();
                return true;
        }
        return false;
    }

    private void registerListeners() {
        mAddSignature.setOnPreferenceChangeListener(this);
    }

    private void setSignature() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mAddSignature.setText(sp.getString(Constants.PREF_SIGNATURE, ""));
    }

    private void restartFirstActivity() {
        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(
                getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(i);
    }
}
