package anonestep.com.worldclock.ViewHolder;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;

import anonestep.com.worldclock.Adapter.TimeZoneAdapter;
import anonestep.com.worldclock.Listener.ListItemClickListener;
import anonestep.com.worldclock.Listener.TimeZoneClickListener;
import anonestep.com.worldclock.R;

/**
 * Created by Madhur Jain on 4/23/2017.
 */

public class TimeZoneViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView displayNameTV, timeZoneIdTV, dateTV, weekDay;
    public TextClock timeClock;
    public TimeZoneClickListener itemClickListener;
    public String timeZoneId;

    public TimeZoneViewHolder(View itemView, TimeZoneClickListener itemClickListener) {
        super(itemView);
        this.itemClickListener = itemClickListener;
        displayNameTV = itemView.findViewById(R.id.displayName);
        timeZoneIdTV = itemView.findViewById(R.id.timeZoneId);
        dateTV = itemView.findViewById(R.id.date);
        timeClock = itemView.findViewById(R.id.timeClock);
        weekDay = itemView.findViewById(R.id.weekDay);
        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (itemClickListener != null)
            itemClickListener.onTimeZoneClick(timeZoneId);
    }

    public void setTimeZone(String timeZoneId) {
        this.timeZoneId = timeZoneId;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm E z");
        Date date = new Date();
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneId));
        String dateString = dateFormat.format(date);
        String dateArray[] = dateString.split(" ");
        String displayName = TimeZone.getTimeZone(timeZoneId).getDisplayName();

        displayNameTV.setText(displayName);
        timeZoneIdTV.setText(timeZoneId);
        dateTV.setText(dateArray[0]);
        timeClock.setTimeZone(timeZoneId);
        weekDay.setText(dateArray[2]);
    }
}
