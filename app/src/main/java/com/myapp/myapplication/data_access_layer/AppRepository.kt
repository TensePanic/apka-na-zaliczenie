package com.myapp.myapplication.data_access_layer

import androidx.annotation.WorkerThread
import com.myapp.myapplication.data_access_layer.model.Exercise
import com.myapp.myapplication.data_access_layer.dao.ExerciseDao
import com.myapp.myapplication.data_access_layer.model.Training
import com.myapp.myapplication.data_access_layer.dao.TrainingDao
import com.myapp.myapplication.data_access_layer.dao.TrainingExerciseSetDao
import com.myapp.myapplication.data_access_layer.model.ExerciseSetDisplay
import com.myapp.myapplication.data_access_layer.model.TrainingExerciseSet
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class AppRepository(private val trainingDao: TrainingDao,
                    private val exerciseDao: ExerciseDao,
                    private val trainingExerciseSetDao: TrainingExerciseSetDao
) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allTrainings: Flow<List<Training>> = trainingDao.getTrainings()
    val allExercises: Flow<List<Exercise>> = exerciseDao.getExercise()

    fun getExercisesForTraining(trainingId: Int): Flow<List<ExerciseSetDisplay>> {
        return trainingExerciseSetDao.getExerciseSetsForTraining(trainingId)
    }

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertTraining(training: Training) {
        trainingDao.insert(training)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertExercise(exercise: Exercise) {
        exerciseDao.insert(exercise)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertSet(trainingExerciseSet: TrainingExerciseSet) {
        trainingExerciseSetDao.insert(trainingExerciseSet)
    }
}
