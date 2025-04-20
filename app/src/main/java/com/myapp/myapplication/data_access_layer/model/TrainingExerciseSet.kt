package com.myapp.myapplication.data_access_layer.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "training_exercise_set",
    foreignKeys = [
        ForeignKey(entity = Training::class, parentColumns = ["id"], childColumns = ["trainingId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Exercise::class, parentColumns = ["id"], childColumns = ["exerciseId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class TrainingExerciseSet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Unikalne ID dla każdego połączenia
    val trainingId: Int, // ID treningu
    val exerciseId: Int,  // ID ćwiczenia
    val setOrderNumber: Int,
    val reps: Int? = null,
    val weight: Int? = null,
    val time: Int? = null,
    val distance: Int? = null
)