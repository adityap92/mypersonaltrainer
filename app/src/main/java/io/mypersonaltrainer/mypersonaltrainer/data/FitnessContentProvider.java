package io.mypersonaltrainer.mypersonaltrainer.data;

import android.content.ContentProvider;
import android.content.ContentUris;
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
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_WORKOUT, WORKOUT);
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_WORKOUT + "/#", WORKOUT);

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
        final SQLiteDatabase db = database.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch(match){
            case USERS:
                long id = db.insert(DBContract.UsersEntry.TABLE_NAME, null, contentValues);

                if(id > 0)
                    returnUri = ContentUris.withAppendedId(DBContract.UsersEntry.CONTENT_URI, id);
                else
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                break;
            case PLANNER:
                long id1 = db.insert(DBContract.PlannerEntry.TABLE_NAME, null, contentValues);
                if(id1 > 0)
                    returnUri = ContentUris.withAppendedId(DBContract.PlannerEntry.CONTENT_URI, id1);
                else
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                break;
            case WORKOUT:
                long id2 = db.insert(DBContract.WorkoutEntry.TABLE_NAME, null, contentValues);
                if(id2 > 0)
                    returnUri = ContentUris.withAppendedId(DBContract.WorkoutEntry.CONTENT_URI, id2);
                else
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                break;
            default:
                throw new android.database.SQLException("Unknown URI: " + uri);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = database.getWritableDatabase();
        int rowsDeleted = 0;

        int match = sUriMatcher.match(uri);
        switch(match){
            case WORKOUT:
                rowsDeleted = db.delete(DBContract.WorkoutEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = database.getWritableDatabase();
        int count = -1;

        int match = sUriMatcher.match(uri);
        switch(match){
            case WORKOUT:
                count = db.update(DBContract.WorkoutEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return count;
    }
}
