package anonestep.com.worldclock.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import anonestep.com.worldclock.AnalogClock;
import anonestep.com.worldclock.Listener.ListItemClickListener;
import anonestep.com.worldclock.R;

/**
 * Created by Madhur Jain on 4/23/2017.
 */

public class SavedRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextClock textClock;
    public AnalogClock analogClock;
    public TextView timeZone,mAmPm,mDate,mWeekday;
    public ImageView optionImage;
    public ListItemClickListener listItemClickListener;


    public SavedRecyclerViewHolder(View itemView, ListItemClickListener listItemClickListener) {
        super(itemView);
        this.listItemClickListener = listItemClickListener;
        textClock = (TextClock) itemView.findViewById(R.id.textClock);
        optionImage = (ImageView) itemView.findViewById(R.id.moreOption);
        analogClock = (AnalogClock) itemView.findViewById(R.id.simpleAnalogClock);
        timeZone = (TextView) itemView.findViewById(R.id.savedTimeZone);
        mAmPm=(TextView)itemView.findViewById(R.id.am_pm);
        mDate=(TextView)itemView.findViewById(R.id.date);
        mWeekday=(TextView)itemView.findViewById(R.id.weekday);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listItemClickListener.onListItemClick(getAdapterPosition());
    }
}
