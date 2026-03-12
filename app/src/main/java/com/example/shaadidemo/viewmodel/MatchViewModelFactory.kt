package com.example.shaadidemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shaadidemo.data.connection.NetworkObserver
import com.example.shaadidemo.data.repository.MatchRepository

class MatchViewModelFactory(
    private val repository: MatchRepository,
    private val networkObserver: NetworkObserver
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MatchViewModel(repository, networkObserver) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}