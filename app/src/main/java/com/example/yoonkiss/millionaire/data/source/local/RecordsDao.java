package com.example.yoonkiss.millionaire.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.yoonkiss.millionaire.data.Record;

import java.util.List;

@Dao
public interface RecordsDao {
    @Query("SELECT * FROM records")
    List<Record> getRecords();

    @Query("SELECT * FROM records WHERE id = :id")
    Record getRecordById(Long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecord(Record record);

    @Update
    int updateRecord(Record record);

    @Query("DELETE FROM records WHERE id = :id")
    int deleteRecordBuId(Long id);

    @Query("DELETE FROM records")
    void deleteRecords();
}
