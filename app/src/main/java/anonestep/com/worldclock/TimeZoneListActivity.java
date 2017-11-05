package anonestep.com.worldclock;

import android.icu.util.TimeZone;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Arrays;

import anonestep.com.worldclock.Adapter.TimeZoneAdapter;
import anonestep.com.worldclock.DialogFragment.SaveConfirmationDialog;
import anonestep.com.worldclock.Listener.TimeZoneClickListener;

public class TimeZoneListActivity extends AppCompatActivity implements SearchView
        .OnQueryTextListener, TimeZoneClickListener {

    RecyclerView timeZoneRecyclerView;
    TimeZoneAdapter adapter;
    ArrayList<String> timeZoneIdList;
    SearchView searchView;
    private static final String TAG = TimeZoneListActivity.class.getSimpleName();
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_zone_list);
        adView = (AdView) findViewById(R.id.time_zone_list_AdView);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice
                ("C4CA5449817AAA2AB35F00AED48D06E0")
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);

        timeZoneIdList = new ArrayList<>();
        timeZoneIdList.addAll(Arrays.asList(TimeZone.getAvailableIDs()));
        searchView = (SearchView) findViewById(R.id.searchBar);
        searchView.setOnQueryTextListener(this);
        timeZoneRecyclerView = (RecyclerView) findViewById(R.id.timezoneRecyclerView);
        timeZoneRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        adapter = new TimeZoneAdapter(timeZoneIdList, this,R.layout.time_zone_card);
        timeZoneRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onTimeZoneClick(String timeZoneId) {
        SaveConfirmationDialog saveConfirmationDialog = SaveConfirmationDialog.newInstance
                (timeZoneId);
        saveConfirmationDialog.show(getSupportFragmentManager(), "Save Profile");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        query = query.toLowerCase();
        ArrayList<String> zoneIdList = new ArrayList<>();
        for (String timeZone : timeZoneIdList) {
            String displayName = TimeZone.getTimeZone(timeZone).getDisplayName().toLowerCase();
            if (displayName.contains(query)) {
                zoneIdList.add(timeZone);
            } else if (timeZone.toLowerCase().contains(query)) {
                zoneIdList.add(timeZone);
            }
        }
        adapter.setFilter(zoneIdList);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<String> zoneIdList = new ArrayList<>();
        Log.d(TAG, timeZoneIdList.size() + " ");
        for (String timeZone : timeZoneIdList) {
            String displayName = TimeZone.getTimeZone(timeZone).getDisplayName().toLowerCase();
            if (displayName.contains(newText)) {
                zoneIdList.add(timeZone);
            } else if (timeZone.toLowerCase().contains(newText)) {
                zoneIdList.add(timeZone);
            }
        }
        adapter.setFilter(zoneIdList);
        return true;
    }


}

