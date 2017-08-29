package anonestep.com.worldclock.ViewHolder;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import anonestep.com.worldclock.Listener.WidgetTextColorSelectorListener;
import anonestep.com.worldclock.R;

/**
 * Created by Madhur Jain on 7/30/2017.
 */

public class WidgetTextColorSelectorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public AppCompatButton colorImage;
    public WidgetTextColorSelectorListener widgetTextColorSelectorListener;


    public WidgetTextColorSelectorViewHolder(View itemView, WidgetTextColorSelectorListener widgetTextColorSelectorListener) {
        super(itemView);
        this.widgetTextColorSelectorListener = widgetTextColorSelectorListener;
        colorImage = (AppCompatButton)itemView.findViewById(R.id.colorImage);
        colorImage.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        widgetTextColorSelectorListener.onWidgetTextColorSelect(getAdapterPosition());
    }
}
