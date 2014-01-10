/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.mms.themes;

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
import android.preference.ColorPickerPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.preference.TextSizeSeekBarPreference;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public class ThemesConversationList extends PreferenceActivity implements
            OnPreferenceChangeListener {

    private SharedPreferences sp;
    private SharedPreferences.Editor spEdit;

    // background
    private ColorPickerPreference mConvListBackground;
    // conv list read
    private ColorPickerPreference mReadBg;
    private ColorPickerPreference mReadContact;
    private ColorPickerPreference mReadSubject;
    private ColorPickerPreference mReadDate;
    private ColorPickerPreference mReadCount;
    // conv list read
    private ColorPickerPreference mUnreadBg;
    private ColorPickerPreference mUnreadContact;
    private ColorPickerPreference mUnreadSubject;
    private ColorPickerPreference mUnreadDate;
    private ColorPickerPreference mUnreadCount;
    private TextSizeSeekBarPreference mContactFontSize;
    private TextSizeSeekBarPreference mFontSize;
    private TextSizeSeekBarPreference mDateFontSize;

    private Preference mCustomImage;

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
        setListeners();
        setDefaultValues();
        updateSummaries();
    }

    public void loadPrefs() {
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.themes_conversation_list);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        spEdit = sp.edit();

        mCustomImage = findPreference("pref_custom_image");
        mConvListBackground = (ColorPickerPreference) findPreference(
                Constants.CONVERSATION_LIST_BACKGROUND);
        mContactFontSize = (TextSizeSeekBarPreference) findPreference(
                Constants.PREF_CONV_CONTACT_FONT_SIZE);
        mFontSize = (TextSizeSeekBarPreference) findPreference(Constants.PREF_CONV_FONT_SIZE);
        mDateFontSize = (TextSizeSeekBarPreference) findPreference(Constants.PREF_CONV_DATE_FONT_SIZE);
        mReadBg = (ColorPickerPreference) findPreference(Constants.PREF_READ_BG);
        mReadContact = (ColorPickerPreference) findPreference(Constants.PREF_READ_CONTACT);
        mReadCount = (ColorPickerPreference) findPreference(Constants.PREF_READ_COUNT);
        mReadDate = (ColorPickerPreference) findPreference(Constants.PREF_READ_DATE);
        mReadSubject = (ColorPickerPreference) findPreference(Constants.PREF_READ_SUBJECT);
        mUnreadBg = (ColorPickerPreference) findPreference(Constants.PREF_UNREAD_BG);
        mUnreadContact = (ColorPickerPreference) findPreference(Constants.PREF_UNREAD_CONTACT);
        mUnreadCount = (ColorPickerPreference) findPreference(Constants.PREF_UNREAD_COUNT);
        mUnreadDate = (ColorPickerPreference) findPreference(Constants.PREF_UNREAD_DATE);
        mUnreadSubject = (ColorPickerPreference) findPreference(Constants.PREF_UNREAD_SUBJECT);
    }

    private void setDefaultValues() {
        mContactFontSize.setMax(22);
        mContactFontSize.setMin(10);
        mContactFontSize.setProgress(sp.getInt(Constants.PREF_CONV_CONTACT_FONT_SIZE, 16));
        mFontSize.setMax(22);
        mFontSize.setMin(10);
        mFontSize.setProgress(sp.getInt(Constants.PREF_CONV_FONT_SIZE, 16));
        mDateFontSize.setMax(22);
        mDateFontSize.setMin(10);
        mDateFontSize.setProgress(sp.getInt(Constants.PREF_CONV_DATE_FONT_SIZE, 16));
    }

    private void setListeners() {
        mConvListBackground.setOnPreferenceChangeListener(this);
        mReadBg.setOnPreferenceChangeListener(this);
        mReadContact.setOnPreferenceChangeListener(this);
        mReadCount.setOnPreferenceChangeListener(this);
        mReadDate.setOnPreferenceChangeListener(this);
        mReadSubject.setOnPreferenceChangeListener(this);
        mUnreadBg.setOnPreferenceChangeListener(this);
        mUnreadContact.setOnPreferenceChangeListener(this);
        mUnreadCount.setOnPreferenceChangeListener(this);
        mUnreadDate.setOnPreferenceChangeListener(this);
        mUnreadSubject.setOnPreferenceChangeListener(this);
    }

    private void updateSummaries() {
        Resources res = getResources();
        mConvListBackground.setSummary(ColorPickerPreference.convertToARGB(
                sp.getInt(Constants.CONVERSATION_LIST_BACKGROUND, res.getColor(
                R.color.default_conv_list_background))));
        mReadBg.setSummary(ColorPickerPreference.convertToARGB(
                sp.getInt(Constants.PREF_READ_BG, res.getColor(
                R.color.default_read_bg))));
        mReadContact.setSummary(ColorPickerPreference.convertToARGB(
                sp.getInt(Constants.PREF_READ_CONTACT, res.getColor(
                R.color.default_read_contact))));
        mReadSubject.setSummary(ColorPickerPreference.convertToARGB(
                sp.getInt(Constants.PREF_READ_SUBJECT, res.getColor(
                R.color.default_read_subject))));
        mReadDate.setSummary(ColorPickerPreference.convertToARGB(
                sp.getInt(Constants.PREF_READ_DATE, res.getColor(
                R.color.default_read_date))));
        mReadCount.setSummary(ColorPickerPreference.convertToARGB(
                sp.getInt(Constants.PREF_READ_COUNT, res.getColor(
                R.color.default_read_count))));
        mUnreadBg.setSummary(ColorPickerPreference.convertToARGB(
                sp.getInt(Constants.PREF_UNREAD_BG, res.getColor(
                R.color.default_unread_bg))));
        mUnreadContact.setSummary(ColorPickerPreference.convertToARGB(
                sp.getInt(Constants.PREF_UNREAD_CONTACT, res.getColor(
                R.color.default_unread_contact))));
        mUnreadSubject.setSummary(ColorPickerPreference.convertToARGB(
                sp.getInt(Constants.PREF_UNREAD_SUBJECT, res.getColor(
                R.color.default_unread_subject))));
        mUnreadDate.setSummary(ColorPickerPreference.convertToARGB(
                sp.getInt(Constants.PREF_UNREAD_DATE, res.getColor(
                R.color.default_unread_date))));
        mUnreadCount.setSummary(ColorPickerPreference.convertToARGB(
                sp.getInt(Constants.PREF_UNREAD_COUNT, res.getColor(
                R.color.default_unread_count))));
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean result = false;
        if (preference == mConvListBackground) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mConvListBackground.setSummary(hex);
            result = true;
        } else if (preference == mReadBg) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mReadBg.setSummary(hex);
            result = true;
        } else if (preference == mReadContact) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mReadContact.setSummary(hex);
            result = true;
        } else if (preference == mReadCount) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mReadCount.setSummary(hex);
            result = true;
        } else if (preference == mReadDate) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mReadDate.setSummary(hex);
            result = true;
        } else if (preference == mReadSubject) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mReadSubject.setSummary(hex);
            result = true;
        } else if (preference == mUnreadBg) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mUnreadBg.setSummary(hex);
            result = true;
        } else if (preference == mUnreadContact) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mUnreadContact.setSummary(hex);
            result = true;
        } else if (preference == mUnreadCount) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mUnreadCount.setSummary(hex);
            result = true;
        } else if (preference == mUnreadDate) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mUnreadDate.setSummary(hex);
            result = true;
        } else if (preference == mUnreadSubject) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mUnreadSubject.setSummary(hex);
            result = true;
        } else if (preference == mContactFontSize) {
            int value = Integer.valueOf((Integer) newValue);
            mContactFontSize.setProgress(sp.getInt(Constants.PREF_CONV_CONTACT_FONT_SIZE, value));
            result = true;
        } else if (preference == mFontSize) {
            int value = Integer.valueOf((Integer) newValue);
            mFontSize.setProgress(sp.getInt(Constants.PREF_CONV_FONT_SIZE, value));
            result = true;
        } else if (preference == mDateFontSize) {
            int value = Integer.valueOf((Integer) newValue);
            mDateFontSize.setProgress(sp.getInt(Constants.PREF_CONV_DATE_FONT_SIZE, value));
            result = true;
        }
        return result;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
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
