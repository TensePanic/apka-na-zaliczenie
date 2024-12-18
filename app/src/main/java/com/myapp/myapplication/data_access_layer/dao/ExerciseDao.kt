package com.myapp.myapplication.data_access_layer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myapp.myapplication.data_access_layer.model.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercise_table ORDER BY exercise_name ASC")
    fun getExercise(): Flow<List<Exercise>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(exercise: Exercise)

    @Query("DELETE FROM exercise_table")
    suspend fun deleteAll()

    @Query("SELECT et.id, et.exercise_name, et.exercise_desc, et.exercise_type FROM training_exercise_join as tej" +
            " JOIN exercise_table AS et ON tej.exerciseId = et.id" +
            " WHERE tej.trainingId = :trainingId")
    fun getExercisesForTraining(trainingId :Int): Flow<List<Exercise>>
}