package com.julian.jotihuntdidam.Logics;

import android.content.Context;
import android.preference.PreferenceManager;

import com.julian.jotihuntdidam.R;

/**
 * Created by jesse on 04/01/2016.
 * used to save and load stuff to and from the preference manager
 */
public class Credentials {

    public static void saveUsername(Context ctx, String username) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString(ctx.getString(R.string.pref_username), username).commit();
    }

    public static String loadUsername(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(ctx.getString(R.string.pref_username), "");
    }

    public static void clearUsername(Context ctx) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().remove(ctx.getString(R.string.pref_username));
    }

    public static void savePassword(Context ctx, String username) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString(ctx.getString(R.string.pref_password), username).commit();
    }

    public static String loadPassword(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(ctx.getString(R.string.pref_password), "");
    }

    public static void clearPassword(Context ctx) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().remove(ctx.getString(R.string.pref_password));
    }

    public static void saveRememberPreference(Context ctx, boolean pref) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putBoolean(ctx.getString(R.string.pref_remember), pref).commit();
    }

    public static boolean loadRememberPreference(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean(ctx.getString(R.string.pref_remember), false);
    }

    public static boolean isLoggedIn(Context ctx) {
        return loadPassword(ctx).equals("");
    }
}
