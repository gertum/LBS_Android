package com.example.os_app_gertum1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.os_app_gertum1.data.database.SignalStrength
import com.example.os_app_gertum1.data.database.SignalStrengthDao
import kotlinx.coroutines.launch

class SignalStrengthViewModel(private val dao: SignalStrengthDao) : ViewModel() {
    val allSignals: LiveData<List<SignalStrength>> = dao.getAllSignalStrengths()

    fun addSignal(signal: SignalStrength) {
        viewModelScope.launch {
            dao.insertSignalStrength(signal)
        }
    }
}
