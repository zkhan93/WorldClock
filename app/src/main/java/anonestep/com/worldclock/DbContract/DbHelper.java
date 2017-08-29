package anonestep.com.worldclock.DbContract;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Madhur Jain on 4/23/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "timezone.db";
    private static final int DATABASE_VERSION = 2;


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TIME_ZONE_ENTRY_TABLE = " CREATE TABLE " +
                DbContract.TimeZoneEntry.TABLE_TIME_ZONE + "( " +
                DbContract.TimeZoneEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbContract.TimeZoneEntry.TIME_ZONE_ID + " TEXT NOT NULL );";

        final String SQL_CREATE_WIDGET_ENTRY_TABLE = " CREATE TABLE " +
                DbContract.WidgetEntry.TABLE_WIDGET+ "( " +
                DbContract.WidgetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbContract.WidgetEntry.WIDGET_COLOR_ID+" INTEGER NOT NULL, "+
                DbContract.WidgetEntry.WIDGET_FONT_ID+" TEXT NOT NULL, "+
                DbContract.WidgetEntry.TIME_ZONE_ID + " TEXT NOT NULL, " +
                "FOREIGN KEY("+DbContract.WidgetEntry.TIME_ZONE_ID+") REFERENCES " +
                DbContract.TimeZoneEntry.TABLE_TIME_ZONE +"("+DbContract.WidgetEntry.TIME_ZONE_ID +"));";

        db.execSQL(SQL_CREATE_TIME_ZONE_ENTRY_TABLE);
        db.execSQL(SQL_CREATE_WIDGET_ENTRY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String SQL_DROP_TIMEZONE_TABLE = " DROP TABLE " + DbContract.TimeZoneEntry.TABLE_TIME_ZONE;
        db.execSQL(SQL_DROP_TIMEZONE_TABLE);
        onCreate(db);
    }
    
}
