package anonestep.com.worldclock.DbContract;

import android.provider.BaseColumns;

/**
 * Created by Madhur Jain on 4/23/2017.
 */

public class DbContract {

    private DbContract() {

    }

    public static final class TimeZoneEntry implements BaseColumns {
        public static final String TABLE_TIME_ZONE = "timeZone";
        public static final String TIME_ZONE_ID = "timeZone";
    }

    public static final class WidgetEntry implements BaseColumns {
        public static final String TABLE_WIDGET = "widgetTable";
        public static final String TIME_ZONE_ID = "timeZoneId";
        public static final String WIDGET_COLOR_ID = "color_id";
        public static final String WIDGET_FONT_ID = "text_id";

    }


}
