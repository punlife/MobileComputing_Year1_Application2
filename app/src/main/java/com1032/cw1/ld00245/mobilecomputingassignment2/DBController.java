package com1032.cw1.ld00245.mobilecomputingassignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Punlife on 21/05/2016.
 */
public class DBController extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static String SUMMONER_TABLE_NAME = null;
    private static String CHAMP_TABLE_NAME = null;
    private static String MATCHHISTORY_TABLE_NAME = null;

    public DBController(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory, DB_VERSION);
        SUMMONER_TABLE_NAME = name;
        CHAMP_TABLE_NAME = name + "champions";
        MATCHHISTORY_TABLE_NAME = name + "matchhistory";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("[SummonerDB::onCreate]", "Creating countries table");
        createTable(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /** First of all check to see if the countries table already exist..if this is the case then delete it */
        String dropSQL = "DROP TABLE IF EXISTS " + SUMMONER_TABLE_NAME + ";";
        db.execSQL(dropSQL);

        /** Then create the table again so that the new DB version will be adopted... */
        createTable(db);
        Log.d("[SummonerDB::onUpgrade]", "Upgrading DB");
    }

    /**
     * Method for deleting all the contents of the countries table.
     * There are many ways of doing that: delete all the rows of the table, delete the table and then re-create it so that it is empty, etc. In this case, we are going with the second solution
     * as it is more efficient
     */
    public void clearData() {
        /** Get a reference to the DB so that we can update it... */
        SQLiteDatabase db = this.getWritableDatabase();

        /** First of all check to see if the countries table already exist..if this is the case then delete it */
        String dropSQL = "DROP TABLE IF EXISTS " + SUMMONER_TABLE_NAME + ";";
        db.execSQL(dropSQL);
        String dropSQL1 = "DROP TABLE IF EXISTS " + CHAMP_TABLE_NAME + ";";
        db.execSQL(dropSQL1);
        String dropSQL2 = "DROP TABLE IF EXISTS " + MATCHHISTORY_TABLE_NAME + ";";
        db.execSQL(dropSQL2);

        /** Then create the table again so that the new DB version will be adopted... */
        createTable(db);
        createChampionTable(db);
        createMatchHistoryTable(db);
    }

    public void createAllTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        createTable(db);
        createChampionTable(db);
        createMatchHistoryTable(db);
    }

    /**
     * Method for inserting data to the countries DB through a ContentValues object...
     *
     * @param cv Set of values to be inserted in the DB
     */
    public void insertData(ContentValues cv) {
        /** Returns the DB associated with this helper... */
        SQLiteDatabase dbSummoner = this.getWritableDatabase();

        /** Create the row to be added in the countries table... */
        dbSummoner.insert(SUMMONER_TABLE_NAME, null, cv);
    }

    public void insertDataChampion(ContentValues cv) {
        /** Returns the DB associated with this helper... */
        SQLiteDatabase dbSummoner = this.getWritableDatabase();

        /** Create the row to be added in the countries table... */
        dbSummoner.insert(CHAMP_TABLE_NAME + "champion", null, cv);
    }

    public void insertDataMatchhistory(ContentValues cv) {
        /** Returns the DB associated with this helper... */
        SQLiteDatabase dbSummoner = this.getWritableDatabase();

        /** Create the row to be added in the countries table... */
        dbSummoner.insert(MATCHHISTORY_TABLE_NAME + "matchhistory", null, cv);
    }

    /**
     * Method for creating the countries tahle within our DB.
     *
     * @param db Reference to the SQLiteDatabase object for being able to execute SQL commands
     */
    private void createTable(SQLiteDatabase db) {
        String createSQL = "CREATE TABLE " + SUMMONER_TABLE_NAME + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "summonerID INT, " +
                "profileIcon TEXT, " +
                "name TEXT, " +
                "region TEXT, " +
                "rank TEXT," +
                "tier TEXT," +
                "level INT," +
                "masterylevel INT, " +
                "wins INT, " +
                "loss INT, " +
                "gamesplayed INT);";

        /** Execute the SQL creation clause...Since we don't expect any results back, we call the execSQL and not the rawQuery */
        db.execSQL(createSQL);
    }

    private void createChampionTable(SQLiteDatabase db) {
        String createSQL = "CREATE TABLE " + CHAMP_TABLE_NAME + "champion" + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "kda TEXT, " +
                "winrate TEXT, " +
                "gamesplayed TEXT, " +
                "averagecs TEXT," +
                "totalgamesplayed INT);";

        /** Execute the SQL creation clause...Since we don't expect any results back, we call the execSQL and not the rawQuery */
        db.execSQL(createSQL);
    }

    private void createMatchHistoryTable(SQLiteDatabase db) {
        String createSQL = "CREATE TABLE " + MATCHHISTORY_TABLE_NAME + "matchhistory" + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "CHAMPION TEXT, " +
                "queue TEXT, " +
                "gamemode TEXT, " +
                "outcome TEXT, " +
                "kda TEXT," +
                "date INT," +
                "level INT," +
                "cs INT, " +
                "length INT);";

        /** Execute the SQL creation clause...Since we don't expect any results back, we call the execSQL and not the rawQuery */
        db.execSQL(createSQL);
    }

    public void dropTable() {
        /** Get a reference to the DB so that we can update it... */
        SQLiteDatabase db = this.getWritableDatabase();

        /** First of all check to see if the countries table already exist..if this is the case then delete it */
        String dropSQL = "DROP TABLE IF EXISTS " + SUMMONER_TABLE_NAME + ";";
        db.execSQL(dropSQL);
    }
}
