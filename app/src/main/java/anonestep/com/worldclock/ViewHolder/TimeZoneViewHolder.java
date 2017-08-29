package anonestep.com.worldclock.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import org.w3c.dom.Text;

import anonestep.com.worldclock.Adapter.TimeZoneAdapter;
import anonestep.com.worldclock.Listener.ListItemClickListener;
import anonestep.com.worldclock.R;

/**
 * Created by Madhur Jain on 4/23/2017.
 */

public class TimeZoneViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView displayNameTV, timeZoneIdTV, dateTV, weekDay;
    public TextClock timeClock;
    public ListItemClickListener listItemClickListener;

    public TimeZoneViewHolder(View itemView, ListItemClickListener listItemClickListener) {
        super(itemView);
        this.listItemClickListener = listItemClickListener;
        displayNameTV = (TextView) itemView.findViewById(R.id.displayName);
        timeZoneIdTV = (TextView) itemView.findViewById(R.id.timeZoneId);
        dateTV = (TextView) itemView.findViewById(R.id.date);
        timeClock = (TextClock) itemView.findViewById(R.id.timeClock);
        weekDay = (TextView) itemView.findViewById(R.id.weekDay);
        itemView.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        listItemClickListener.onListItemClick(getAdapterPosition());
    }
}
