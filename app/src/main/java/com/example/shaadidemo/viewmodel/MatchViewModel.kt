package com.example.shaadidemo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shaadidemo.data.entity.ProfilesEntity
import com.example.shaadidemo.data.repository.MatchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class MatchViewModel(
    private val repository: MatchRepository
): ViewModel() {

    val profiles: StateFlow<List<ProfilesEntity>> = repository.allProfiles
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        Log.d("MatchViewModel", "init called")
        refreshMatches()
    }

    fun refreshMatches() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                repository.fetchAndRefreshCache()
            } catch (e: Exception) {
                Log.e("MatchViewModel", "Refresh failed", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun acceptUser(uuid: String) {
        viewModelScope.launch {
            repository.updateStatus(uuid, "ACCEPTED")
        }
    }

    fun declineUser(uuid: String) {
        viewModelScope.launch {
            repository.updateStatus(uuid, "DECLINED")
        }
    }
}