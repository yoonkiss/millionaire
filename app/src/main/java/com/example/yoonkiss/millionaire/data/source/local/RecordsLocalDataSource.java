package com.example.yoonkiss.millionaire.data.source.local;

import android.support.annotation.NonNull;

import com.example.yoonkiss.millionaire.data.Record;
import com.example.yoonkiss.millionaire.data.source.RecordsDataSource;
import com.example.yoonkiss.millionaire.util.AppExecutors;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class RecordsLocalDataSource implements RecordsDataSource {
    private static volatile RecordsLocalDataSource INSTANCE;

    private RecordsDao mRecordsDao;

    private AppExecutors mAppExecutors;

    private RecordsLocalDataSource(@NonNull AppExecutors appExecutors, @NonNull RecordsDao recordsDao) {
        mAppExecutors = appExecutors;
        mRecordsDao = recordsDao;
    }

    public static RecordsLocalDataSource getInstance(@NonNull AppExecutors appExecutors, @NonNull RecordsDao recordsDao) {
        if (INSTANCE == null) {
            synchronized (RecordsLocalDataSource.class) {
                if(INSTANCE == null) {
                    INSTANCE = new RecordsLocalDataSource(appExecutors, recordsDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getRecords(@NonNull final LoadRecordsCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Record> records = mRecordsDao.getRecords();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (records.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onRecordsLoaded(records);
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getRecord(@NonNull Long recordId, @NonNull GetRecordCallback callback) {

    }

    @Override
    public void saveRecord(@NonNull final Record record) {
        checkNotNull(record);
        Runnable saveRunnable = new Runnable() {

            @Override
            public void run() {
                mRecordsDao.insertRecord(record);
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void deleteAllRecords() {

    }

    @Override
    public void deleteRecord(@NonNull Long recordId) {

    }
}
