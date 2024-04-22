package com.myapp.myapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "training_table")
class Training(@PrimaryKey(autoGenerate = true) val id: Int,
               @ColumnInfo(name = "training_name") val trainingName: String) {
}