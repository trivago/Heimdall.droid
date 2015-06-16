package de.rheinfabrik.heimdalldroid.utils;

import android.content.Context;
import android.content.Intent;

import de.rheinfabrik.heimdalldroid.actvities.LoginActivity;

/**
 * Factory used to create Intents.
 */
public class IntentFactory {

    // Public Api

    /**
     * Creates an intent for showing the LoginActivity
     */
    public static Intent loginIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}
