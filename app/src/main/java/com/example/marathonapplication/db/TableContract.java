package com.example.marathonapplication.db;

import android.provider.BaseColumns;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Date;
import java.util.List;


public class TableContract {
    public TableContract(){}
    public static final class UserInfoEntry implements BaseColumns {

        public final static String TABLE_NAME = "login_user";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_USER_ID = "user_id";
        public final static String COLUMN_EMAIL = "email";
        public final static String COLUMN_USER_NAME = "user_name";
        public final static String COLUMN_USER_ADDRESS = "user_address";
        public final static String COLUMN_PASSWORD = "password";

    }
    public static final class RaceInfoEntry implements BaseColumns {

        public final static String TABLE_NAME = "race_info";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_RACE_NAME = "race_name";
        public final static String COLUMN_CREATOR = "creator_id";
        public final static String COLUMN_LINE_POINTS = "line_points";
        public final static String COLUMN_START_DATE = "start_date";
        public final static String COLUMN_END_DATE = "end_date";

    }

    public static final class RaceDetailEntry implements BaseColumns {

        public final static String TABLE_NAME = "race_detail";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_RACE_ID = "race_id";
        public final static String COLUMN_CREATOR = "creator_id";
        public final static String COLUMN_PARTICIPANT_ID = "participant_id";
        public final static String COLUMN_STEP_NUM = "step_num";
        public final static String COLUMN_RACE_STATUS = "race_status";
        public final static String COLUMN_RACE_COST_TIME = "race_cost_time";

    }
}
