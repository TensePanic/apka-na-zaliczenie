package com.myapp.myapplication.data_access_layer.model

import androidx.room.Entity
import androidx.room.ForeignKey


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