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
import android.preference.EditTextPreference;
import android.preference.ListPreference;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ThemesMessageList extends PreferenceActivity implements
            OnPreferenceChangeListener {

    private SharedPreferences sp;
    private SharedPreferences.Editor spEdit;

    // message background
    private ColorPickerPreference mMessageBackground;
    // send
    private ColorPickerPreference mSentTextColor;
    private ColorPickerPreference mSentDateColor;
    private ColorPickerPreference mSentContactColor;
    private ColorPickerPreference mSentTextBgColor;
    // received
    private ColorPickerPreference mRecvTextColor;
    private ColorPickerPreference mRecvContactColor;
    private ColorPickerPreference mRecvDateColor;
    private ColorPickerPreference mRecvTextBgColor;

    private CheckBoxPreference mUseContact;
    private CheckBoxPreference mShowAvatar;
    private CheckBoxPreference mBubbleFillParent;
    private TextSizeSeekBarPreference mContactFontSize;
    private TextSizeSeekBarPreference mFontSize;
    private TextSizeSeekBarPreference mDateFontSize;
    private EditTextPreference mAddSignature;
    private ListPreference mTextLayout;
    private ListPreference mBubbleType;
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

    private void loadPrefs() {
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.themes_message_list);
        PreferenceScreen prefSet = getPreferenceScreen();

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        spEdit = sp.edit();

        mAddSignature = (EditTextPreference) findPreference(Constants.PREF_SIGNATURE);
        mUseContact = (CheckBoxPreference) prefSet.findPreference(Constants.PREF_USE_CONTACT);
        mShowAvatar = (CheckBoxPreference) prefSet.findPreference(Constants.PREF_SHOW_AVATAR);
        mBubbleFillParent = (CheckBoxPreference) prefSet.findPreference(Constants.PREF_BUBBLE_FILL_PARENT);
        mTextLayout = (ListPreference) findPreference(Constants.PREF_TEXT_CONV_LAYOUT);
        mContactFontSize = (TextSizeSeekBarPreference) findPreference(Constants.PREF_CONTACT_FONT_SIZE);
        mFontSize = (TextSizeSeekBarPreference) findPreference(Constants.PREF_FONT_SIZE);
        mDateFontSize = (TextSizeSeekBarPreference) findPreference(Constants.PREF_DATE_FONT_SIZE);
        mBubbleType = (ListPreference) findPreference(Constants.PREF_BUBBLE_TYPE);
        mMessageBackground = (ColorPickerPreference) findPreference(Constants.PREF_MESSAGE_BG);
        mRecvTextBgColor = (ColorPickerPreference) findPreference(Constants.PREF_RECV_TEXT_BG);
        mRecvTextColor = (ColorPickerPreference) findPreference(Constants.PREF_RECV_TEXTCOLOR);
        mRecvContactColor = (ColorPickerPreference) findPreference(
                Constants.PREF_RECV_CONTACT_COLOR);
        mRecvDateColor = (ColorPickerPreference) findPreference(Constants.PREF_RECV_DATE_COLOR);
        mSentTextBgColor = (ColorPickerPreference) findPreference(Constants.PREF_SENT_TEXT_BG);
        mSentTextColor = (ColorPickerPreference) findPreference(Constants.PREF_SENT_TEXTCOLOR);
        mSentContactColor = (ColorPickerPreference) findPreference(
                Constants.PREF_SENT_CONTACT_COLOR);
        mSentDateColor = (ColorPickerPreference) findPreference(Constants.PREF_SENT_DATE_COLOR);
        mCustomImage = findPreference("pref_custom_image");
    }

    private void setDefaultValues() {
        mContactFontSize.setMax(22);
        mContactFontSize.setMin(10);
        mContactFontSize.setProgress(sp.getInt(Constants.PREF_CONTACT_FONT_SIZE, 16));
        mFontSize.setMax(22);
        mFontSize.setMin(10);
        mFontSize.setProgress(sp.getInt(Constants.PREF_FONT_SIZE, 16));
        mDateFontSize.setMax(22);
        mDateFontSize.setMin(10);
        mDateFontSize.setProgress(sp.getInt(Constants.PREF_DATE_FONT_SIZE, 16));
    }

    private void setListeners() {
        mAddSignature.setOnPreferenceChangeListener(this);
        mTextLayout.setOnPreferenceChangeListener(this);
        mBubbleType.setOnPreferenceChangeListener(this);
        mMessageBackground.setOnPreferenceChangeListener(this);
        mSentTextColor.setOnPreferenceChangeListener(this);
        mSentContactColor.setOnPreferenceChangeListener(this);
        mSentDateColor.setOnPreferenceChangeListener(this);
        mSentTextBgColor.setOnPreferenceChangeListener(this);
        mRecvTextColor.setOnPreferenceChangeListener(this);
        mRecvContactColor.setOnPreferenceChangeListener(this);
        mRecvDateColor.setOnPreferenceChangeListener(this);
        mRecvTextBgColor.setOnPreferenceChangeListener(this);
    }

    private void updateSummaries() {
        Resources res = this.getResources();
        mTextLayout.setSummary(mTextLayout.getEntry());
        mBubbleType.setSummary(mBubbleType.getEntry());
        mMessageBackground.setSummary(ColorPickerPreference.convertToARGB(
                sp.getInt(Constants.PREF_MESSAGE_BG, res.getColor(
                R.color.default_message_background))));
        mRecvTextBgColor.setSummary(ColorPickerPreference.convertToARGB(
                sp.getInt(Constants.PREF_RECV_TEXT_BG, res.getColor(
                R.color.default_recv_text_bg))));
        mRecvTextColor.setSummary(ColorPickerPreference.convertToARGB(
                sp.getInt(Constants.PREF_RECV_TEXTCOLOR, res.getColor(
                R.color.default_recv_textcolor))));
        mRecvContactColor.setSummary(ColorPickerPreference.convertToARGB(
                sp.getInt(Constants.PREF_RECV_CONTACT_COLOR, res.getColor(
                R.color.default_recv_contact_color))));
        mRecvDateColor.setSummary(ColorPickerPreference.convertToARGB(
                sp.getInt(Constants.PREF_RECV_DATE_COLOR, res.getColor(
                R.color.default_recv_datecolor))));
        mSentTextBgColor.setSummary(ColorPickerPreference.convertToARGB(
                sp.getInt(Constants.PREF_SENT_TEXT_BG, res.getColor(
                R.color.default_sent_text_bg))));
        mSentTextColor.setSummary(ColorPickerPreference.convertToARGB(
                sp.getInt(Constants.PREF_SENT_TEXTCOLOR, res.getColor(
                R.color.default_sent_textcolor))));
        mSentContactColor.setSummary(ColorPickerPreference.convertToARGB(
                sp.getInt(Constants.PREF_SENT_CONTACT_COLOR, res.getColor(
                R.color.default_sent_contact_color))));
        mSentDateColor.setSummary(ColorPickerPreference.convertToARGB(
                sp.getInt(Constants.PREF_SENT_DATE_COLOR, res.getColor(
                R.color.default_sent_datecolor))));
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean result = false;
        if (preference == mMessageBackground) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mMessageBackground.setSummary(hex);
            result = true;
        } else if (preference == mSentTextColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mSentTextColor.setSummary(hex);
            result = true;
        } else if (preference == mSentContactColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mSentContactColor.setSummary(hex);
            result = true;
        } else if (preference == mSentDateColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mSentDateColor.setSummary(hex);
            result = true;
        } else if (preference == mSentTextBgColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mSentTextBgColor.setSummary(hex);
            result = true;
        } else if (preference == mRecvTextColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mRecvTextColor.setSummary(hex);
            result = true;
        } else if (preference == mRecvContactColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mRecvContactColor.setSummary(hex);
            result = true;
        } else if (preference == mRecvDateColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mRecvDateColor.setSummary(hex);
            result = true;
        } else if (preference == mRecvTextBgColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mRecvTextBgColor.setSummary(hex);
            result = true;
        } else if (preference == mTextLayout) {
            int index = mTextLayout.findIndexOfValue((String) newValue);
            mTextLayout.setSummary(mTextLayout.getEntries()[index]);
            result = true;
        } else if (preference == mContactFontSize) {
            int value = Integer.parseInt((String) newValue);
            mContactFontSize.setProgress(sp.getInt(Constants.PREF_CONTACT_FONT_SIZE, value));
            result = true;
        } else if (preference == mFontSize) {
            int value = Integer.parseInt((String) newValue);
            mFontSize.setProgress(sp.getInt(Constants.PREF_FONT_SIZE, value));
            result = true;
        } else if (preference == mDateFontSize) {
            int value = Integer.parseInt((String) newValue);
            mDateFontSize.setProgress(sp.getInt(Constants.PREF_DATE_FONT_SIZE, value));
            result = true;
        } else if (preference == mBubbleType) {
            int index = mBubbleType.findIndexOfValue((String) newValue);
            mBubbleType.setSummary(mBubbleType.getEntries()[index]);
            result = true;
        } else if (preference == mAddSignature) {
            spEdit.putString(Constants.PREF_SIGNATURE, (String) newValue);
            spEdit.commit();
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

            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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

            startActivityForResult(Intent.createChooser(
                    intent, "Select Image"), Constants.REQUEST_PICK_WALLPAPER);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void restoreThemeMessageListDefaultPreferences() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply();
        setPreferenceScreen(null);
        loadPrefs();
    }

    private void setSignature() {
        mAddSignature.setText(sp.getString(Constants.PREF_SIGNATURE, ""));
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
                restoreThemeMessageListDefaultPreferences();
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
        this.deleteFile(Constants.MSG_CUSTOM_IMAGE);
    }

    private Uri getCustomImageExternalUri() {
        File dir = this.getExternalCacheDir();
        File wallpaper = new File(dir, Constants.MSG_CUSTOM_IMAGE);

        return Uri.fromFile(wallpaper);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.REQUEST_PICK_WALLPAPER) {
                FileOutputStream wallpaperStream = null;
                try {
                    wallpaperStream = this.openFileOutput(Constants.MSG_CUSTOM_IMAGE,
                            Context.MODE_WORLD_READABLE);
                } catch (FileNotFoundException e) {
                    return; // No file found
                }

                Uri selectedImageUri = getCustomImageExternalUri();
                Bitmap bitmap;
                if (data != null) {
                    Uri mUri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(
                                this.getContentResolver(), mUri);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, wallpaperStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        bitmap = BitmapFactory.decodeFile(selectedImageUri.getPath());
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, wallpaperStream);
                    } catch (NullPointerException npe) {
                        Log.e("ThemesMessageList", "SeletedImageUri was null.");
                        super.onActivityResult(requestCode, resultCode, data);
                        return;
                    }
                }
            }
        }
    }
}
