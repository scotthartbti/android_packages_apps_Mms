
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
import android.preference.EditTextPreference;
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
import com.android.mms.themes.Constants;
import com.android.mms.ui.ColorPickerPreference;

public class ThemesMessageList extends PreferenceActivity implements
            OnPreferenceChangeListener {

    // message background
    ColorPickerPreference mMessageBackground;
    // send
    ColorPickerPreference mSentTextColor;
    ColorPickerPreference mSentDateColor;
    ColorPickerPreference mSentContactColor;
    ColorPickerPreference mSentTextBgColor;
    ColorPickerPreference mSentSmiley;
    // received
    ColorPickerPreference mRecvTextColor;
    ColorPickerPreference mRecvContactColor;
    ColorPickerPreference mRecvDateColor;
    ColorPickerPreference mRecvTextBgColor;
    ColorPickerPreference mRecvSmiley;

    CheckBoxPreference mUseContact;
    CheckBoxPreference mShowAvatar;
    CheckBoxPreference mBubbleFillParent;
    EditTextPreference mAddSignature;
    ListPreference mTextLayout;
    ListPreference mContactFontSize;
    ListPreference mFontSize;
    ListPreference mDateFontSize;
    ListPreference mBubbleType;
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

    private void loadPrefs() {
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences_themes_msglist);
        PreferenceScreen prefSet = getPreferenceScreen();

        mAddSignature = (EditTextPreference) findPreference(Constants.PREF_SIGNATURE);
        mUseContact = (CheckBoxPreference) prefSet.findPreference(Constants.PREF_USE_CONTACT);
        mShowAvatar = (CheckBoxPreference) prefSet.findPreference(Constants.PREF_SHOW_AVATAR);
        mBubbleFillParent = (CheckBoxPreference) prefSet.findPreference(Constants.PREF_BUBBLE_FILL_PARENT);
        mTextLayout = (ListPreference) findPreference(Constants.PREF_TEXT_CONV_LAYOUT);
        mContactFontSize = (ListPreference) findPreference(Constants.PREF_CONTACT_FONT_SIZE);
        mFontSize = (ListPreference) findPreference(Constants.PREF_FONT_SIZE);
        mDateFontSize = (ListPreference) findPreference(Constants.PREF_DATE_FONT_SIZE);
        mBubbleType = (ListPreference) findPreference(Constants.PREF_BUBBLE_TYPE);
        mMessageBackground = (ColorPickerPreference) findPreference(Constants.PREF_MESSAGE_BG);
        mSentTextColor = (ColorPickerPreference) findPreference(Constants.PREF_SENT_TEXTCOLOR);
        mSentContactColor = (ColorPickerPreference) findPreference(Constants.PREF_SENT_TEXTCOLOR);
        mSentDateColor = (ColorPickerPreference) findPreference(Constants.PREF_SENT_TEXTCOLOR);
        mSentTextBgColor = (ColorPickerPreference) findPreference(Constants.PREF_SENT_TEXTCOLOR);
        mSentSmiley = (ColorPickerPreference) findPreference(Constants.PREF_SENT_SMILEY);
        mRecvTextColor = (ColorPickerPreference) findPreference(Constants.PREF_RECV_TEXTCOLOR);
        mRecvContactColor = (ColorPickerPreference) findPreference(Constants.PREF_RECV_TEXTCOLOR);
        mRecvDateColor = (ColorPickerPreference) findPreference(Constants.PREF_RECV_TEXTCOLOR);
        mRecvTextBgColor = (ColorPickerPreference) findPreference(Constants.PREF_RECV_TEXT_BG);
        mRecvSmiley = (ColorPickerPreference) findPreference(Constants.PREF_RECV_SMILEY);
        mCustomImage = findPreference("pref_custom_image");
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean result = false;
        if (preference == mMessageBackground) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mMessageBackground.setSummary(hex);
        } else if (preference == mSentTextColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mSentTextColor.setSummary(hex);
        } else if (preference == mSentContactColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mSentContactColor.setSummary(hex);
        } else if (preference == mSentDateColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mSentDateColor.setSummary(hex);
        } else if (preference == mSentTextBgColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mSentTextBgColor.setSummary(hex);
        } else if (preference == mSentSmiley) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mSentSmiley.setSummary(hex);
        } else if (preference == mRecvTextColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mRecvTextColor.setSummary(hex);
        } else if (preference == mRecvContactColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mRecvContactColor.setSummary(hex);
        } else if (preference == mRecvDateColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mRecvDateColor.setSummary(hex);
        } else if (preference == mRecvTextBgColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mRecvTextBgColor.setSummary(hex);
        } else if (preference == mRecvSmiley) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            mRecvSmiley.setSummary(hex);
        } else if (preference == mTextLayout) {
            int index = mTextLayout.findIndexOfValue((String) newValue);
            mTextLayout.setSummary(mTextLayout.getEntries()[index]);
            return true;
        } else if (preference == mContactFontSize) {
            int index = mContactFontSize.findIndexOfValue((String) newValue);
            mContactFontSize.setSummary(mFontSize.getEntries()[index]);
            int val = Integer.parseInt((String) newValue);
            result = Settings.System.putInt(this.getContentResolver(),
                    Constants.PREF_CONTACT_FONT_SIZE, val);
        } else if (preference == mFontSize) {
            int index = mFontSize.findIndexOfValue((String) newValue);
            mFontSize.setSummary(mFontSize.getEntries()[index]);
            int val = Integer.parseInt((String) newValue);
            result = Settings.System.putInt(this.getContentResolver(),
                    Constants.PREF_FONT_SIZE, val);
        } else if (preference == mDateFontSize) {
            int index = mDateFontSize.findIndexOfValue((String) newValue);
            mDateFontSize.setSummary(mFontSize.getEntries()[index]);
            int val = Integer.parseInt((String) newValue);
            result = Settings.System.putInt(this.getContentResolver(),
                    Constants.PREF_DATE_FONT_SIZE, val);
        } else if (preference == mBubbleType) {
            int index = mBubbleType.findIndexOfValue((String) newValue);
            mBubbleType.setSummary(mBubbleType.getEntries()[index]);
            return true;
        } else if (preference == mAddSignature) {
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

        } else if (preference == mUseContact) {
            value = mUseContact.isChecked();

        } else if (preference == mShowAvatar) {
            value = mShowAvatar.isChecked();

        } else if (preference == mBubbleFillParent) {
            value = mShowAvatar.isChecked();
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void restoreThemeMessageListDefaultPreferences() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply();
        setPreferenceScreen(null);
        loadPrefs();
    }

    private void setSignature() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mAddSignature.setText(sp.getString(Constants.PREF_SIGNATURE, ""));
    }

    private void registerListeners() {
        mAddSignature.setOnPreferenceChangeListener(this);
        mTextLayout.setOnPreferenceChangeListener(this);
        mContactFontSize.setOnPreferenceChangeListener(this);
        mContactFontSize.setValue(Integer.toString(Settings.System.getInt(this.getContentResolver(),
                Constants.PREF_CONTACT_FONT_SIZE, 16)));
        mFontSize.setOnPreferenceChangeListener(this);
        mFontSize.setValue(Integer.toString(Settings.System.getInt(this.getContentResolver(),
                Constants.PREF_FONT_SIZE, 16)));
        mDateFontSize.setOnPreferenceChangeListener(this);
        mDateFontSize.setValue(Integer.toString(Settings.System.getInt(this.getContentResolver(),
                Constants.PREF_DATE_FONT_SIZE, 16)));
        mBubbleType.setOnPreferenceChangeListener(this);
        mMessageBackground.setOnPreferenceChangeListener(this);
        mSentTextColor.setOnPreferenceChangeListener(this);
        mSentContactColor.setOnPreferenceChangeListener(this);
        mSentDateColor.setOnPreferenceChangeListener(this);
        mSentTextBgColor.setOnPreferenceChangeListener(this);
        mSentSmiley.setOnPreferenceChangeListener(this);
        mRecvTextColor.setOnPreferenceChangeListener(this);
        mRecvContactColor.setOnPreferenceChangeListener(this);
        mRecvDateColor.setOnPreferenceChangeListener(this);
        mRecvTextBgColor.setOnPreferenceChangeListener(this);
        mRecvSmiley.setOnPreferenceChangeListener(this);
    }

    private void updateSummaries() {
        mTextLayout.setSummary(mTextLayout.getEntry());
        mContactFontSize.setSummary(mContactFontSize.getEntry());
        mFontSize.setSummary(mFontSize.getEntry());
        mDateFontSize.setSummary(mDateFontSize.getEntry());
        mBubbleType.setSummary(mBubbleType.getEntry());
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
