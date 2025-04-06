package com.myapp.myapplication.data_access_layer.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myapp.myapplication.data_access_layer.model.Exercise
import com.myapp.myapplication.data_access_layer.model.ExerciseSetDisplay
import com.myapp.myapplication.data_access_layer.model.TrainingExerciseSet
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingExerciseSetDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trainingExerciseSet: TrainingExerciseSet)

    @Query("SELECT * FROM exercise_table INNER JOIN training_exercise_set ON exercise_table.id = training_exercise_set.exerciseId WHERE training_exercise_set.trainingId = :trainingId")
    fun getExercisesForTraining(trainingId: Int): Flow<List<Exercise>> // Changed to Flow

    @Query("""
    SELECT exercise_table.exercise_name AS exerciseName,
           exercise_table.exercise_type AS exerciseType,
           training_exercise_set.reps AS reps,
           training_exercise_set.weight AS weight,
           training_exercise_set.time AS time,
           training_exercise_set.distance AS distance
    FROM training_exercise_set
    INNER JOIN exercise_table ON training_exercise_set.exerciseId = exercise_table.id
    WHERE training_exercise_set.trainingId = :trainingId
""")
    fun getExerciseSetsForTraining(trainingId: Int): Flow<List<ExerciseSetDisplay>>


    @Delete
    suspend fun delete(trainingExerciseSet: TrainingExerciseSet)
}