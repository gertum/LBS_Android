package com.example.os_app_gertum1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.os_app_gertum1.data.database.SignalStrengthDao

class SignalStrengthViewModelFactory(private val dao: SignalStrengthDao) : ViewModelProvider.Factory {

    // The create method is used to instantiate your ViewModel.
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignalStrengthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignalStrengthViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
