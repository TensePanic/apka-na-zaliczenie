package com.myapp.myapplication.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.myapp.myapplication.data_access_layer.AppRepository
import com.myapp.myapplication.data_access_layer.model.Exercise
import com.myapp.myapplication.data_access_layer.model.ExerciseSetDisplay
import com.myapp.myapplication.data_access_layer.model.TrainingExerciseSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TrainingDetailsViewModel(private val repository: AppRepository) : ViewModel() {

    fun getExercisesForTraining(trainingId: Int): Flow<List<ExerciseSetDisplay>> {
        return repository.getExercisesForTraining(trainingId)
    }

    fun deleteExerciseSetById(id: Int) = viewModelScope.launch{
        repository.deleteSetById(id)
    }

    fun updateSetPosition(id: Int, position: Int) = viewModelScope.launch{
        repository.updateSetPosition(id, position)
    }
}

class TrainingDetailsViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainingDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TrainingDetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}