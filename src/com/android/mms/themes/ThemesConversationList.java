
package com.android.mms.themes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Spannable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.android.mms.R;
import com.android.mms.ui.ColorPickerPreference;

public class ThemesConversationList extends PreferenceActivity implements
            OnPreferenceChangeListener {

    // background
    ColorPickerPreference mConvListBackground;
    // conv list read
    ColorPickerPreference mReadBg;
    ColorPickerPreference mReadContact;
    ColorPickerPreference mReadSubject;
    ColorPickerPreference mReadDate;
    ColorPickerPreference mReadCount;
    ColorPickerPreference mReadSmiley;
    // conv list read
    ColorPickerPreference mUnreadBg;
    ColorPickerPreference mUnreadContact;
    ColorPickerPreference mUnreadSubject;
    ColorPickerPreference mUnreadDate;
    ColorPickerPreference mUnreadCount;
    ColorPickerPreference mUnreadSmiley;
    ListPreference mContactFontSize;
    ListPreference mFontSize;
    ListPreference mDateFontSize;

    Preference mCustomImage;

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
        addPreferencesFromResource(R.xml.preferences_themes_convlist);

        mCustomImage = findPreference("pref_custom_image");
        mConvListBackground = (ColorPickerPreference) findPreference(Constants.PREF_CONV_LIST_BG);
        mContactFontSize = (ListPreference) findPreference(Constants.PREF_CONV_CONTACT_FONT_SIZE);
        mFontSize = (ListPreference) findPreference(Constants.PREF_CONV_FONT_SIZE);
        mDateFontSize = (ListPreference) findPreference(Constants.PREF_CONV_DATE_FONT_SIZE);
        mReadBg = (ColorPickerPreference) findPreference(Constants.PREF_READ_BG);
        mReadContact = (ColorPickerPreference) findPreference(Constants.PREF_READ_CONTACT);
        mReadCount = (ColorPickerPreference) findPreference(Constants.PREF_READ_COUNT);
        mReadDate = (ColorPickerPreference) findPreference(Constants.PREF_READ_DATE);
        mReadSubject = (ColorPickerPreference) findPreference(Constants.PREF_READ_SUBJECT);
        mReadSmiley = (ColorPickerPreference) findPreference(Constants.PREF_READ_SMILEY);
        mUnreadBg = (ColorPickerPreference) findPreference(Constants.PREF_UNREAD_BG);
        mUnreadContact = (ColorPickerPreference) findPreference(Constants.PREF_UNREAD_CONTACT);
        mUnreadCount = (ColorPickerPreference) findPreference(Constants.PREF_UNREAD_COUNT);
        mUnreadDate = (ColorPickerPreference) findPreference(Constants.PREF_UNREAD_DATE);
        mUnreadSubject = (ColorPickerPreference) findPreference(Constants.PREF_UNREAD_SUBJECT);
        mUnreadSmiley = (ColorPickerPreference) findPreference(Constants.PREF_UNREAD_SMILEY);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean result = false;
        if (preference == mConvListBackground) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mConvListBackground.setSummary(hex);
        } else if (preference == mReadBg) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mReadBg.setSummary(hex);
        } else if (preference == mReadContact) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mReadContact.setSummary(hex);
        } else if (preference == mReadCount) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mReadCount.setSummary(hex);
        } else if (preference == mReadDate) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mReadDate.setSummary(hex);
        } else if (preference == mReadSubject) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mReadSubject.setSummary(hex);
        } else if (preference == mReadSmiley) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mReadSmiley.setSummary(hex);
        } else if (preference == mUnreadBg) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mUnreadBg.setSummary(hex);
        } else if (preference == mUnreadContact) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mUnreadContact.setSummary(hex);
        } else if (preference == mUnreadCount) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mUnreadCount.setSummary(hex);
        } else if (preference == mUnreadDate) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mUnreadDate.setSummary(hex);
        } else if (preference == mUnreadSubject) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mUnreadSubject.setSummary(hex);
        } else if (preference == mUnreadSmiley) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mUnreadSmiley.setSummary(hex);
        } else if (preference == mContactFontSize) {
            int index = mContactFontSize.findIndexOfValue((String) newValue);
            mContactFontSize.setSummary(mFontSize.getEntries()[index]);
            int val = Integer.parseInt((String) newValue);
            result = Settings.System.putInt(this.getContentResolver(),
                    Constants.PREF_CONV_CONTACT_FONT_SIZE, val);
        } else if (preference == mFontSize) {
            int index = mFontSize.findIndexOfValue((String) newValue);
            mFontSize.setSummary(mFontSize.getEntries()[index]);
            int val = Integer.parseInt((String) newValue);
            result = Settings.System.putInt(this.getContentResolver(),
                    Constants.PREF_CONV_FONT_SIZE, val);
        } else if (preference == mDateFontSize) {
            int index = mDateFontSize.findIndexOfValue((String) newValue);
            mDateFontSize.setSummary(mFontSize.getEntries()[index]);
            int val = Integer.parseInt((String) newValue);
            result = Settings.System.putInt(this.getContentResolver(),
                    Constants.PREF_CONV_DATE_FONT_SIZE, val);
        }
        return result;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        if (preference == mCustomImage) {
            Display display = this.getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            Rect rect = new Rect();
            Window window = this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(rect);
            int statusBarHeight = rect.top;
            int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
            int titleBarHeight = contentViewTop - statusBarHeight;

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
            intent.setType("image/*");
            intent.putExtra("crop", "true");
            boolean isPortrait = getResources()
                    .getConfiguration().orientation
                    == Configuration.ORIENTATION_PORTRAIT;
            intent.putExtra("aspectX", isPortrait ? width : height - titleBarHeight);
            intent.putExtra("aspectY", isPortrait ? height - titleBarHeight : width);
            intent.putExtra("outputX", width);
            intent.putExtra("outputY", height);
            intent.putExtra("scale", true);
            intent.putExtra("scaleUpIfNeeded", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getCustomImageExternalUri());
            intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());

            startActivityForResult(intent, Constants.REQUEST_PICK_WALLPAPER);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void registerListeners() {
        mConvListBackground.setOnPreferenceChangeListener(this);
        mContactFontSize.setOnPreferenceChangeListener(this);
        mContactFontSize.setValue(Integer.toString(Settings.System.getInt(this.getContentResolver(),
                Constants.PREF_CONV_CONTACT_FONT_SIZE, 16)));
        mFontSize.setOnPreferenceChangeListener(this);
        mFontSize.setValue(Integer.toString(Settings.System.getInt(this.getContentResolver(),
                Constants.PREF_CONV_FONT_SIZE, 16)));
        mDateFontSize.setOnPreferenceChangeListener(this);
        mDateFontSize.setValue(Integer.toString(Settings.System.getInt(this.getContentResolver(),
                Constants.PREF_CONV_DATE_FONT_SIZE, 16)));
        mReadBg.setOnPreferenceChangeListener(this);
        mReadContact.setOnPreferenceChangeListener(this);
        mReadCount.setOnPreferenceChangeListener(this);
        mReadDate.setOnPreferenceChangeListener(this);
        mReadSubject.setOnPreferenceChangeListener(this);
        mReadSmiley.setOnPreferenceChangeListener(this);
        mUnreadBg.setOnPreferenceChangeListener(this);
        mUnreadContact.setOnPreferenceChangeListener(this);
        mUnreadCount.setOnPreferenceChangeListener(this);
        mUnreadDate.setOnPreferenceChangeListener(this);
        mUnreadSubject.setOnPreferenceChangeListener(this);
        mUnreadSmiley.setOnPreferenceChangeListener(this);
    }

    private void updateSummaries() {
        mContactFontSize.setSummary(mContactFontSize.getEntry());
        mFontSize.setSummary(mFontSize.getEntry());
        mDateFontSize.setSummary(mDateFontSize.getEntry());
    }

    private void restoreThemeConversationListDefaultPreferences() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply();
        setPreferenceScreen(null);
        loadPrefs();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.clear();
        menu.add(0, Constants.THEMES_RESTORE_DEFAULTS, 0, R.string.restore_default);
        menu.add(0, Constants.THEMES_CUSTOM_IMAGE_DELETE, 0, R.string.delete_custom_image);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Constants.THEMES_RESTORE_DEFAULTS:
                restoreThemeConversationListDefaultPreferences();
                return true;

            case Constants.THEMES_CUSTOM_IMAGE_DELETE:
                deleteCustomImage();
                return true;

            case android.R.id.home:
                // The user clicked on the Messaging icon in the action bar. Take them back from
                // wherever they came from
                finish();
                return true;
        }
        return false;
    }

    private void deleteCustomImage() {
        this.deleteFile(Constants.CONV_CUSTOM_IMAGE);
    }

    private Uri getCustomImageExternalUri() {
        File dir = this.getExternalCacheDir();
        File wallpaper = new File(dir, Constants.CONV_CUSTOM_IMAGE);

        return Uri.fromFile(wallpaper);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.REQUEST_PICK_WALLPAPER) {

                FileOutputStream wallpaperStream = null;
                try {
                    wallpaperStream = this.openFileOutput(Constants.CONV_CUSTOM_IMAGE,
                            Context.MODE_WORLD_READABLE);
                } catch (FileNotFoundException e) {
                    return; // NOOOOO
                }

                Uri selectedImageUri = getCustomImageExternalUri();
                Bitmap bitmap = BitmapFactory.decodeFile(selectedImageUri.getPath());

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, wallpaperStream);
            }
        }
    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
