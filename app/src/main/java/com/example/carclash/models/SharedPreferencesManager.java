package com.example.carclash.models;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static volatile SharedPreferencesManager instance = null;

    private static final String SP_FILE = "SP_FILE";
    private SharedPreferences sharedPref;

    private SharedPreferencesManager(Context context) {
        if (context != null) {
            this.sharedPref = context.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
        }
    }

    /**
     * Initializes the SharedPreferencesManager instance if not already initialized.
     * This method is thread-safe.
     *
     * @param context The application context.
     * @return The singleton instance of SharedPreferencesManager.
     */
    public static SharedPreferencesManager init(Context context) {
        if (instance == null) {
            synchronized (SharedPreferencesManager.class) {
                if (instance == null && context != null) {
                    instance = new SharedPreferencesManager(context.getApplicationContext());
                }
            }
        }
        return getInstance();
    }

    /**
     * Gets the singleton instance of SharedPreferencesManager.
     *
     * @return The singleton instance of SharedPreferencesManager.
     */
    public static SharedPreferencesManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SharedPreferencesManager is not initialized, call init(context) method first.");
        }
        return instance;
    }

    /**
     * Stores a string value in SharedPreferences.
     *
     * @param key   The key under which the value is stored.
     * @param value The string value to store.
     */
    public void putString(String key, String value) {
        if (sharedPref != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    /**
     * Retrieves a string value from SharedPreferences.
     *
     * @param key          The key whose value is to be retrieved.
     * @param defaultValue The default value to return if the key doesn't exist.
     * @return The string value associated with the key, or the default value if the key doesn't exist.
     */
    public String getString(String key, String defaultValue) {
        if (sharedPref != null) {
            return sharedPref.getString(key, defaultValue);
        }
        return defaultValue;
    }
}
