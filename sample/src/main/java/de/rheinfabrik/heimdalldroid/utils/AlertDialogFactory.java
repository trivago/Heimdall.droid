package de.rheinfabrik.heimdalldroid.utils;

import android.app.AlertDialog;
import android.content.Context;

import de.rheinfabrik.heimdalldroid.R;

/**
 * Factory used to create AlertDialogs.
 */
public class AlertDialogFactory {

    // Public Api

    /**
     * Creates an error dialog.
     */
    public static AlertDialog errorAlertDialog(Context context) {
        return new AlertDialog.Builder(context)
                .setTitle(R.string.error_title)
                .setMessage(R.string.error_message)
                .setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss()).create();
    }

    /**
     * Creates an dialog informing the user that there are no liked lists found.
     */
    public static AlertDialog noListsFoundDialog(Context context) {
        return new AlertDialog.Builder(context)
                .setTitle(R.string.error_title)
                .setMessage(R.string.no_liked_lists_message)
                .setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss()).create();
    }

}
