package anonestep.com.worldclock.Adapter;

import android.content.Context;
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
import java.util.List;

import anonestep.com.worldclock.DialogFragment.SaveConfirmationDialog;
import anonestep.com.worldclock.Listener.ListItemClickListener;
import anonestep.com.worldclock.Listener.TimeZoneClickListener;
import anonestep.com.worldclock.R;
import anonestep.com.worldclock.ViewHolder.TimeZoneViewHolder;

/**
 * Created by Madhur Jain on 4/23/2017.
 */

public class TimeZoneAdapter extends RecyclerView.Adapter<TimeZoneViewHolder> {

    List<String> timeZoneIdList;
    TimeZoneClickListener itemClickListener;
    int layoutResource;

    public TimeZoneAdapter(List<String> timeZoneIdList, TimeZoneClickListener itemClickListener,
                           int layoutResource) {
        this.timeZoneIdList = timeZoneIdList;
        this.itemClickListener = itemClickListener;
        this.layoutResource = layoutResource;
    }

    @Override
    public TimeZoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResource, parent, false);
        return new TimeZoneViewHolder(view, itemClickListener);
    }


    @Override
    public void onBindViewHolder(TimeZoneViewHolder holder, int position) {
        holder.setTimeZone(timeZoneIdList.get(position));
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
