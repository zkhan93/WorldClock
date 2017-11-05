package anonestep.com.worldclock;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import anonestep.com.worldclock.Adapter.TextStyleAdapter;
import anonestep.com.worldclock.Adapter.TimeZoneAdapter;
import anonestep.com.worldclock.Adapter.WidgetTextColorSelectorAdapter;
import anonestep.com.worldclock.Listener.TextStyleSelectorListener;
import anonestep.com.worldclock.Listener.TimeZoneClickListener;
import anonestep.com.worldclock.Listener.WidgetTextColorSelectorListener;

public class WidgetCustomizationActivity extends AppCompatActivity implements
        WidgetTextColorSelectorListener, TextStyleSelectorListener, TimeZoneClickListener {
    public static String TAG = WidgetCustomizationActivity.class.getSimpleName();
    public static String PREF_KEY_WIDGET_COLOR = "WIDGET_COLOR_%d";
    public static String PREF_KEY_WIDGET_FONT = "WIDGET_FONT_%d";
    public static String PREF_KEY_WIDGET_TIMZONE = "WIDGET_TIMEZONE_%d";
    public static String PREF_KEY_WIDGET_LABEL = "WIDGET_LABEL_%d";
    int[] colorIds = {
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent,
            R.color.white,
            R.color.black,
            R.color.red,
            R.color.blue,
            R.color.color1,
            R.color.color2,
            R.color.color3};

    String[] fonts = {
            "MonospaceTypewriter.ttf",
            "GrandHotel-Regular.otf", "LobsterTwo-Regular.otf", "Sofia-Regular.otf",
            "Android Insomnia Regular.ttf",
            "ANDROID ROBOT.ttf",
            "DroidSans.ttf",
            "DroidSans-Bold.ttf",
            "DroidSansMono.ttf",
            "DroidSerif-Bold.ttf",
            "DroidSerif-BoldItalic.ttf",
            "DroidSerif-Regular.ttf", "Fabrica.otf", "Fonty.ttf", "Fun " +
            "Raiser.ttf",
            "ROBACK.ttf",
            "Roboto-BoldCondensed.ttf",
            "Roboto-Light.ttf",
            "Roboto-Thin.ttf",
            "SemacCk_klop.otf",
            "Skyrimouski.ttf",
            "Xiomara-Script.ttf"};

    int colorPosition = 0, fontPosition = 0;
    String currentTimeZoneId;
    TextView mTimeZone, mDate, mAmPm, mWeekDay;
    EditText customLabel;
    TextClock mTextClock;
    int mAppWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_customization);


      /*  Intent intent = getIntent();
        String timeZoneId = intent.getStringExtra(DbContract.TimeZoneEntry.TIME_ZONE_ID);
        final int timeZoneDbId = intent.getIntExtra(DbContract.TimeZoneEntry._ID, 1);
//*/
        mTimeZone = findViewById(R.id.timeZone);
        mDate = findViewById(R.id.date);
        mAmPm = findViewById(R.id.am_pm);
        mWeekDay = findViewById(R.id.weekDay);
        mTextClock = findViewById(R.id.time_clock);
        customLabel = findViewById(R.id.customLabel);
        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mAppWidgetId = bundle.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Log.d(TAG, "confguration activity called for: " + mAppWidgetId);
        currentTimeZoneId = TimeZone.getDefault().getID();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy E a");
        dateFormat.setTimeZone(TimeZone.getTimeZone(currentTimeZoneId));

        String dateArray[] = dateFormat.format(new Date()).split(" ");

        mDate.setText(dateArray[0]);
        mWeekDay.setText(dateArray[1] + " ");
        mAmPm.setText(dateArray[2] + " ");
        mTextClock.setTimeZone(currentTimeZoneId);
        mTimeZone.setText(TimeZone.getTimeZone(currentTimeZoneId).getDisplayName());

        RecyclerView mTextColorRecyclerView = findViewById(R.id.text_color_recyclerview);
        mTextColorRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(),
                LinearLayoutManager.HORIZONTAL, false));
        WidgetTextColorSelectorAdapter textColorSelectorAdapter = new
                WidgetTextColorSelectorAdapter(getBaseContext(), colorIds);
        textColorSelectorAdapter.setWidgetTextColorSelectorListener(this);
        mTextColorRecyclerView.setAdapter(textColorSelectorAdapter);


        RecyclerView mTextStyleRecyclerView = findViewById(R.id.text_style_recyclerview);
        mTextStyleRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(),
                LinearLayoutManager.HORIZONTAL, false));
        TextStyleAdapter mTextStyleAdapter = new TextStyleAdapter
                (getBaseContext(), fonts);
        mTextStyleAdapter.setWidgetTextStycleClickListener(this);
        mTextStyleRecyclerView.setAdapter(mTextStyleAdapter);

        RecyclerView mTimezoneRecyclerView = findViewById(R.id.timezone_recyclerview);
        mTimezoneRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(),
                LinearLayoutManager.HORIZONTAL, false));
        List<String> timeZones = Arrays.asList(TimeZone
                .getAvailableIDs());
        TimeZoneAdapter mTimeZoneAdapter = new TimeZoneAdapter(timeZones,
                this);
        mTimezoneRecyclerView.setAdapter(mTimeZoneAdapter);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance
                        (WidgetCustomizationActivity.this);
                RemoteViews views = new RemoteViews(WidgetCustomizationActivity.this
                        .getApplicationContext()
                        .getPackageName(), R.layout.new_app_widget);
                Bitmap image = ClockWidget.getContentBitmap(WidgetCustomizationActivity.this,
                        getColor
                                (colorIds[colorPosition]),
                        fonts[fontPosition], currentTimeZoneId, customLabel.getText().toString());
                views.setImageViewBitmap(R.id.canvas, image);
                Intent intent = new Intent(WidgetCustomizationActivity.this,
                        WidgetCustomizationActivity.class);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                PendingIntent pendingIntent = PendingIntent.getActivity
                        (WidgetCustomizationActivity.this,
                                mAppWidgetId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                views.setOnClickPendingIntent(R.id.canvas, pendingIntent);
                Log.d(TAG, "saving color: " + getColor(colorIds[colorPosition]) + " for: " +
                        mAppWidgetId);
                PreferenceManager.getDefaultSharedPreferences(WidgetCustomizationActivity.this
                        .getApplicationContext())
                        .edit().putInt(String.format(PREF_KEY_WIDGET_COLOR, mAppWidgetId),
                        getColor(colorIds[colorPosition])).putString(String.format
                        (PREF_KEY_WIDGET_FONT,
                                mAppWidgetId), fonts[fontPosition]).putString(String.format
                        (PREF_KEY_WIDGET_TIMZONE, mAppWidgetId), currentTimeZoneId).putString
                        (String.format
                                (PREF_KEY_WIDGET_LABEL, mAppWidgetId), customLabel.getText()
                                .toString())
                        .commit();

                Log.d(TAG, "created onfig for widget: " + mAppWidgetId);
                appWidgetManager.updateAppWidget(mAppWidgetId, views);
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences
                (this.getApplicationContext());
        if (sharedPreferences.contains(String.format(PREF_KEY_WIDGET_COLOR, mAppWidgetId))) {
            long color = sharedPreferences.getInt(String.format(PREF_KEY_WIDGET_COLOR,
                    mAppWidgetId), -1);
            Log.d(TAG, "loaded color: " + color + " for: " + mAppWidgetId);
            for (int i = 0; i < colorIds.length; i++)
                if (getColor(colorIds[i]) == color) {
                    onWidgetTextColorSelect(i);
                    Log.d(TAG, "color was at: " + i);
                    break;
                }
        }

        String fontName = sharedPreferences.getString(String.format(PREF_KEY_WIDGET_FONT,
                mAppWidgetId),
                null);
        String label = sharedPreferences.getString(String.format(PREF_KEY_WIDGET_LABEL,
                mAppWidgetId),
                null);
        String timeZoneId = sharedPreferences.getString(String.format(PREF_KEY_WIDGET_TIMZONE,
                mAppWidgetId), null);

        if (fontName != null)
            for (int i = 0; i < fonts.length; i++)
                if (fontName.equals(fonts[i]))
                    OnTextStyleSelect(i);
        if (timeZoneId != null)
            onTimeZoneClick(timeZoneId);
        if (label != null)
            customLabel.setText(label);
    }

    @Override
    public void onWidgetTextColorSelect(int position) {
        colorPosition = position;
        mTimeZone.setTextColor(ContextCompat.getColor(getBaseContext(), colorIds[position]));
        mTextClock.setTextColor(ContextCompat.getColor(getBaseContext(), colorIds[position]));
        mDate.setTextColor(ContextCompat.getColor(getBaseContext(), colorIds[position]));
        mWeekDay.setTextColor(ContextCompat.getColor(getBaseContext(), colorIds[position]));
        mAmPm.setTextColor(ContextCompat.getColor(getBaseContext(), colorIds[position]));

    }

    @Override
    public void OnTextStyleSelect(int position) {
        fontPosition = position;
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/" + fonts[position]);
        mTextClock.setTypeface(custom_font);
        mTimeZone.setTypeface(custom_font);
        mDate.setTypeface(custom_font);
        mWeekDay.setTypeface(custom_font);
        mAmPm.setTypeface(custom_font);
    }

    @Override
    public void onTimeZoneClick(String timeZoneId) {
        currentTimeZoneId = timeZoneId;
        if (mTimeZone != null)
            mTimeZone.setText(timeZoneId);
    }
}

