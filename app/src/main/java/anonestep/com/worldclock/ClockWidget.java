package anonestep.com.worldclock;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class ClockWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        Calendar calendar = Calendar.getInstance();
        views.setTextViewText(R.id.time_clock, calendar.getTimeZone().getDisplayName());
        views.setTextViewText(R.id.timeZone,calendar.getTimeZone().getID());
        views.setTextViewText(R.id.am_pm,"AM");
        views.setTextViewText(R.id.weekDay,"Tue");
        views.setTextViewText(R.id.date,calendar.get(Calendar.DAY_OF_WEEK)+" "+calendar.get(Calendar.DATE));
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

