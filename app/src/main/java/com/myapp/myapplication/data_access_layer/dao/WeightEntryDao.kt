package com.myapp.myapplication.data_access_layer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myapp.myapplication.data_access_layer.model.WeightEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightEntryDao {

    @Query("SELECT * FROM weight_entry_table ORDER BY timestamp DESC")
    fun getWeightEntries(): Flow<List<WeightEntry>>

    @Query("SELECT * FROM weight_entry_table WHERE timestamp >= :from ORDER BY timestamp ASC")
    fun getEntriesFrom(from: Long = 0): Flow<List<WeightEntry>>


//    @Query("SELECT * FROM exercise_table where id = :id")
//    suspend fun getExerciseById(id: Int): Exercise

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(weightEntry: WeightEntry)

    @Query("DELETE FROM weight_entry_table")
    suspend fun deleteAll()
}