package anonestep.com.worldclock.DialogFragment;

import android.app.Dialog;
import android.content.ContentValues;
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
import anonestep.com.worldclock.R;

/**
 * Created by Madhur Jain on 4/24/2017.
 */

public class DeleteConfirmation extends DialogFragment {

    private static final String TIME_ZONE_STRING_ID = "TIME_ZONE_STRING_ID";
    private static final String TIME_ZONE_INT_ID = "TIME_ZONE_INT_ID";
    Listener listener;

    public static DeleteConfirmation newInstance(String timeZoneStringId, int position) {
        DeleteConfirmation deleteConfirmation = new DeleteConfirmation();
        Bundle bundle = new Bundle();
        bundle.putString(TIME_ZONE_STRING_ID, timeZoneStringId);
        bundle.putInt(TIME_ZONE_INT_ID, position);
        deleteConfirmation.setArguments(bundle);
        return deleteConfirmation;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        String timeZoneId = getArguments().getString(TIME_ZONE_STRING_ID);
        final int position = getArguments().getInt(TIME_ZONE_INT_ID);
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.save_confirmation_dialog);
        TextClock textClock = (TextClock) dialog.findViewById(R.id.textClock);
        textClock.setTimeZone(timeZoneId);
        TextView savedTimeZone = (TextView) dialog.findViewById(R.id.savedTimeZone);
        savedTimeZone.setText(TimeZone.getTimeZone(timeZoneId).getDisplayName());
        final Button deleteConfirm = (Button) dialog.findViewById(R.id.addToFavourite);
        deleteConfirm.setText("Delete");
        Button cancel = (Button) dialog.findViewById(R.id.cancel);

        deleteConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.confirmDelete(position);
                dialog.cancel();
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

    public interface Listener {
        void confirmDelete(int count);
    }


}
