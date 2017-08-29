package anonestep.com.worldclock;

import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import anonestep.com.worldclock.Adapter.TimeZoneTextStyleAdapter;
import anonestep.com.worldclock.Adapter.WidgetTextColorSelectorAdapter;
import anonestep.com.worldclock.DbContract.DbContract;
import anonestep.com.worldclock.DbContract.DbHelper;
import anonestep.com.worldclock.Listener.TextStyleSelectorListener;
import anonestep.com.worldclock.Listener.WidgetTextColorSelectorListener;

public class WidgetCustomizationActivity extends AppCompatActivity implements
        WidgetTextColorSelectorListener, TextStyleSelectorListener {

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
    TextView mTimeZone, mDate, mAmPm, mWeekDay;
    TextClock mTextClock;
    SQLiteDatabase mSqLiteDatabase;
    DbHelper dbHelper;
    int mAppWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_customization);


      /*  Intent intent = getIntent();
        String timeZoneId = intent.getStringExtra(DbContract.TimeZoneEntry.TIME_ZONE_ID);
        final int timeZoneDbId = intent.getIntExtra(DbContract.TimeZoneEntry._ID, 1);
*/
        mTimeZone = (TextView) findViewById(R.id.timeZone);
        mDate = (TextView) findViewById(R.id.date);
        mAmPm = (TextView) findViewById(R.id.am_pm);
        mWeekDay = (TextView) findViewById(R.id.weekDay);

        mTextClock = (TextClock) findViewById(R.id.time_clock);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mAppWidgetId = bundle.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm E a");
        Date date = new Date();
        String dateString = dateFormat.format(date);
        String dateArray[] = dateString.split(" ");

  /*      dateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneId));
        mAmPm.setText(dateArray[3] + " ");
        mWeekDay.setText(dateArray[2] + " ");
        mTextClock.setTimeZone(timeZoneId);
        mTimeZone.setText(TimeZone.getTimeZone(timeZoneId).getDisplayName());
        mDate.setText(dateArray[0]);
*/
        RecyclerView mTextColorRecyclerView = (RecyclerView) findViewById(R.id.text_color_recyclerview);
        mTextColorRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(),
                LinearLayoutManager.HORIZONTAL, false));
        WidgetTextColorSelectorAdapter textColorSelectorAdapter = new WidgetTextColorSelectorAdapter(getBaseContext(), colorIds);

        textColorSelectorAdapter.setWidgetTextColorSelectorListener(this);
        mTextColorRecyclerView.setAdapter(textColorSelectorAdapter);


        RecyclerView mTextStyleRecyclerView = (RecyclerView) findViewById(R.id.text_style_recyclerview);
        mTextStyleRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(),
                LinearLayoutManager.HORIZONTAL, false));
        TimeZoneTextStyleAdapter mTimeZoneTextStyleAdapter = new TimeZoneTextStyleAdapter(getBaseContext(), fonts);
        mTextStyleRecyclerView.setAdapter(mTimeZoneTextStyleAdapter);
        mTimeZoneTextStyleAdapter.setWidgetTextStycleClickListener(this);


        textColorSelectorAdapter.setWidgetTextColorSelectorListener(this);
        mTextColorRecyclerView.setAdapter(textColorSelectorAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
  /*              dbHelper = new DbHelper(getBaseContext());
                ContentValues contentValues = new ContentValues();
                contentValues.put(DbContract.WidgetEntry.TIME_ZONE_ID, timeZoneDbId);
                contentValues.put(DbContract.WidgetEntry.WIDGET_COLOR_ID, colorIds[colorPosition]);
                contentValues.put(DbContract.WidgetEntry.WIDGET_FONT_ID, fonts[fontPosition]);
                mSqLiteDatabase = dbHelper.getWritableDatabase();
                long id = mSqLiteDatabase.insert(DbContract.WidgetEntry.TABLE_WIDGET, null, contentValues);
                if (id != 0) {
                    Toast.makeText(getBaseContext(), "Widget Added", Toast.LENGTH_LONG).show();
                    mSqLiteDatabase.close();
                } else {
                    Toast.makeText(getBaseContext(), "Widget Not Added", Toast.LENGTH_LONG).show();
                }*/
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm E a");
                Date date = new Date();
                String dateString = dateFormat.format(date);
                String dateArray[] = dateString.split(" ");

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());
                RemoteViews views = new RemoteViews(getBaseContext().getPackageName(), R.layout.new_app_widget);

                views.setImageViewBitmap(R.id.timeZone,buildUpdate("India"));
                views.setImageViewBitmap(R.id.am_pm, buildUpdate(dateArray[3]));
                views.setTextViewText(R.id.weekDay, dateArray[2]);
                views.setTextViewText(R.id.date, dateArray[0]);
                appWidgetManager.updateAppWidget(mAppWidgetId, views);
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
    }



    public Bitmap buildUpdate(String time)
    {
        Bitmap myBitmap = Bitmap.createBitmap(160, 84, Bitmap.Config.ARGB_4444);
        Canvas myCanvas = new Canvas(myBitmap);
        Paint paint = new Paint();
        Typeface clock = Typeface.createFromAsset(this.getAssets(),"fonts/MonospaceTypewriter.ttf");
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(clock);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(colorIds[colorPosition]);
        paint.setTextSize(65);
        paint.setTextAlign(Paint.Align.CENTER);
        myCanvas.drawText(time, 80, 60, paint);
        return myBitmap;
    }


    @Override
    public void onWidgetTextColorSelect(int position) {
        colorPosition = position;
        mTimeZone.setTextColor(ContextCompat.getColor(getBaseContext(), colorIds[position]));
        mTextClock.setTextColor(ContextCompat.getColor(getBaseContext(), colorIds[position]));
        mDate.setTextColor(ContextCompat.getColor(getBaseContext(), colorIds[position]));
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
}

