package xyz.cybersapien.habittracker.data;

import android.provider.BaseColumns;

/**
 * Created by ogcybersapien on 13/10/16.
 * API Contract for the Habits in the app.
 */

public final class HabitContract {

    //prevent initialization of the app with an empty constructor.
    private HabitContract(){}

    /**
     * Inner class that defines the constant values for the habits database table.
     * Each entry in the table represents a single habit
     */

    public static final class HabitEntry implements BaseColumns{

        public static final String TABLE_NAME = "habits";

        /**
         * Unique ID number for the habit
         * Type: Integer
         */
        public static final String _ID = BaseColumns._ID;

        /**
         * Name of the habit
         * Type: TEXT
         */
        public static final String COLUMN_HABIT_NAME = "name";

        /**
         * If the habit was performed on this monday
         * Type: INTEGER
         */
        public static final String COLUMN_HABIT_MONDAY = "monday";

        /**
         * If the habit was performed on this tuesday
         * Type: INTEGER
         */
        public static final String COLUMN_HABIT_TUESDAY = "tuesday";

        /**
         * If the habit was performed on this wednesday
         * Type: INTEGER
         */
        public static final String COLUMN_HABIT_WEDNESDAY = "wednesday";

        /**
         * If the habit was performed on this friday
         * Type: INTEGER
         */
        public static final String COLUMN_HABIT_THURSDAY = "thursday";

        /**
         * If the habit was performed on this friday
         * Type: INTEGER
         */
        public static final String COLUMN_HABIT_FRIDAY = "friday";

        /**
         * If the habit was performed on this saturday
         * Type: INTEGER
         */
        public static final String COLUMN_HABIT_SATURDAY = "saturday";

        /**
         * If the habit was performed on this sunday
         * Type: INTEGER
         */
        public static final String COLUMN_HABIT_SUNDAY = "sunday";

        /*VALUE FOR TRUE FOR THE DAY OF THE WEEK*/
        public static final int DONE_THIS_DAY = 1;

        /*VALUE FOR FALSE FOR THE DAY OF THE WEEK*/
        public static final int NOT_DONE_THIS_DAY = 0;

        /**
         * Number of previous weeks the habit was performed continously for
         * Type: INTEGER
         */
        public static final String COLUMN_HABIT_WEEKS = "numWeeks";
    }
}
