package com.myapp.myapplication.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.myapp.myapplication.data_access_layer.AppRepository
import com.myapp.myapplication.data_access_layer.model.Exercise
import kotlinx.coroutines.launch

class ExerciseViewModel(private val repository: AppRepository) : ViewModel() {

    // Using LiveData and caching what allTrainings returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allExercises: LiveData<List<Exercise>> = repository.allExercises.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(exercise: Exercise) = viewModelScope.launch {
        repository.insertExercise(exercise)
    }

    fun delete(id: Int) = viewModelScope.launch {
        repository.softDeleteExercise(id)
    }

    fun update(exercise: Exercise) = viewModelScope.launch {
        repository.updateExercise(exercise)
    }
}

class ExerciseViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExerciseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
