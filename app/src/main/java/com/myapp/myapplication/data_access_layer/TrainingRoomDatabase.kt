package com.myapp.myapplication.data_access_layer

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.myapp.myapplication.data_access_layer.model.Exercise
import com.myapp.myapplication.data_access_layer.model.Training
import com.myapp.myapplication.data_access_layer.model.TrainingExerciseJoin
import com.myapp.myapplication.data_access_layer.dao.ExerciseDao
import com.myapp.myapplication.data_access_layer.dao.TrainingDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Training class
@Database(entities = arrayOf(Training::class, Exercise::class, TrainingExerciseJoin::class), version = 4, exportSchema = false)
abstract class TrainingRoomDatabase : RoomDatabase() {

    abstract fun trainingDao(): TrainingDao

    abstract fun exerciseDao(): ExerciseDao

    private class TrainingDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
 // ZOSTAWIAM JAKO TEMPLATE
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: TrainingRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): TrainingRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrainingRoomDatabase::class.java,
                    "word_database"
                )
                    .addCallback(TrainingDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

