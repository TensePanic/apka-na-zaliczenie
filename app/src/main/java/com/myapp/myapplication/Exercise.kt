package com.myapp.myapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_table")
class Exercise(@PrimaryKey(autoGenerate = true) val id: Int,
               @ColumnInfo(name = "exercise_name") val exerciseName: String,
               @ColumnInfo(name = "exercise_desc") val exerciseDesc: String) {
}