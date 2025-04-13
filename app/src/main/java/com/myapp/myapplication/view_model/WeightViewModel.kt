package com.myapp.myapplication.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.myapp.myapplication.data_access_layer.AppRepository
import com.myapp.myapplication.data_access_layer.model.Training
import com.myapp.myapplication.data_access_layer.model.WeightEntry
import kotlinx.coroutines.launch

class WeightViewModel(private val repository: AppRepository) : ViewModel() {

    val allWeightEntries: LiveData<List<WeightEntry>> = repository.allWeightEntries.asLiveData()
    fun allWeightEntriesAsc(type : Int): LiveData<List<WeightEntry>>{
        when(type){
            1 -> return repository.lastWeekWeightEntriesAsc.asLiveData()
            2 -> return   repository.last30WeightEntriesAsc.asLiveData()
            3 -> return   repository.last90WeightEntriesAsc.asLiveData()
            else -> return   repository.allWeightEntriesAsc.asLiveData()
        }
    }


    fun insert(weightEntry: WeightEntry) = viewModelScope.launch {
        repository.insertWeightEntry(weightEntry)
    }
}

class WeightViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeightViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeightViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}