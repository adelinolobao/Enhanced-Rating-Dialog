package com.github.adelinolobao.enhanced_rating_dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

public class EnhancedRatingDialog {
    private static final String TAG = EnhancedRatingDialog.class.getSimpleName();

    private static final String SHARED_PREFERENCE_KEY = "ENHANCED_RATING_DIALOG_SHARE_PREFERENCE_KEY";

    private static final String TRACK_COUNT_APPLICATION_OPEN = "ENHANCED_RATING_DIALOG_TRACK_COUNT_APPLICATION_OPEN";

    private static final String TRACK_SHOW_DIALOG = "ENHANCED_RATING_DIALOG_TRACK_SHOW_DIALOG";

    private static final String TRACK_DO_NOT_SHOW_AGAIN = "ENHANCED_RATING_DIALOG_TRACK_DO_NOT_SHOW_AGAIN";

    public static void show(final Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_KEY, 0);

        // Check if is necessary to show rating popup
        final boolean showDialog = sharedPreferences.getBoolean(TRACK_SHOW_DIALOG, true);
        if (!showDialog) {
            Log.d(TAG, "Not showing dialog rating popup");
            return;
        }

        final SharedPreferences.Editor editor = sharedPreferences.edit();

        // Count the number of times application is open
        final long amountOfTimesOpen = sharedPreferences.getLong(TRACK_COUNT_APPLICATION_OPEN, 0) + 1;
        editor.putLong(TRACK_COUNT_APPLICATION_OPEN, amountOfTimesOpen).apply();

        /**
         * If user select NO:
         * 	- check if application 'dialog_rate_interval_reset' times and reset the application popup
         * 	- if not do not show popup
         */
        final int resetInterval = Integer.parseInt(context.getString(R.string.dialog_rate_interval_reset));
        if (sharedPreferences.getBoolean(TRACK_DO_NOT_SHOW_AGAIN, false)) {
            if ((amountOfTimesOpen % resetInterval) == 0) {
                editor.putBoolean(TRACK_DO_NOT_SHOW_AGAIN, false).apply();
            } else {
                return;
            }
        }

        // Check if application was open X amount of times
        final int dialogRateInterval = Integer.parseInt(context.getString(R.string.dialog_rate_interval));
        if (amountOfTimesOpen % dialogRateInterval == 0) {
            showDialog(context, editor);
        }
    }

    private static void showDialog(final Context context, final SharedPreferences.Editor editor) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.rating_dialog, null);

        builder.setView(view);

        // Positive feedback
        builder.setPositiveButton(R.string.dialog_rate_positive_button_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Make sure the dialog does not appear
                editor.putBoolean(TRACK_SHOW_DIALOG, false).apply();
                // Opens the app store
                final Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
                // Dismiss dialog
                dialog.dismiss();
            }
        });

        // Negative feedback
        builder.setNegativeButton(R.string.dialog_rate_negative_button_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editor.putBoolean(TRACK_DO_NOT_SHOW_AGAIN, true).apply();
                dialog.dismiss();
            }
        });


        // Neutral feedback
        builder.setNeutralButton(R.string.dialog_rate_neutral_button_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog alertDialog = builder.show();

        final RatingBar ratingBar = (RatingBar) alertDialog.findViewById(R.id.dialog_rating_bar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                final int storeLimitRating = Integer.parseInt(context.getString(R.string.dialog_rate_store_limit));
                if (rating >= storeLimitRating) {
                    // Make sure the dialog does not appear
                    editor.putBoolean(TRACK_SHOW_DIALOG, false).apply();
                    // Opens the app store
                    final Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                    final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                } else {
                    editor.putBoolean(TRACK_DO_NOT_SHOW_AGAIN, true).apply();
                    // Send the email
                    final Uri uri = Uri.parse("mailto:" + context.getString(R.string.dialog_rate_email_support));
                    final Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                    final String subject = context.getString(R.string.dialog_rate_email_subject) + " (" + context.getPackageName() + ")";
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    context.startActivity(Intent.createChooser(intent, "Send mail..."));
                }
                alertDialog.dismiss();
            }
        });
    }
}
