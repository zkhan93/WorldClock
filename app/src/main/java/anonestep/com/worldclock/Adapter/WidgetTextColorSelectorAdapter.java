package anonestep.com.worldclock.Adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import anonestep.com.worldclock.Listener.WidgetTextColorSelectorListener;
import anonestep.com.worldclock.R;
import anonestep.com.worldclock.ViewHolder.WidgetTextColorSelectorViewHolder;

/**
 * Created by Madhur Jain on 7/30/2017.
 */

public class WidgetTextColorSelectorAdapter extends RecyclerView.Adapter<WidgetTextColorSelectorViewHolder> {
    private int[] colorIds;
    private Context context;
    private WidgetTextColorSelectorListener widgetTextColorSelectorListener;

    public WidgetTextColorSelectorAdapter(Context context, int colorIds[]) {
        this.context = context;
        this.colorIds = colorIds;
    }


    public void setWidgetTextColorSelectorListener(WidgetTextColorSelectorListener widgetTextColorSelectorListener){
        this.widgetTextColorSelectorListener=widgetTextColorSelectorListener;
    }


    @Override
    public WidgetTextColorSelectorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_color_image, parent, false);
        return new WidgetTextColorSelectorViewHolder(view, widgetTextColorSelectorListener);

    }

    @Override
    public void onBindViewHolder(WidgetTextColorSelectorViewHolder holder, int position) {
        holder.colorImage.getBackground().setColorFilter(ContextCompat.getColor(context, colorIds[position]),
                PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public int getItemCount() {
        return colorIds.length;
    }
}
