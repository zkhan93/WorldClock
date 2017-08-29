package anonestep.com.worldclock.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextClock;

import anonestep.com.worldclock.R;

/**
 * Created by Madhur Jain on 8/21/2017.
 */

public class TimeZoneTextStyleViewHolder extends RecyclerView.ViewHolder {

    public TextClock mTextClock;

    public TimeZoneTextStyleViewHolder(View itemView) {
        super(itemView);
        mTextClock = (TextClock) itemView.findViewById(R.id.textClock);
    }
}
