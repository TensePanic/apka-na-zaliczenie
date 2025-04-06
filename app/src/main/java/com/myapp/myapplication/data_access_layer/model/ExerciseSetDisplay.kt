package com.myapp.myapplication.data_access_layer.model

data class ExerciseSetDisplay(
    val exerciseName: String,
    val exerciseType: String,
    val reps: Int?,
    val weight: Int?,
    val time: Int?,
    val distance: Int?
)
