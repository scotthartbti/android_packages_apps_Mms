/*
 * Copyright (C) 2008 The Android Open Source Project
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

 public class Constants {
    public static final boolean DEBUG = false;
    public static final String PREF_NAME = "Mms";

    // add Signature
    public static final String PREF_SIGNATURE = "pref_signature";

    // Menu entries
    public static final int THEMES_RESTORE_DEFAULTS = 1;
    public static final int THEMES_CUSTOM_IMAGE_DELETE = 0;

    // Request Wallpaper
    public static final int REQUEST_PICK_WALLPAPER = 201;
    public static final int SELECT_WALLPAPER = 5;

    /**
     * Messaging view
     */
    // Layout Style
    public static final String PREF_TEXT_CONV_LAYOUT = "pref_text_conv_layout";

    // Text Size
    public static final String PREF_CONTACT_FONT_SIZE = "pref_contact_font_size";
    public static final String PREF_FONT_SIZE = "pref_font_size";
    public static final String PREF_DATE_FONT_SIZE = "pref_date_font_size";

    // Background
    public static final String PREF_MESSAGE_BG = "pref_message_bg";
    public static final String MSG_CUSTOM_IMAGE = "message_list_image.jpg";

    // Bubble Types
    public static final String PREF_BUBBLE_TYPE = "pref_bubble_type";
    public static final String PREF_BUBBLE_FILL_PARENT = "pref_bubble_fill_parent";

    // Checkbox preferences
    public static final String PREF_USE_CONTACT = "pref_use_contact";
    public static final String PREF_SHOW_AVATAR = "pref_show_avatar";

    // Color Preferences Send
    public static final String PREF_SENT_TEXTCOLOR = "pref_sent_textcolor";
    public static final String PREF_SENT_CONTACT_COLOR = "pref_sent_contact_color";
    public static final String PREF_SENT_DATE_COLOR = "pref_sent_date_color";
    public static final String PREF_SENT_TEXT_BG = "pref_sent_text_bg";
    public static final String PREF_SENT_SMILEY = "pref_sent_smiley";

    // Color Preferences Received
    public static final String PREF_RECV_TEXTCOLOR = "pref_recv_textcolor";
    public static final String PREF_RECV_CONTACT_COLOR = "pref_recv_contact_color";
    public static final String PREF_RECV_DATE_COLOR = "pref_recv_date_color";
    public static final String PREF_RECV_TEXT_BG = "pref_recv_text_bg";
    public static final String PREF_RECV_SMILEY = "pref_recv_smiley";

    /**
     * Conversation List
     */
    // Background
    public static final String PREF_CONV_LIST_BG = "pref_conv_list_bg";
    public static final String CONV_CUSTOM_IMAGE = "conversation_list_image.jpg";

    // Text Size
    public static final String PREF_CONV_CONTACT_FONT_SIZE = "pref_conv_contact_font_size";
    public static final String PREF_CONV_FONT_SIZE = "pref_conv_font_size";
    public static final String PREF_CONV_DATE_FONT_SIZE = "pref_conv_date_font_size";

    // Color Read
    public static final String PREF_READ_BG = "pref_read_bg";
    public static final String PREF_READ_CONTACT = "pref_read_contact";
    public static final String PREF_READ_SUBJECT = "pref_read_subject";
    public static final String PREF_READ_DATE = "pref_read_date";
    public static final String PREF_READ_COUNT = "pref_read_count";
    public static final String PREF_READ_SMILEY = "pref_read_smiley";

    // Color Unread
    public static final String PREF_UNREAD_BG = "pref_unread_bg";
    public static final String PREF_UNREAD_CONTACT = "pref_unread_contact";
    public static final String PREF_UNREAD_SUBJECT = "pref_unread_subject";
    public static final String PREF_UNREAD_DATE = "pref_unread_date";
    public static final String PREF_UNREAD_COUNT = "pref_unread_count";
    public static final String PREF_UNREAD_SMILEY = "pref_unread_smiley";

    /**
     * Widget
     */
    // Layout style
    public static final String PREF_WIDGET_LAYOUT = "pref_widget_layout";

    /** With the way remoteviews works couldn't figure out a way yet to include this
     *  but leaving code in just in case for now.
     */
    // read/unread backgrounds
    // public static final String PREF_WIDGET_READ_BG = "pref_widget_bg_read";
    // public static final String PREF_WIDGET_UNREAD_BG = "pref_widget_bg_unread";

    // Text colors
    public static final String PREF_SENDERS_TEXTCOLOR_READ = "pref_senders_textcolor_read";
    public static final String PREF_SENDERS_TEXTCOLOR_UNREAD = "pref_senders_textcolor_unread";
    public static final String PREF_SUBJECT_TEXTCOLOR_READ = "pref_subject_textcolor_read";
    public static final String PREF_SUBJECT_TEXTCOLOR_UNREAD = "pref_subject_textcolor_unread";
    public static final String PREF_DATE_TEXTCOLOR_READ = "pref_date_textcolor_read";
    public static final String PREF_DATE_TEXTCOLOR_UNREAD = "pref_date_textcolor_unread";
}
