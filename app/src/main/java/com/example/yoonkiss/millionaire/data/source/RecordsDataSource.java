package com.example.yoonkiss.millionaire.data.source;

import android.support.annotation.NonNull;

import com.example.yoonkiss.millionaire.data.Record;

import java.util.List;

/**
 * Main entry point for accessing records data.
 * <p>
 * For simplicity, only getRecords() and getRecord() have callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 * For example, when a new record is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 */
public interface RecordsDataSource {
    interface LoadRecordsCallback {

        void onRecordsLoaded(List<Record> records);

        void onDataNotAvailable();
    }

    interface GetRecordCallback {

        void onRecordLoaded(Record task);

        void onDataNotAvailable();
    }

    void getRecords(@NonNull LoadRecordsCallback callback);

    void getRecord(@NonNull Long recordId, @NonNull GetRecordCallback callback);

    void saveRecord(@NonNull Record record);

    void deleteAllRecords();

    void deleteRecord(@NonNull Long recordId);
}
