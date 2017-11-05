package anonestep.com.worldclock.Adapter;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import anonestep.com.worldclock.Listener.TextStyleSelectorListener;
import anonestep.com.worldclock.R;
import anonestep.com.worldclock.ViewHolder.TimeZoneTextStyleViewHolder;
import anonestep.com.worldclock.WidgetCustomizationActivity;

/**
 * Created by Madhur Jain on 8/21/2017.
 */

public class TextStyleAdapter extends RecyclerView.Adapter<TimeZoneTextStyleViewHolder> {

    String[] fontStyle;
    private TextStyleSelectorListener mTextStyleClickListener;
    Context context;
    String mTimeZoneId;

    public TextStyleAdapter(Context context, String[] fontStyle) {
        this.fontStyle = fontStyle;
        this.context = context;
        //this.mTimeZoneId =mTimeZoneId;
    }

    @Override
    public TimeZoneTextStyleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_clock_style, parent, false);
        return new TimeZoneTextStyleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TimeZoneTextStyleViewHolder holder, final int position) {
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/"+fontStyle[position]);
        holder.mTextClock.setTypeface(custom_font);
        //holder.mTextClock.setTimeZone(mTimeZoneId);
        holder.mTextClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextStyleClickListener.OnTextStyleSelect(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("MADHUR", fontStyle.length + " ");
        return fontStyle.length;
    }

    public void setWidgetTextStycleClickListener(TextStyleSelectorListener mTextStyleClickListener) {
        this.mTextStyleClickListener = mTextStyleClickListener;
    }


}
