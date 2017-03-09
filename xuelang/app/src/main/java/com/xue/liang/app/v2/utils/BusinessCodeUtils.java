package com.xue.liang.app.v2.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by jikun on 17/3/9.
 */

public class BusinessCodeUtils {

    private static final String TAG=BusinessCodeUtils.class.getSimpleName();

    public static final String USER_ID = "UserID";
    public static final String USER_PASSWORD = "UserPassword";
    public static final String USER_TOKEN = "UserToken";

    public static String getValue(Context context,String key) {
        String value = "";
        Uri uri = Uri.parse("content://stbconfig/summary");
        try {
            Cursor mCursor = context.getContentResolver().query(uri, null,
                    null, null, null);

            if (mCursor == null) {
                Log.e(TAG, "query error, cursor is null...");
                return "";
            }
            if (mCursor.moveToFirst()) {
                int index = 0;
                if (TextUtils.equals(key, USER_TOKEN)) {
                    index = mCursor.getColumnIndex(USER_TOKEN);
                } else if (TextUtils.equals(key, USER_ID)) {
                    index = mCursor.getColumnIndex(USER_ID);
                } else if (TextUtils.equals(key, USER_PASSWORD)) {
                    index = mCursor.getColumnIndex(USER_PASSWORD);
                }
                value = mCursor.getString(index);
                mCursor.close();
            }
        } catch (Exception e) {

        }
        return value;
    }
}
