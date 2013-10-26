/*
 * Copyright (C) 2013 The CyanogenMod Project
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
/*
 *  Copyright (C) 2013 The OmniROM Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.android.mms.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Collections;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.view.View.OnFocusChangeListener;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import com.android.mms.R;
import com.android.mms.data.Contact;
import com.android.mms.data.GMembership;
import com.android.mms.data.Group;
import com.android.mms.data.PhoneNumber;

public class AddRecipientsList extends Activity implements OnQueryTextListener,
                OnCloseListener, OnFocusChangeListener{

    private static final String TAG = "AddRecipientsList";

    public static boolean mIsRunning;

    private AddRecipientsListAdapter mListAdapter;
    private Button mOkButton;
    private Button mCancelButton;
    private ArrayList<PhoneNumber> mPhoneNumbers;
    private ArrayList<Group> mGroups;
    private ArrayList<GMembership> mGroupMemberships;
    private ArrayList<PhoneNumber> mCheckedPhoneNumbers;
    private SearchView mSearchView;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_recipients_list_screen);

        // Buttons
        mOkButton = (Button) findViewById(R.id.ok_button);
        mOkButton.setEnabled(false);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = mCheckedPhoneNumbers.size();
                String[] resultData = new String[count];
                for (int i = 0; i < count; i++) {
                    PhoneNumber phoneNumber = mCheckedPhoneNumbers.get(i);
                    if (phoneNumber.isChecked()) {
                        resultData[i] = phoneNumber.getNumber();
                    }
                }

                Intent intent = new Intent();
                intent.putExtra("com.android.mms.ui.AddRecipients", resultData);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        mCancelButton = (Button) findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupActionBar();

        // List
        mListView = (ListView) findViewById(R.id.list);
        mListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        mListView.setFastScrollEnabled(true);
        mListView.setFastScrollAlwaysVisible(true);
        mListView.setDivider(null);
        mListView.setDividerHeight(0);
        mListView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                AddRecipientsListItem item  = (AddRecipientsListItem) adapter.getItemAtPosition(position);

                if (item.isGroup()) {
                    Group group = item.getGroup();
                    checkGroup(group, !group.isChecked());
                } else {
                    PhoneNumber phoneNumber = item.getPhoneNumber();
                    checkPhoneNumber(phoneNumber, !phoneNumber.isChecked());
                }

                mOkButton.setEnabled(mCheckedPhoneNumbers.size() > 0);
                mListAdapter.notifyDataSetChanged();
            }
         });


        initListAdapter();
    }

    @Override
    public void onPause() {
        super.onPause();
        // close keyboard if open
        mSearchView.clearFocus();
        mIsRunning = false;
    }

    private void checkPhoneNumber(PhoneNumber phoneNumber, boolean check) {
        phoneNumber.setChecked(check);

        if (check) {
            if (!mCheckedPhoneNumbers.contains(phoneNumber)) {
                mCheckedPhoneNumbers.add(phoneNumber);
            }
        } else {
            if (mCheckedPhoneNumbers.contains(phoneNumber)) {
                mCheckedPhoneNumbers.remove(phoneNumber);
            }

            ArrayList<Group> phoneGroups = phoneNumber.getGroups();
            int count = phoneGroups.size();
            for (int i = 0; i < count; i++) {
                Group group = phoneGroups.get(i);
                if (group.isChecked()) {
                    group.setChecked(false);
                }
            }
        }
    }

    private void checkGroup(Group group, boolean check) {
        group.setChecked(check);
        ArrayList<PhoneNumber> phoneNumbers = group.getPhoneNumbers();
        int count = phoneNumbers.size();

        for (int i = 0; i < count; i++) {
            PhoneNumber phoneNumber = phoneNumbers.get(i);
            if (phoneNumber.isDefault() || phoneNumber.isFirst()) {
                checkPhoneNumber(phoneNumber, check);
            }
        }
    }

    private void initListAdapter() {
        mPhoneNumbers = PhoneNumber.getPhoneNumbers(this);

        if (mPhoneNumbers == null) {
            return;
        }

        mCheckedPhoneNumbers = new ArrayList<PhoneNumber>();
        mGroups = Group.getGroups(this);
        mGroupMemberships = GMembership.getGroupMemberships(this);
        int GMCount = 0;
        int groupsCount = 0;
        int phoneNumbersCount = mPhoneNumbers.size();

        Map<Long,ArrayList<Long>> groupIdWithContactsId = new HashMap<Long, ArrayList<Long>>();
        ArrayList<AddRecipientsListItem> items = new ArrayList<AddRecipientsListItem>();

        if (mGroups != null && mGroupMemberships != null){
            // Store GID with all its CIDs
            GMCount = mGroupMemberships.size();

            for (int i = 0; i < GMCount; i++) {
                GMembership groupMembership = mGroupMemberships.get(i);
                Long gid = groupMembership.getGroupId();
                Long uid = groupMembership.getContactId();

                if (!groupIdWithContactsId.containsKey(gid)) {
                    groupIdWithContactsId.put(gid, new ArrayList<Long>());
                }

                if (!groupIdWithContactsId.get(gid).contains(uid)) {
                    groupIdWithContactsId.get(gid).add(uid);
                }
            }

            // For each PhoneNumber, find its GID, and add it to correct Group
            groupsCount = mGroups.size();

            for (int i = 0; i < phoneNumbersCount; i++) {
                PhoneNumber phoneNumber = mPhoneNumbers.get(i);
                long cid = phoneNumber.getContactId();

                Iterator<Long> iterator = groupIdWithContactsId.keySet().iterator();
                while (iterator.hasNext()) {
                    long gid = (Long)iterator.next();
                    if (groupIdWithContactsId.get(gid).contains(cid)) {
                        for (int j = 0; j < groupsCount; j++) {
                            Group group = mGroups.get(j);
                            if (group.getId() == gid) {
                                group.addPhoneNumber(phoneNumber);
                                phoneNumber.addGroup(group);
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < groupsCount; i++) {
                Group group = mGroups.get(i);
                items.add(i, new AddRecipientsListItem(this, group));
            }
        }
        for (int i = 0; i < phoneNumbersCount; i++) {
            PhoneNumber phoneNumber = mPhoneNumbers.get(i);
            items.add(i + groupsCount, new AddRecipientsListItem(this, phoneNumber));
        }

        Collections.sort(items);
        mListAdapter = new AddRecipientsListAdapter(this, items);
        mListView.setAdapter(mListAdapter);
    }

    private void setupActionBar() {
        final ActionBar actionBar = getActionBar();

        final View searchViewContainer = LayoutInflater.from(actionBar.getThemedContext())
                .inflate(R.layout.add_recipients_list_actionbar, null);
        mSearchView = (SearchView) searchViewContainer.findViewById(R.id.search_view);

        // In order to make the SearchView look like "shown via search menu", we need to
        // manually setup its state. See also DialtactsActivity.java and ActionBarAdapter.java.
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setQueryHint(getString(R.string.hint_findContacts));
        mSearchView.setIconified(false);

        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);
        mSearchView.setOnQueryTextFocusChangeListener(this);

        actionBar.setCustomView(searchViewContainer,
                    new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Clear focus and suppress keyboard show-up.
        mSearchView.clearFocus();
    }

    @Override
    public boolean onClose() {
        if (!TextUtils.isEmpty(mSearchView.getQuery())) {
            mSearchView.setQuery(null, true);
        }
        return true;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.search_view: {
                if (hasFocus) {
                    showInputMethod(mSearchView.findFocus());
                }
            }
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mListAdapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private void showInputMethod(View view) {
        final InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            if (!imm.showSoftInput(view, 0)) {
                Log.w(TAG, "Failed to show soft input method.");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
