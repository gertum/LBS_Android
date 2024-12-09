package com.example.os_app_gertum1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.os_app_gertum1.data.repository.SignalStrengthRepository

class SignalStrengthViewModelFactory(private val repository: SignalStrengthRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignalStrengthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignalStrengthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
