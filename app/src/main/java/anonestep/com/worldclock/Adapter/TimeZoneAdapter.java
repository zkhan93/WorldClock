package anonestep.com.worldclock.Adapter;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import anonestep.com.worldclock.DialogFragment.SaveConfirmationDialog;
import anonestep.com.worldclock.Listener.ListItemClickListener;
import anonestep.com.worldclock.R;
import anonestep.com.worldclock.ViewHolder.TimeZoneViewHolder;

/**
 * Created by Madhur Jain on 4/23/2017.
 */

public class TimeZoneAdapter extends RecyclerView.Adapter<TimeZoneViewHolder> implements ListItemClickListener {

    ArrayList<String> timeZoneIdList;
    FragmentManager fragmentManager;

    public TimeZoneAdapter(ArrayList timeZoneIdList, FragmentManager fragmentManager) {
        this.timeZoneIdList = timeZoneIdList;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public TimeZoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_zone_card, parent, false);
        return new TimeZoneViewHolder(view,this);
    }

    @Override
    public void onListItemClick(int position) {
        SaveConfirmationDialog saveConfirmationDialog = SaveConfirmationDialog.newInstance(timeZoneIdList.get(position));
        saveConfirmationDialog.show(fragmentManager, "Save Profile");
    }


    @Override
    public void onBindViewHolder(TimeZoneViewHolder holder, int position) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm E z");
        Date date = new Date();
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneIdList.get(position)));
        String dateString = dateFormat.format(date);
        String dateArray[] = dateString.split(" ");
        String displayName = TimeZone.getTimeZone(timeZoneIdList.get(position)).getDisplayName();
        String timeZone = timeZoneIdList.get(position);
        holder.displayNameTV.setText(displayName);
        holder.timeZoneIdTV.setText(timeZone);
        holder.dateTV.setText(dateArray[0]);
        holder.timeClock.setTimeZone(timeZoneIdList.get(position));
        holder.weekDay.setText(dateArray[2]);
    }

    @Override
    public int getItemCount() {
        if (timeZoneIdList == null)
            return 0;
        else
            return timeZoneIdList.size();
    }

    public void setFilter(ArrayList<String> zoneIdList) {
        timeZoneIdList = new ArrayList<>();
        timeZoneIdList.addAll(zoneIdList);
        notifyDataSetChanged();
    }
}
