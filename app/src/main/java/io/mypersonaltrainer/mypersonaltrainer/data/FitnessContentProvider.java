package io.mypersonaltrainer.mypersonaltrainer.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by aditya on 7/15/17.
 */

public class FitnessContentProvider extends ContentProvider {

    public static final int USERS = 100;
    public static final int PLANNER = 500;
    public static final int WORKOUT = 900;
    PersonalFitnessDbHelper database;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //add matches
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_USERS, USERS);
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_USERS +"/#", USERS);
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_PLANNER, PLANNER);
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_PLANNER +"/#", PLANNER);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        database = new PersonalFitnessDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = database.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch(match){
            case USERS:
                retCursor = db.query(DBContract.UsersEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case PLANNER:
                retCursor = db.query(DBContract.PlannerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case WORKOUT:
                retCursor = db.query(DBContract.WorkoutEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) { return null; }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
