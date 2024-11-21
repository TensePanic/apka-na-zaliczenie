package com.myapp.myapplication.data_access_layer.model

import androidx.room.Entity
import androidx.room.ForeignKey
import com.myapp.myapplication.data_access_layer.model.Exercise
import com.myapp.myapplication.data_access_layer.model.Training


@Entity(
    tableName = "training_exercise_join",
    primaryKeys = ["trainingId", "exerciseId"],
    foreignKeys = [
        ForeignKey(
            entity = Training::class,
            parentColumns = ["id"],
            childColumns = ["trainingId"]),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"])]
)
class TrainingExerciseJoin(
    var trainingId: Int,
    var exerciseId: Int
)