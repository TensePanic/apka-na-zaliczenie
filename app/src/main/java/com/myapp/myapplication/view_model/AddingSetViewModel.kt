package com.myapp.myapplication.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.myapp.myapplication.data_access_layer.AppRepository
import com.myapp.myapplication.data_access_layer.model.Exercise
import com.myapp.myapplication.data_access_layer.model.TrainingExerciseSet
import kotlinx.coroutines.launch

class AddingSetViewModel(private val repository: AppRepository) : ViewModel() {

    // Using LiveData and caching what allTrainings returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allExercises: LiveData<List<Exercise>> = repository.allExercises.asLiveData()//TODO: poprawić nazwę

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(trainingExerciseSet: TrainingExerciseSet) = viewModelScope.launch {
        repository.insertSet(trainingExerciseSet)
    }

    suspend fun getExerciseById(id: Int): Exercise {
        return repository.getExerciseById(id)
    }

    suspend fun getExerciseSetById(id: Int): TrainingExerciseSet {
        return repository.getExerciseSetById(id)
    }

    fun updateSet(trainingExerciseSet: TrainingExerciseSet) = viewModelScope.launch {
        repository.updateSet(trainingExerciseSet)
    }

    fun buildSet(
        exerciseType: String?,
        exerciseID: Int,
        trainingId: Int,
        mainValue: Int?,
        weight: Int?
    ): TrainingExerciseSet? {
        when (exerciseType) {
            "Without weights" -> {
                return TrainingExerciseSet(
                    exerciseId = exerciseID,
                    trainingId = trainingId,
                    reps = mainValue
                )
            }

            "With weights" -> {
                return TrainingExerciseSet(
                    exerciseId = exerciseID,
                    trainingId = trainingId,
                    reps = mainValue,
                    weight = weight
                )
            }

            "Time" -> {
                return TrainingExerciseSet(
                    exerciseId = exerciseID,
                    trainingId = trainingId,
                    time = mainValue
                )
            }

            "Distance" -> {
                return TrainingExerciseSet(
                    exerciseId = exerciseID,
                    trainingId = trainingId,
                    distance = mainValue
                )
            }

            else -> return null
        }
    }

    fun updateSetValue(
        set: TrainingExerciseSet,
        exerciseType: String?,
        mainValue: Int?,
        weight: Int?
    ): TrainingExerciseSet? {
        when (exerciseType) {
            "Without weights" -> {
                return TrainingExerciseSet(
                    id = set.id,
                    exerciseId = set.exerciseId,
                    trainingId = set.trainingId,
                    reps = mainValue
                )
            }

            "With weights" -> {
                return TrainingExerciseSet(
                    id = set.id,
                    exerciseId = set.exerciseId,
                    trainingId = set.trainingId,
                    reps = mainValue,
                    weight = weight
                )
            }

            "Time" -> {
                return TrainingExerciseSet(
                    id = set.id,
                    exerciseId = set.exerciseId,
                    trainingId = set.trainingId,
                    time = mainValue
                )
            }

            "Distance" -> {
                return TrainingExerciseSet(
                    id = set.id,
                    exerciseId = set.exerciseId,
                    trainingId = set.trainingId,
                    distance = mainValue
                )
            }

            else -> return null
        }
    }
}

class AddingSetViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddingSetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddingSetViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}