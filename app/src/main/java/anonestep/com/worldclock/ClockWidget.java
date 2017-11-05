package anonestep.com.worldclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.Date;

import static anonestep.com.worldclock.WidgetCustomizationActivity.PREF_KEY_WIDGET_COLOR;
import static anonestep.com.worldclock.WidgetCustomizationActivity.PREF_KEY_WIDGET_FONT;
import static anonestep.com.worldclock.WidgetCustomizationActivity.PREF_KEY_WIDGET_LABEL;
import static anonestep.com.worldclock.WidgetCustomizationActivity.PREF_KEY_WIDGET_TIMZONE;

/**
 * Implementation of App Widget functionality.
 */
public class ClockWidget extends AppWidgetProvider {
    public static String TAG = ClockWidget.class.getSimpleName();

    public static RemoteViews updateAppWidget(Context context,
                                              int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getApplicationContext().getPackageName(), R
                .layout.new_app_widget);

        long colorCode;
        String fontName;
        String timeZoneId, label;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences
                (context.getApplicationContext());
        colorCode = sharedPreferences.getInt(String.format(PREF_KEY_WIDGET_COLOR, appWidgetId), R
                .color.white);
        fontName = sharedPreferences.getString(String.format(PREF_KEY_WIDGET_FONT, appWidgetId),
                null);
        label = sharedPreferences.getString(String.format(PREF_KEY_WIDGET_LABEL, appWidgetId),
                null);
        timeZoneId = sharedPreferences.getString(String.format(PREF_KEY_WIDGET_TIMZONE,
                appWidgetId), null);
        if (fontName != null && timeZoneId != null)
            views.setImageViewBitmap(R.id.canvas, getContentBitmap(context, (int)colorCode,
                    fontName, timeZoneId, label));
        return views;
    }

    protected static Bitmap getContentBitmap(Context context, int color, String fontName, String
            timeZoneId, String label) {
        Bitmap myBitmap = Bitmap.createBitmap(400, 320, Bitmap.Config.ARGB_4444);
        Canvas myCanvas = new Canvas(myBitmap);
        Paint paint = new Paint();
        Typeface font = Typeface.createFromAsset(context.getAssets(), String.format("fonts/%s",
                fontName));
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(font);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        //end common attributes

        paint.setTextAlign(Paint.Align.CENTER);
        String timezone = timeZoneId;
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy-HH:mm-E-a");
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneId));

        String dateArray[] = dateFormat.format(new Date()).split("-");

        String date = dateArray[0].trim();
        String time = dateArray[1].trim();
        String day = dateArray[2].trim();
        String period = dateArray[3].trim();
        boolean labelPresent = !label.isEmpty();
        int scaleText = 46;
        Rect bounds = new Rect();
        int vgap = Math.round(paint.getFontSpacing()), hgap = 10, x = myCanvas.getWidth() / 2, y;
        int height;
        String textString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        //reduce scale if total height exceeds the canvas height
        do {
            scaleText -= 1;
            paint.setTextSize(2 * scaleText);
            paint.getTextBounds(textString, 0, textString.length(), bounds);
            int largeTextHeight = bounds.height();

            paint.setTextSize(scaleText);
            paint.getTextBounds(textString, 0, textString.length(), bounds);
            height = bounds.height() * (labelPresent ? 4 : 3) + largeTextHeight + vgap *
                    (labelPresent ? 4 : 3);
        } while (height >= myCanvas.getHeight());

        int sizeTimeZone = 1 * scaleText;
        int sizeDate = 1 * scaleText;
        int sizeTime = 2 * scaleText;
        int sizePeriod = Math.round(0.8f * scaleText);
        int sizeDay = 1 * scaleText;
        int sizeLabel = 1 * scaleText;

        paint.setTextSize(sizeTimeZone);
        paint.getTextBounds(timezone, 0, timezone.length(), bounds);
        paint.setTextScaleX(getTextScale(myCanvas.getWidth(), bounds.width()));
        y = (myCanvas.getHeight() - height) / 2;
        y += bounds.height();
        myCanvas.drawText(timezone, x, y, paint);


        paint.setTextSize(sizeTime);
        paint.getTextBounds(time, 0, time.length(), bounds);
        paint.setTextScaleX(getTextScale(myCanvas.getWidth(), bounds.width()));
        y += vgap + bounds.height();
        myCanvas.drawText(time, x, y, paint);


        paint.setTextSize(sizePeriod);
        x = myCanvas.getWidth() / 2 + bounds.width() / 2;
        paint.getTextBounds(period, 0, period.length(), bounds);
        paint.setTextScaleX(getTextScale(myCanvas.getWidth() - x - 2 * hgap, bounds.width()));
        x += bounds.width() / 2 + 2 * hgap;
        myCanvas.drawText(period, x, y, paint);

        x = myCanvas.getWidth() / 2;
        paint.setTextSize(sizeDay);
        paint.getTextBounds(day, 0, day.length(), bounds);
        paint.setTextScaleX(getTextScale(myCanvas.getWidth(), bounds.width()));
        y += bounds.height() + vgap;
        myCanvas.drawText(day, x, y, paint);

        paint.setTextSize(sizeDate);
        paint.getTextBounds(date, 0, date.length(), bounds);
        paint.setTextScaleX(getTextScale(myCanvas.getWidth(), bounds.width()));
        y += bounds.height() + vgap;
        myCanvas.drawText(date, x, y, paint);

        if (labelPresent) {
            paint.setTextSize(sizeLabel);
            paint.getTextBounds(label, 0, label.length(), bounds);
            paint.setTextScaleX(getTextScale(myCanvas.getWidth(), bounds.width()));
            y += bounds.height() + vgap;
            myCanvas.drawText(label, x, y, paint);
        }
        return myBitmap;
    }

    private static float getTextScale(int maxWidth, int textWidth) {
        float scale = 1f;
        while (textWidth * scale > maxWidth)
            scale -= 0.1;
        return scale;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Log.d(TAG, "update called for widget: " + appWidgetId);
            RemoteViews views = updateAppWidget(context, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.d(TAG, "widget Enabled");
        setAlarm(context);
        super.onEnabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        SharedPreferences.Editor sharedPreferencesEditor = PreferenceManager
                .getDefaultSharedPreferences
                (context).edit();
        for (int appWidgetId : appWidgetIds) {
            sharedPreferencesEditor.remove(String.format(PREF_KEY_WIDGET_COLOR, appWidgetId))
                    .remove(String.format(PREF_KEY_WIDGET_FONT, appWidgetId))
                    .remove(String.format(PREF_KEY_WIDGET_LABEL, appWidgetId))
                    .remove(String.format(PREF_KEY_WIDGET_TIMZONE, appWidgetId));
        }
        sharedPreferencesEditor.apply();
    }

    private void setAlarm(Context context) {
        final Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        final PendingIntent pending = PendingIntent.getBroadcast
                (context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        int interval = 60000;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC, ((System.currentTimeMillis() / interval) * interval + interval), pending);
            Log.d(TAG, String.format("repeating alarm set on %d", Calendar.getInstance().getTimeInMillis() + interval));
        }
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.d(TAG, "widget disabled");
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "update action received: " + intent.getAction() + " " + System
                .currentTimeMillis());
        if (intent.getAction() != null && intent.getAction().equals(AppWidgetManager
                .ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService
                    (Context.APPWIDGET_SERVICE);
            int[] appwidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context,
                    ClockWidget.class));
            onUpdate(context, appWidgetManager, appwidgetIds);
            setAlarm(context);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int
            appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }
}

