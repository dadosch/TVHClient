package org.tvheadend.tvhclient.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.tvheadend.tvhclient.DatabaseHelper;

public class DataContentProvider extends ContentProvider {
    private final static String TAG = DataContentProvider.class.getSimpleName();

    // helper constants for use with the UriMatcher
    private static final int CONNECTION_LIST = 1;
    private static final int CONNECTION_ID = 2;
    private static final int PROFILE_LIST = 3;
    private static final int PROFILE_ID = 4;
    private static final int CHANNEL_LIST = 5;
    private static final int CHANNEL_ID = 6;
    private static final int TAG_LIST = 7;
    private static final int TAG_ID = 8;
    private static final int PROGRAM_LIST = 9;
    private static final int PROGRAM_ID = 10;
    private static final UriMatcher mUriMatcher;

    // prepare the UriMatcher
    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(DataContract.AUTHORITY, "connections", CONNECTION_LIST);
        mUriMatcher.addURI(DataContract.AUTHORITY, "connections/#", CONNECTION_ID);
        mUriMatcher.addURI(DataContract.AUTHORITY, "profiles", PROFILE_LIST);
        mUriMatcher.addURI(DataContract.AUTHORITY, "profiles/#", PROFILE_ID);
        mUriMatcher.addURI(DataContract.AUTHORITY, "channels", CHANNEL_LIST);
        mUriMatcher.addURI(DataContract.AUTHORITY, "channels/#", CHANNEL_ID);
        mUriMatcher.addURI(DataContract.AUTHORITY, "tags", TAG_LIST);
        mUriMatcher.addURI(DataContract.AUTHORITY, "tags/#", TAG_ID);
        mUriMatcher.addURI(DataContract.AUTHORITY, "programs", PROGRAM_LIST);
        mUriMatcher.addURI(DataContract.AUTHORITY, "programs/#", PROGRAM_ID);
    }

    private DatabaseHelper mHelper;

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate() called");
        mHelper = DatabaseHelper.getInstance(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query() called with: uri = [" + uri + "]");

        SQLiteDatabase db = mHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        switch (mUriMatcher.match(uri)) {
            case CONNECTION_LIST:
                builder.setTables(DataContract.Connections.TABLE);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = DataContract.Connections.SORT_ORDER_DEFAULT;
                }
                break;

            case CONNECTION_ID:
                builder.setTables(DataContract.Connections.TABLE);
                // limit query to one row at most
                builder.appendWhere(DataContract.Connections.ID + " = " + uri.getLastPathSegment());
                break;

            case PROFILE_LIST:
                builder.setTables(DataContract.Profiles.TABLE);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = DataContract.Profiles.SORT_ORDER_DEFAULT;
                }
                break;

            case PROFILE_ID:
                builder.setTables(DataContract.Profiles.TABLE);
                // limit query to one row at most
                builder.appendWhere(DataContract.Profiles.ID + " = " + uri.getLastPathSegment());
                break;

            case CHANNEL_LIST:
                builder.setTables(DataContract.Channels.TABLE);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = DataContract.Channels.SORT_ORDER_DEFAULT;
                }
                break;

            case CHANNEL_ID:
                builder.setTables(DataContract.Channels.TABLE);
                // limit query to one row at most
                builder.appendWhere(DataContract.Channels.ID + " = " + uri.getLastPathSegment());
                break;

            case TAG_LIST:
                builder.setTables(DataContract.Tags.TABLE);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = DataContract.Tags.SORT_ORDER_DEFAULT;
                }
                break;

            case TAG_ID:
                builder.setTables(DataContract.Tags.TABLE);
                // limit query to one row at most
                builder.appendWhere(DataContract.Tags.ID + " = " + uri.getLastPathSegment());
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        // if you like you can log the query
        Log.d(TAG, "SQL query: " + builder.buildQuery(projection, selection, null, null, sortOrder, null));

        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        // if we want to be notified of any changes:
        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.d(TAG, "getType() called with: uri = [" + uri + "]");

        // TODO rename the content_<name>_type to content_id_type or something else
        switch (mUriMatcher.match(uri)) {
            case CONNECTION_ID:
                return DataContract.Connections.CONTENT_CONNECTION_TYPE;
            case CONNECTION_LIST:
                return DataContract.Connections.CONTENT_TYPE;
            case PROFILE_ID:
                return DataContract.Profiles.CONTENT_PROFILE_TYPE;
            case PROFILE_LIST:
                return DataContract.Profiles.CONTENT_TYPE;
            case CHANNEL_ID:
                return DataContract.Channels.CONTENT_CHANNEL_TYPE;
            case CHANNEL_LIST:
                return DataContract.Channels.CONTENT_TYPE;
            case TAG_ID:
                return DataContract.Tags.CONTENT_TAG_TYPE;
            case TAG_LIST:
                return DataContract.Tags.CONTENT_TYPE;
            case PROGRAM_ID:
                return DataContract.Programs.CONTENT_PROGRAM_TYPE;
            case PROGRAM_LIST:
                return DataContract.Programs.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        Log.d(TAG, "insert() called with: uri = [" + uri + "]");

        // Check if the uri is valid
        if (mUriMatcher.match(uri) != CONNECTION_LIST
                && mUriMatcher.match(uri) != PROFILE_LIST
                && mUriMatcher.match(uri) != CHANNEL_LIST
                && mUriMatcher.match(uri) != TAG_LIST
                && mUriMatcher.match(uri) != PROGRAM_LIST) {
            throw new IllegalArgumentException("Unsupported URI for insertion: " + uri);
        }

        SQLiteDatabase db = mHelper.getWritableDatabase();
        Uri newUri = null;
        long id;

        // Depending on the uri update the provides values
        switch (mUriMatcher.match(uri)) {
            case CONNECTION_LIST:
                id = db.insert(DataContract.Connections.TABLE, null, contentValues);
                newUri = getUriForId(id, uri);
                break;

            case PROFILE_LIST:
                id = db.insert(DataContract.Profiles.TABLE, null, contentValues);
                newUri = getUriForId(id, uri);
                break;

            case CHANNEL_LIST:
                id = db.insertWithOnConflict(DataContract.Channels.TABLE, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                newUri = getUriForId(id, uri);
                break;

            case TAG_LIST:
                id = db.insertWithOnConflict(DataContract.Tags.TABLE, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                newUri = getUriForId(id, uri);
                break;

            case PROGRAM_LIST:
                id = db.insertWithOnConflict(DataContract.Programs.TABLE, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                newUri = getUriForId(id, uri);
                break;
        }

        return newUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete() called with: uri = [" + uri + "]");

        SQLiteDatabase db = mHelper.getWritableDatabase();
        int deletedRows;
        String where;

        switch (mUriMatcher.match(uri)) {
            case CONNECTION_LIST:
                deletedRows = db.delete(DataContract.Connections.TABLE, selection, selectionArgs);
                break;

            case CONNECTION_ID:
                where = DataContract.Connections.ID + " = " + uri.getLastPathSegment();
                where += TextUtils.isEmpty(selection) ? "" : " AND " + selection;
                deletedRows = db.delete(DataContract.Connections.TABLE, where, selectionArgs);
                break;

            case PROFILE_LIST:
                deletedRows = db.delete(DataContract.Profiles.TABLE, selection, selectionArgs);
                break;

            case PROFILE_ID:
                where = DataContract.Profiles.ID + " = " + uri.getLastPathSegment();
                where += TextUtils.isEmpty(selection) ? "" : " AND " + selection;
                deletedRows = db.delete(DataContract.Profiles.TABLE, where, selectionArgs);
                break;

            case CHANNEL_LIST:
                deletedRows = db.delete(DataContract.Channels.TABLE, selection, selectionArgs);
                break;

            case CHANNEL_ID:
                where = DataContract.Channels.ID + " = " + uri.getLastPathSegment();
                where += TextUtils.isEmpty(selection) ? "" : " AND " + selection;
                deletedRows = db.delete(DataContract.Channels.TABLE, where, selectionArgs);
                break;

            case TAG_LIST:
                deletedRows = db.delete(DataContract.Tags.TABLE, selection, selectionArgs);
                break;

            case TAG_ID:
                where = DataContract.Tags.ID + " = " + uri.getLastPathSegment();
                where += TextUtils.isEmpty(selection) ? "" : " AND " + selection;
                deletedRows = db.delete(DataContract.Tags.TABLE, where, selectionArgs);
                break;

            case PROGRAM_LIST:
                deletedRows = db.delete(DataContract.Programs.TABLE, selection, selectionArgs);
                break;

            case PROGRAM_ID:
                where = DataContract.Programs.ID + " = " + uri.getLastPathSegment();
                where += TextUtils.isEmpty(selection) ? "" : " AND " + selection;
                deletedRows = db.delete(DataContract.Programs.TABLE, where, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        // notify all listeners of the changes
        if (getContext() != null && deletedRows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        Log.d(TAG, "update() called with: uri = [" + uri + "], contentValues = [" + contentValues + "], selection = [" + selection + "], selectionArgs = [" + selectionArgs[0] + "]");

        SQLiteDatabase db = mHelper.getWritableDatabase();
        int updateCount;
        String where;

        switch (mUriMatcher.match(uri)) {
            case CONNECTION_LIST:
                updateCount = db.update(DataContract.Connections.TABLE, contentValues, selection, selectionArgs);
                break;

            case CONNECTION_ID:
                where = DataContract.Connections.ID + " = " + uri.getLastPathSegment();
                where += TextUtils.isEmpty(selection) ? "" : " AND " + selection;
                updateCount = db.update(DataContract.Connections.TABLE, contentValues, where, selectionArgs);
                break;

            case PROFILE_LIST:
                updateCount = db.update(DataContract.Profiles.TABLE, contentValues, selection, selectionArgs);
                break;

            case PROFILE_ID:
                where = DataContract.Profiles.ID + " = " + uri.getLastPathSegment();
                where += TextUtils.isEmpty(selection) ? "" : " AND " + selection;
                updateCount = db.update(DataContract.Profiles.TABLE, contentValues, where, selectionArgs);
                break;

            case CHANNEL_LIST:
                updateCount = db.update(DataContract.Channels.TABLE, contentValues, selection, selectionArgs);
                break;

            case CHANNEL_ID:
                where = DataContract.Channels.ID + " = " + uri.getLastPathSegment();
                where += TextUtils.isEmpty(selection) ? "" : " AND " + selection;
                updateCount = db.update(DataContract.Channels.TABLE, contentValues, where, selectionArgs);
                break;

            case TAG_LIST:
                updateCount = db.update(DataContract.Tags.TABLE, contentValues, selection, selectionArgs);
                break;

            case TAG_ID:
                where = DataContract.Tags.ID + " = " + uri.getLastPathSegment();
                where += TextUtils.isEmpty(selection) ? "" : " AND " + selection;
                updateCount = db.update(DataContract.Tags.TABLE, contentValues, where, selectionArgs);
                break;

            case PROGRAM_LIST:
                updateCount = db.update(DataContract.Programs.TABLE, contentValues, selection, selectionArgs);
                break;

            case PROGRAM_ID:
                where = DataContract.Programs.ID + " = " + uri.getLastPathSegment();
                where += TextUtils.isEmpty(selection) ? "" : " AND " + selection;
                updateCount = db.update(DataContract.Programs.TABLE, contentValues, where, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        // notify all listeners of changes:
        if (getContext() != null && updateCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updateCount;
    }

    private Uri getUriForId(long id, Uri uri) {
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            if (getContext() != null) {
                // notify all listeners of changes and return itemUri:
                getContext().getContentResolver().notifyChange(itemUri, null);
            }
            return itemUri;
        }
        throw new SQLException("Problem while inserting into uri: " + uri);
    }
}