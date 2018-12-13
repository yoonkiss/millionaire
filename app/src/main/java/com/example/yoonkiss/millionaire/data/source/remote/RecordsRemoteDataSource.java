package com.example.yoonkiss.millionaire.data.source.remote;

import android.support.annotation.NonNull;

import com.example.yoonkiss.millionaire.data.Record;
import com.example.yoonkiss.millionaire.data.source.RecordsDataSource;
import com.example.yoonkiss.millionaire.data.source.local.RecordsDao;
import com.example.yoonkiss.millionaire.util.AppExecutors;
import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import android.os.Handler;

import static com.google.common.base.Preconditions.checkNotNull;

public class RecordsRemoteDataSource implements RecordsDataSource {
    private static volatile RecordsRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private final static Map<Long, Record> RECORDS_SERVICE_DATA;

    static {
        RECORDS_SERVICE_DATA = new LinkedHashMap<>(2);
    }

    public static RecordsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RecordsRemoteDataSource();
        }
        return INSTANCE;
    }

    private RecordsRemoteDataSource() {}

    @Override
    public void getRecords(@NonNull final LoadRecordsCallback callback) {
        // Simulate network by delaying the execution.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onRecordsLoaded(Lists.newArrayList(RECORDS_SERVICE_DATA.values()));
            }
        }, SERVICE_LATENCY_IN_MILLIS);

    }

    @Override
    public void getRecord(@NonNull Long recordId, @NonNull GetRecordCallback callback) {

    }

    @Override
    public void saveRecord(@NonNull final Record record) {
        RECORDS_SERVICE_DATA.put(record.getId(), record);
    }

    @Override
    public void deleteAllRecords() {

    }

    @Override
    public void deleteRecord(@NonNull Long recordId) {

    }
}
