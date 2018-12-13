package com.example.yoonkiss.millionaire.data.source;

import android.support.annotation.NonNull;

import com.example.yoonkiss.millionaire.data.Record;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class RecordsRepository implements RecordsDataSource {

    private static RecordsRepository INSTANCE = null;

    private final RecordsDataSource mRecordsLocalDataSource;
    private final RecordsDataSource mRecordsRemoteDataSource;
    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<Long, Record> mCachedRecords;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private RecordsRepository(@NonNull RecordsDataSource recordsRemoteDataSource,
                              @NonNull RecordsDataSource recordsLocalDataSource) {
        mRecordsRemoteDataSource = checkNotNull(recordsRemoteDataSource);
        mRecordsLocalDataSource = checkNotNull(recordsLocalDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param recordsRemoteDataSource the backend data source
     * @param recordsLocalDataSource  the device storage data source
     * @return the {@link RecordsRepository} instance
     */
    public static RecordsRepository getInstance(RecordsDataSource recordsRemoteDataSource,
                                                RecordsDataSource recordsLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RecordsRepository(recordsRemoteDataSource, recordsLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(RecordsDataSource, RecordsDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Gets records from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     * <p>
     * Note: {@link LoadRecordsCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */
    @Override
    public void getRecords(@NonNull final LoadRecordsCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedRecords != null && !mCacheIsDirty) {
            callback.onRecordsLoaded(new ArrayList<>(mCachedRecords.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getRecordsFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mRecordsLocalDataSource.getRecords(new LoadRecordsCallback() {
                @Override
                public void onRecordsLoaded(List<Record> records) {
                    refreshCache(records);
                    callback.onRecordsLoaded(new ArrayList<>(mCachedRecords.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getRecordsFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void saveRecord(@NonNull Record record) {
        checkNotNull(record);
        mRecordsRemoteDataSource.saveRecord(record);
        mRecordsLocalDataSource.saveRecord(record);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedRecords == null) {
            mCachedRecords = new LinkedHashMap<>();
        }
        mCachedRecords.put(record.getId(), record);
    }

    @Override
    public void deleteAllRecords() {

    }


    @Override
    public void getRecord(@NonNull Long recordId, @NonNull GetRecordCallback callback) {

    }

    @Override
    public void deleteRecord(@NonNull Long recordId) {

    }

    private void getRecordsFromRemoteDataSource(@NonNull final LoadRecordsCallback callback) {
        mRecordsRemoteDataSource.getRecords(new LoadRecordsCallback() {
            @Override
            public void onRecordsLoaded(List<Record> records) {
                refreshCache(records);
                refreshLocalDataSource(records);
                callback.onRecordsLoaded(new ArrayList<>(mCachedRecords.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }


    private void refreshCache(List<Record> records) {
        if (mCachedRecords == null) {
            mCachedRecords = new LinkedHashMap<>();
        }
        mCachedRecords.clear();
        for (Record record : records) {
            mCachedRecords.put(record.getId(), record);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Record> records) {
        mRecordsLocalDataSource.deleteAllRecords();
        for (Record record : records) {
            mRecordsLocalDataSource.saveRecord(record);
        }
    }
}
