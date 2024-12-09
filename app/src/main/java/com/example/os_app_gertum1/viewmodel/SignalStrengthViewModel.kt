package com.example.os_app_gertum1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.os_app_gertum1.data.database.SignalStrength
import com.example.os_app_gertum1.data.repository.SignalStrengthRepository
import kotlinx.coroutines.launch

class SignalStrengthViewModel(private val repository: SignalStrengthRepository) : ViewModel() {

    // LiveData for observing all signals from the local database
    val allSignals: LiveData<List<SignalStrength>> = repository.getSignalStrengthsFromDb()

    // Refresh signal strengths from the API
    fun refreshSignalStrengths() {
        viewModelScope.launch {
            repository.refreshSignalStrengths()
        }
    }

    // Add a new signal strength
    fun addSignal(signal: SignalStrength) {
        viewModelScope.launch {
            repository.addSignal(signal) // Use repository's method
        }
    }

    // Update an existing signal strength
    fun updateSignal(signal: SignalStrength) {
        viewModelScope.launch {
            repository.updateSignal(signal) // Use repository's method
        }
    }

    // Delete a signal strength
    fun deleteSignal(signal: SignalStrength) {
        viewModelScope.launch {
            repository.deleteSignal(signal) // Use repository's method
        }
    }
}
