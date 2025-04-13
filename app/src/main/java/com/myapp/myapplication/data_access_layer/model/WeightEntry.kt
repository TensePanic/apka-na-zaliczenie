package com.myapp.myapplication.data_access_layer.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weight_entry_table")
class WeightEntry(@PrimaryKey(autoGenerate = true) val id: Int,
                  @ColumnInfo(name = "weight_value") val weightValue: Float,
                  @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis())
