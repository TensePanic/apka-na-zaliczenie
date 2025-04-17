package com.myapp.myapplication.data_access_layer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.myapp.myapplication.data_access_layer.model.Exercise
import com.myapp.myapplication.data_access_layer.model.TrainingExerciseSet
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercise_table WHERE exercise_is_deleted = 0 ORDER BY exercise_name ASC")
    fun getExercise(): Flow<List<Exercise>>

    @Query("SELECT * FROM exercise_table where id = :id")
    suspend fun getExerciseById(id: Int): Exercise

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(exercise: Exercise)

    @Query("UPDATE exercise_table SET exercise_is_deleted = 1 WHERE id = :id")
    suspend fun softDelete(id: Int)

    @Query("DELETE FROM exercise_table")
    suspend fun deleteAll()

    @Update
    suspend fun update(exercise: Exercise)

    @Query("SELECT * FROM training_exercise_set as tej" +
            " JOIN exercise_table AS et ON tej.exerciseId = et.id" +
            " WHERE tej.trainingId = :trainingId")
    fun getExercisesForTraining(trainingId :Int): Flow<List<Exercise>>
}