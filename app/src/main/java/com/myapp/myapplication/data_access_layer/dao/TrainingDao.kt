package com.myapp.myapplication.data_access_layer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myapp.myapplication.data_access_layer.model.Training
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingDao {

    @Query("SELECT * FROM training_table ORDER BY training_name ASC")
    fun getTrainings(): Flow<List<Training>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(training: Training)

    @Query("DELETE FROM training_table")
    suspend fun deleteAll()
}
