
package com.android.mms.themes;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.mms.R;
import com.android.mms.ui.ColorPickerPreference;

public class ThemesWidgets extends PreferenceActivity implements
            OnPreferenceChangeListener {

    ColorPickerPreference mSendersRead;
    ColorPickerPreference mSendersUnread;
    ColorPickerPreference mSubjectRead;
    ColorPickerPreference mSubjectUnread;
    ColorPickerPreference mDateRead;
    ColorPickerPreference mDateUnread;
    ListPreference mWidgetLayout;

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
        updateSummaries();
        registerListeners();
    }

    public void loadPrefs() {
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences_themes_widgets);

        mWidgetLayout = (ListPreference) findPreference(Constants.PREF_WIDGET_LAYOUT);
        mSendersRead = (ColorPickerPreference) findPreference(Constants.PREF_SENDERS_TEXTCOLOR_READ);
        mSendersUnread = (ColorPickerPreference) findPreference(Constants.PREF_SENDERS_TEXTCOLOR_UNREAD);
        mSubjectRead = (ColorPickerPreference) findPreference(Constants.PREF_SUBJECT_TEXTCOLOR_READ);
        mSubjectUnread = (ColorPickerPreference) findPreference(Constants.PREF_SUBJECT_TEXTCOLOR_UNREAD);
        mDateRead = (ColorPickerPreference) findPreference(Constants.PREF_DATE_TEXTCOLOR_READ);
        mDateUnread = (ColorPickerPreference) findPreference(Constants.PREF_DATE_TEXTCOLOR_UNREAD);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean result = false;

        if (preference == mSendersRead) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mSendersRead.setSummary(hex);

        } else if (preference == mSendersUnread) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mSendersUnread.setSummary(hex);

        } else if (preference == mSubjectRead) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mSubjectRead.setSummary(hex);

        } else if (preference == mSubjectUnread) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mSubjectUnread.setSummary(hex);

        } else if (preference == mDateRead) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mDateRead.setSummary(hex);

        } else if (preference == mDateUnread) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mDateUnread.setSummary(hex);

        } else if (preference == mWidgetLayout) {
            int index = mWidgetLayout.findIndexOfValue((String) newValue);
            mWidgetLayout.setSummary(mWidgetLayout.getEntries()[index]);
            return true;

        }
        return result;
    }

    private void registerListeners() {
        mWidgetLayout.setOnPreferenceChangeListener(this);
        mSendersRead.setOnPreferenceChangeListener(this);
        mSendersUnread.setOnPreferenceChangeListener(this);
        mSubjectRead.setOnPreferenceChangeListener(this);
        mSubjectUnread.setOnPreferenceChangeListener(this);
        mDateRead.setOnPreferenceChangeListener(this);
        mDateUnread.setOnPreferenceChangeListener(this);
    }

    private void updateSummaries() {
        mWidgetLayout.setSummary(mWidgetLayout.getEntry());
    }

    private void restoreThemeWidgetsDefaultPreferences() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply();
        setPreferenceScreen(null);
        loadPrefs();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.clear();
        menu.add(0, Constants.THEMES_RESTORE_DEFAULTS, 0, R.string.restore_default);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Constants.THEMES_RESTORE_DEFAULTS:
                restoreThemeWidgetsDefaultPreferences();
                return true;

            case android.R.id.home:
                // The user clicked on the Messaging icon in the action bar. Take them back from
                // wherever they came from
                finish();
                return true;
        }
        return false;
    }
}
