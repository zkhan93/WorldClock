package anonestep.com.worldclock.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.nfc.Tag;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import anonestep.com.worldclock.DbContract.DbContract;
import anonestep.com.worldclock.DbContract.DbHelper;
import anonestep.com.worldclock.DialogFragment.DeleteConfirmation;
import anonestep.com.worldclock.Listener.ListItemClickListener;
import anonestep.com.worldclock.R;
import anonestep.com.worldclock.ViewHolder.SavedRecyclerViewHolder;

/**
 * Created by Madhur Jain on 4/23/2017.
 */

public class SavedTimeZoneAdapter extends RecyclerView.Adapter<SavedRecyclerViewHolder> {

    Cursor cursor;
    Context context;
    private static final String TAG = SavedTimeZoneAdapter.class.getSimpleName();
    FragmentManager fragmentManager;
    DeleteConfirmation.Listener listener;
    ListItemClickListener listItemClickListener;


    public SavedTimeZoneAdapter(Context context, Cursor cursor, FragmentManager fragmentManager) {
        this.cursor = cursor;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    public void setListener(DeleteConfirmation.Listener listener) {
        this.listener = listener;
    }

    public void setListItemClickListener(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    @Override
    public SavedRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_time_clock_card, parent, false);
        return new SavedRecyclerViewHolder(view, listItemClickListener);
    }

    @Override
    public void onBindViewHolder(SavedRecyclerViewHolder holder, final int position) {
        if (!cursor.moveToPosition(position))
            return;
        final String timeZoneStringId = cursor.getString(cursor.getColumnIndex(DbContract.TimeZoneEntry.TIME_ZONE_ID));
        holder.textClock.setTimeZone(timeZoneStringId);
        holder.analogClock.setTimeZone(timeZoneStringId);
        holder.optionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, position + " ", Toast.LENGTH_LONG).show();
                showPopMenu(v, timeZoneStringId, position);
            }
        });

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm E a");
        Date date = new Date();
        String dateString = dateFormat.format(date);
        String dateArray[] = dateString.split(" ");
        holder.mAmPm.setText(dateArray[3] + " ");
        holder.mDate.setText(dateArray[0] + " ");
        holder.mWeekday.setText(dateArray[2] + " ");
        holder.timeZone.setText(TimeZone.getTimeZone(timeZoneStringId).getDisplayName());

    }

    @Override
    public int getItemCount() {
        if (cursor == null)
            return 0;
        else
            return cursor.getCount();
    }


    public void resetAdapter(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public void removeItem() {

    }

    public void showPopMenu(View view, final String timeZoneStringId, final int position) {
        final PopupMenu popupMenu = new PopupMenu(context, view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.saved_time_option, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_delete) {
                    DeleteConfirmation deleteConfirmation = DeleteConfirmation.newInstance(timeZoneStringId, position);
                    deleteConfirmation.setListener(listener);
                    deleteConfirmation.show(fragmentManager, "Delete Confirmation");
                }
                return false;
            }
        });
        popupMenu.show();
    }

}
