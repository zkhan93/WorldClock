package anonestep.com.worldclock;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import java.util.Calendar;

import anonestep.com.worldclock.Adapter.SavedTimeZoneAdapter;
import anonestep.com.worldclock.DbContract.DbContract;
import anonestep.com.worldclock.DbContract.DbHelper;
import anonestep.com.worldclock.DialogFragment.DeleteConfirmation;
import anonestep.com.worldclock.Listener.ListItemClickListener;

public class MainActivity extends AppCompatActivity implements DeleteConfirmation.Listener, ListItemClickListener {

    TextClock textClock;
    TextView timeZoneTV;
    RecyclerView saveTimeRecyclerView;
    SavedTimeZoneAdapter adapter;
    DbHelper dbHelper;
    SQLiteDatabase mSqLiteDatabase;
    Cursor cursor;
    ImageView actionSettingImage;
    private static final String TAG = MainActivity.class.getSimpleName();
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(getBaseContext());
        mSqLiteDatabase = dbHelper.getReadableDatabase();
        actionSettingImage = findViewById(R.id.actionSettings);
        timeZoneTV = findViewById(R.id.defaulttimezone);
        textClock = findViewById(R.id.textClock);
        Calendar cal = Calendar.getInstance();
        timeZoneTV.setText(cal.getTimeZone().getDisplayName());

/*
        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("C4CA5449817AAA2AB35F00AED48D06E0")
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);
*/
        int gridCount = getResources().getInteger(R.integer.grid_count);

        saveTimeRecyclerView = (RecyclerView) findViewById(R.id.savedTimeZone);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getBaseContext(), gridCount);
        saveTimeRecyclerView.setLayoutManager(layoutManager);
        adapter = new SavedTimeZoneAdapter(this, getSaveTimeZoneList(), getSupportFragmentManager());
        adapter.setListener(this);
        adapter.setListItemClickListener(this);
        saveTimeRecyclerView.setAdapter(adapter);
        actionSettingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingPopMenu(v);
            }
        });

        ImageView fab = (ImageView) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), TimeZoneListActivity.class));
            }
        });
    }


    public void showSettingPopMenu(View view) {
        final PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_settings) {
                    startActivity(new Intent(getBaseContext(), SettingActivity.class));
                }
                return false;
            }
        });
        popupMenu.show();


    }


    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.resetAdapter(getSaveTimeZoneList());
    }


    public Cursor getSaveTimeZoneList() {
        cursor = mSqLiteDatabase.query(DbContract.TimeZoneEntry.TABLE_TIME_ZONE,
                null,
                null,
                null,
                null,
                null,
                DbContract.TimeZoneEntry._ID);
        return cursor;

    }

    @Override
    public void confirmDelete(int position) {
        if (cursor != null) {
            cursor.moveToPosition(position);
            int timeZoneId = cursor.getInt(cursor.getColumnIndex(DbContract.TimeZoneEntry._ID));
            mSqLiteDatabase = dbHelper.getWritableDatabase();
            mSqLiteDatabase.delete(DbContract.TimeZoneEntry.TABLE_TIME_ZONE, DbContract.TimeZoneEntry._ID + "=" + timeZoneId, null);
            adapter.notifyItemRemoved(position);
            adapter.resetAdapter(getSaveTimeZoneList());
        }
    }

    @Override
    public void onListItemClick(int position) {
        if (cursor.moveToPosition(position)) {
            Intent intent = new Intent(getBaseContext(), WidgetCustomizationActivity.class);
            intent.putExtra(DbContract.TimeZoneEntry.TIME_ZONE_ID, cursor.getString(cursor.getColumnIndex(DbContract.TimeZoneEntry.TIME_ZONE_ID)));
            intent.putExtra(DbContract.TimeZoneEntry._ID,cursor.getString(cursor.getColumnIndex(DbContract.TimeZoneEntry._ID)));
            startActivity(intent);
        }
    }
}
