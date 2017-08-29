package anonestep.com.worldclock.DialogFragment;


import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import anonestep.com.worldclock.DbContract.DbContract;
import anonestep.com.worldclock.DbContract.DbHelper;
import anonestep.com.worldclock.MainActivity;
import anonestep.com.worldclock.R;

/**
 * Created by Madhur Jain on 4/24/2017.
 */

public class SaveConfirmationDialog extends DialogFragment {

    SQLiteDatabase mSqLiteDatabase;
    DbHelper dbHelper;

    private static final String TIME_ZONE_ID = "timeZoneID";

    public static SaveConfirmationDialog newInstance(String timeZoneId) {
        SaveConfirmationDialog saveConfirmationDialog = new SaveConfirmationDialog();
        Bundle bundle = new Bundle();
        bundle.putString(TIME_ZONE_ID, timeZoneId);
        saveConfirmationDialog.setArguments(bundle);
        return saveConfirmationDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        dbHelper = new DbHelper(getActivity());

        final String timeZoneId = getArguments().getString(TIME_ZONE_ID);
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.save_confirmation_dialog);
        TextClock textClock = (TextClock) dialog.findViewById(R.id.textClock);
        textClock.setTimeZone(timeZoneId);
        TextView savedTimeZone = (TextView) dialog.findViewById(R.id.savedTimeZone);
        savedTimeZone.setText(TimeZone.getTimeZone(timeZoneId).getDisplayName());
        Button addToFavourite = (Button) dialog.findViewById(R.id.addToFavourite);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);

        addToFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DbContract.TimeZoneEntry.TIME_ZONE_ID, timeZoneId);
                mSqLiteDatabase = dbHelper.getWritableDatabase();
                long id = mSqLiteDatabase.insert(DbContract.TimeZoneEntry.TABLE_TIME_ZONE, null, contentValues);
                Toast.makeText(getActivity(), id + " ", Toast.LENGTH_LONG).show();
                dialog.cancel();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        return dialog;


    }
}
