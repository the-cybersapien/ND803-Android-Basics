package xyz.cybersapien.habittracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import xyz.cybersapien.habittracker.data.HabitContract.HabitEntry;

/**
 * Created by ogcybersapien on 13/10/16.
 *
 * Database helper for the Habits Db.
 * Manages database creation, version management(not needed as of now though)
 * and adding/removing a row from the table
 */

public class HabitDbHelper extends SQLiteOpenHelper {

    /*Log tag for the Activity to be used for error detection/removal */
    public static final String LOG_TAG = HabitDbHelper.class.getName();

    /* Name of the Database file*/
    private static final String DATABASE_NAME = "habits.db";

    /* Database version. to be used in case we have to change the database schema.*/
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of HabitDbHelper Object
     * @param context of the app
     */
    public HabitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     * Creates a table for the habits
     * @param db the database we are dealing with.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create a String that contains the SQL statement to create the Habits table
        StringBuilder habitStringBuilder;
        habitStringBuilder = new StringBuilder("CREATE TABLE ");
        habitStringBuilder.append(HabitEntry.TABLE_NAME + " (")
                .append(HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(HabitEntry.COLUMN_HABIT_NAME + " TEXT NOT NULL, ")
                .append(HabitEntry.COLUMN_HABIT_MONDAY + " INTEGER NOT NULL DEFAULT 0, ")
                .append(HabitEntry.COLUMN_HABIT_TUESDAY + " INTEGER NOT NULL DEFAULT 0, ")
                .append(HabitEntry.COLUMN_HABIT_WEDNESDAY + " INTEGER NOT NULL DEFAULT 0, ")
                .append(HabitEntry.COLUMN_HABIT_THURSDAY + " INTEGER NOT NULL DEFAULT 0, ")
                .append(HabitEntry.COLUMN_HABIT_FRIDAY + " INTEGER NOT NULL DEFAULT 0, ")
                .append(HabitEntry.COLUMN_HABIT_SATURDAY + " INTEGER NOT NULL DEFAULT 0, ")
                .append(HabitEntry.COLUMN_HABIT_SUNDAY + " INTEGER NOT NULL DEFAULT 0, ")
                .append(HabitEntry.COLUMN_HABIT_WEEKS + " INTEGER NOT NULL DEFAULT 0);");

        db.execSQL(habitStringBuilder.toString());

    }

    /**
     * Needed only when there is an update to the database, such as addition/removal of a column or a table
     * Not needed for now.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Database still in v1, so nothing to be done here.
    }

    /**
     * Method to add a new habit to the table
     * When called in an activity, Enter an array of 7 booleans, each representing
     * one day of a week, starting from monday.
     * The method will prematurely return -1 if the number of booleans in days array is not 7.
     * else it returns the number of rows in the Table after inserting the new row.
     */
    public long insertHabit(SQLiteDatabase db, String habitName,
                            boolean days[], int weeks){
        if (days.length!=7){
            return -1;
        }
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT_NAME, habitName);
        values.put(HabitEntry.COLUMN_HABIT_MONDAY, days[0]?"1":"0");
        values.put(HabitEntry.COLUMN_HABIT_TUESDAY, days[1]?"1":"0");
        values.put(HabitEntry.COLUMN_HABIT_WEDNESDAY, days[2]?"1":"0");
        values.put(HabitEntry.COLUMN_HABIT_THURSDAY, days[3]?"1":"0");
        values.put(HabitEntry.COLUMN_HABIT_FRIDAY, days[4]?"1":"0");
        values.put(HabitEntry.COLUMN_HABIT_SATURDAY, days[5]?"1":"0");
        values.put(HabitEntry.COLUMN_HABIT_SUNDAY, days[6]?"1":"0");
        values.put(HabitEntry.COLUMN_HABIT_WEEKS, weeks);

        return db.insert(HabitEntry.TABLE_NAME, null, values);

    }

    /**
     * Returns a cursor with names of all the Habits
     */
    public Cursor showHabitsList(){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                HabitEntry._ID,
                HabitEntry.COLUMN_HABIT_NAME };

        return db.query(HabitEntry.TABLE_NAME,
                projection,
                null, null, null, null, null);
    }

    /**
     * Returns a cursor with all the information for the habits
     * To be used to make a list for the database.
     */
    public Cursor getHabitsList(){

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                HabitEntry._ID,
                HabitEntry.COLUMN_HABIT_NAME,
                HabitEntry.COLUMN_HABIT_MONDAY,
                HabitEntry.COLUMN_HABIT_TUESDAY,
                HabitEntry.COLUMN_HABIT_WEDNESDAY,
                HabitEntry.COLUMN_HABIT_THURSDAY,
                HabitEntry.COLUMN_HABIT_FRIDAY,
                HabitEntry.COLUMN_HABIT_SATURDAY,
                HabitEntry.COLUMN_HABIT_SUNDAY,
                HabitEntry.COLUMN_HABIT_WEEKS
        };

        return db.query(HabitEntry.TABLE_NAME,
                projection, null, null, null, null, HabitEntry.COLUMN_HABIT_WEEKS + " ASC");
    }

}
