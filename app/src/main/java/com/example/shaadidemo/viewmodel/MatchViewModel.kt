package com.example.shaadidemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shaadidemo.data.connection.NetworkObserver
import com.example.shaadidemo.data.entity.ProfilesEntity
import com.example.shaadidemo.data.repository.MatchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class MatchViewModel(
    private val repository: MatchRepository,
    networkObserver: NetworkObserver
): ViewModel() {

    private val _snackBarChannel = Channel<String>()
    val snackBarFlow = _snackBarChannel.receiveAsFlow()
    private val _selectedEducation = MutableStateFlow<String?>(null)
    val selectedEducation = _selectedEducation.asStateFlow()

    private val _selectedReligion = MutableStateFlow<String?>(null)
    val selectedReligion = _selectedReligion.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val profiles: StateFlow<List<ProfilesEntity>> = combine(
        _selectedEducation,
        _selectedReligion
    ) { edu, rel ->
        edu to rel
    }.flatMapLatest { (edu, rel) ->
        repository.getFilteredProfiles(edu, rel)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    val networkStatus = networkObserver.observe
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NetworkObserver.Status.Available
        )

    fun setEducationFilter(edu: String?) {
        _selectedEducation.value = edu
    }

    fun setReligionFilter(rel: String?) {
        _selectedReligion.value = rel
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        refreshMatches()
    }

    fun refreshMatches() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                repository.fetchAndRefreshCache()
            }catch (_: SocketTimeoutException) {
                _snackBarChannel.send("Connection timed out. Please try again.")
            } catch (_: IOException) {
                _snackBarChannel.send("No internet connection. Showing offline data.")
            } catch (e: HttpException) {
                val msg = when (e.code()) {
                    429 -> "Too many requests. Slow down!"
                    500 -> "Server is having trouble. Try later."
                    else -> "Something went wrong (Error: ${e.code()})"
                }
                _snackBarChannel.send(msg)
            } catch (_: Exception) {
                _snackBarChannel.send("An unexpected error occurred.")
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