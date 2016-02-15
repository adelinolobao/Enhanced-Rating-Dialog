package com.github.adelinolobao.enhanced_rating_dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

public class RatingDialog {

    public static void show(final Context context) {

        // TODO : implement logic behind showing the popup

        showDialog(context);
    }

    private static void showDialog(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.rating_dialog, null);

        builder.setView(view);

        // Positive feedback
        builder.setPositiveButton(R.string.dialog_rate_positive_button_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO : implement this logic...
            }
        });

        // Negative feedback
        builder.setNegativeButton(R.string.dialog_rate_negative_button_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO : implement this logic...
            }
        });


        // Neutral feedback
        builder.setNeutralButton(R.string.dialog_rate_neutral_button_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO : implement this logic...
            }
        });

        final AlertDialog alertDialog = builder.show();

        final RatingBar ratingBar = (RatingBar) alertDialog.findViewById(R.id.dialog_rating_bar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // TODO : implement this logic
            }
        });
    }
}
