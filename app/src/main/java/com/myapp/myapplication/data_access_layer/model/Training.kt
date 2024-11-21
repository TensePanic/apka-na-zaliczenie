package com.myapp.myapplication.data_access_layer.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "training_table")
class Training(@PrimaryKey(autoGenerate = true) val id: Int,
               @ColumnInfo(name = "training_name") val trainingName: String) {
}