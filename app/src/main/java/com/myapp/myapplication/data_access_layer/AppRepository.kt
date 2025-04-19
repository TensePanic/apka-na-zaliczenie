package com.myapp.myapplication.data_access_layer

import androidx.annotation.WorkerThread
import com.myapp.myapplication.data_access_layer.model.Exercise
import com.myapp.myapplication.data_access_layer.dao.ExerciseDao
import com.myapp.myapplication.data_access_layer.model.Training
import com.myapp.myapplication.data_access_layer.dao.TrainingDao
import com.myapp.myapplication.data_access_layer.dao.TrainingExerciseSetDao
import com.myapp.myapplication.data_access_layer.dao.WeightEntryDao
import com.myapp.myapplication.data_access_layer.model.ExerciseSetDisplay
import com.myapp.myapplication.data_access_layer.model.TrainingExerciseSet
import com.myapp.myapplication.data_access_layer.model.WeightEntry
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class AppRepository(private val trainingDao: TrainingDao,
                    private val exerciseDao: ExerciseDao,
                    private val trainingExerciseSetDao: TrainingExerciseSetDao,
                    private val weightEntryDao: WeightEntryDao
) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allTrainings: Flow<List<Training>> = trainingDao.getTrainings()
    val allExercises: Flow<List<Exercise>> = exerciseDao.getExercise()
    val allWeightEntries: Flow<List<WeightEntry>> = weightEntryDao.getWeightEntries()
    val allWeightEntriesAsc: Flow<List<WeightEntry>> = weightEntryDao.getEntriesFrom()
    val lastWeekWeightEntriesAsc: Flow<List<WeightEntry>> = weightEntryDao.getEntriesFrom(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000)
    val last30WeightEntriesAsc: Flow<List<WeightEntry>> = weightEntryDao.getEntriesFrom(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000)
    val last90WeightEntriesAsc: Flow<List<WeightEntry>> = weightEntryDao.getEntriesFrom(System.currentTimeMillis() - 183L * 24 * 60 * 60 * 1000)

    fun getExercisesForTraining(trainingId: Int): Flow<List<ExerciseSetDisplay>> {
        return trainingExerciseSetDao.getExerciseSetsForTraining(trainingId)
    }

    suspend fun getExerciseById(id: Int): Exercise {
        return exerciseDao.getExerciseById(id)
    }

    suspend fun getExerciseSetById(id: Int): TrainingExerciseSet {
        return trainingExerciseSetDao.getExerciseSetById(id)
    }

    suspend fun updateExercise(exercise: Exercise) {
        exerciseDao.update(exercise)
    }

    suspend fun softDeleteExercise(id: Int) {
        exerciseDao.softDelete(id)
    }

    suspend fun softDeleteTraining(id: Int){
        trainingDao.softDelete(id)
    }

    suspend fun updateSet(trainingExerciseSet: TrainingExerciseSet) {
        trainingExerciseSetDao.update(trainingExerciseSet)
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

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertWeightEntry(weightEntry: WeightEntry) {
        weightEntryDao.insert(weightEntry)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteSetById(id: Int) {
        trainingExerciseSetDao.deleteById(id)
    }
}
