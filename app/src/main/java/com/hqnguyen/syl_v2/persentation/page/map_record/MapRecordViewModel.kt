package com.hqnguyen.syl_v2.persentation.page.map_record

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.hqnguyen.syl_v2.core.BaseEvent
import com.hqnguyen.syl_v2.core.BaseViewModel
import com.hqnguyen.syl_v2.data.InfoTracking
import com.hqnguyen.syl_v2.data.entity.InfoRecordEntity
import com.hqnguyen.syl_v2.data.entity.RecordEntity
import com.hqnguyen.syl_v2.data.location.LocationAction
import com.hqnguyen.syl_v2.data.location.MapLocation
import com.hqnguyen.syl_v2.data.repository.InfoRecordRepositoryImpl
import com.hqnguyen.syl_v2.data.repository.RecordRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapRecordViewModel @Inject constructor(
    private val repositoryRecordImpl: RecordRepositoryImpl,
    private val repositoryInfoImpl: InfoRecordRepositoryImpl,
    private val locationManager: LocationAction
) : BaseViewModel<MapEvent, MapState, MapEffect>() {
    private val TAG = this.javaClass.name

    override fun createInitialState(): MapState = MapState()

    private var currentRecord: RecordEntity? = null
    private var jobTrackingLocation: Job? = null
    private var jobCountTime: Job? = null

    override fun processEvent(event: BaseEvent) {
        when (event) {
            MapEvent.StartTrackingLocation -> startTrackingLocation()
            is MapEvent.UpdateInfoTracking -> updateInfoTracking(event.infoTracking)
            MapEvent.GetCurrentLocation -> getCurrentLocation()
            is MapEvent.Start -> startRecord()
            is MapEvent.Stop -> stopRecord()
        }
    }

    private fun startTrackingLocation() {
        jobTrackingLocation?.cancel()
        jobTrackingLocation = viewModelScope.launch(Dispatchers.IO) {
            locationManager
                .getLocationUpdate(1000L)
                .catch { e -> Log.e(TAG, "startTrackingLocation: e.message ${e.message}") }
                .map { location ->
                    val lat = location.latitude.toString().takeLast(3)
                    val long = location.longitude.toString().takeLast(3)
                    MapLocation(long.toDouble(), lat.toDouble())
                }
                .collectLatest {
                    updateUiState { copy(currentLocation = it) }
                }
        }
    }

    private fun getCurrentLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentLocation = locationManager.getCurrentLocation()
                val lat = currentLocation.latitude.toString().takeLast(3)
                val long = currentLocation.longitude.toString().takeLast(3)
                updateUiState {
                    copy(currentLocation = MapLocation(long.toDouble(), lat.toDouble()))
                }
            } catch (ex: Exception) {
                Log.d(TAG, "getCurrentLocation ex: $ex")
            }
        }
    }

    private fun startCountTime() {
        jobCountTime = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                Log.d(TAG, "startCountTime: MapUiState ${uiState.value.countTime}")
                if (uiState.value.isRecord && isActive) {
                    updateUiState { copy(countTime = uiState.value.countTime + 1) }
                    delay(1000)
                }
            }
        }
    }

    private fun stopCountTime() {
        jobCountTime?.cancel()
        viewModelScope.launch(Dispatchers.IO) {
            updateUiState { copy(countTime = 0) }
        }
    }

    private fun startRecord() {
        Log.d(TAG, "startRecord")
        viewModelScope.launch {
            val timeStart = System.currentTimeMillis()
            updateUiState { copy(timeStart = timeStart, isRecord = true) }
            startCountTime()
            saveRecordLocal(timeStart)
        }
    }

    private fun stopRecord() {
        Log.d(TAG, "stopRecord")
        val countTime = uiState.value.countTime
        stopCountTime()
        viewModelScope.launch {
            updateRecordLocal(countTime)
            updateUiState { MapState() }
        }
    }

    private fun updateInfoTracking(infoTracking: InfoTracking) {
        viewModelScope.launch {
            try {
                updateUiState {
                    copy(
                        infoTracking = infoTracking
                    )
                }
                saveInfoRecordLocal(infoTracking)
            } catch (ex: Exception) {
                Log.e(TAG, "updateInfoTracking: ${ex.message}")
            }
        }
    }

    private fun saveRecordLocal(timeStart: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                currentRecord = RecordEntity(
                    id = System.currentTimeMillis(),
                    timeStart = timeStart,
                    countTime = 0L
                )

                currentRecord?.let {
                    repositoryRecordImpl.insertRecord(
                        it
                    )
                }
            } catch (ex: Exception) {
                Log.e(TAG, "saveRecordLocal: ${ex.message}")
            }
        }
    }

    private fun updateRecordLocal(countTime: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                currentRecord?.let {
                    val newRecord = it.copy(countTime = countTime)
                    Log.d(TAG, "updateRecordLocal newRecord: $newRecord")
                    val result = repositoryRecordImpl.updateRecord(newRecord)
                    Log.e(TAG, "updateRecordLocal status update: $result")
                }
            } catch (ex: Exception) {
                Log.e(TAG, "updateRecordLocal: ${ex.message}")
            }
        }
    }

    private fun saveInfoRecordLocal(infoTracking: InfoTracking) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                currentRecord?.let {
                    repositoryInfoImpl.insertInfoRecord(
                        InfoRecordEntity(
                            id = System.currentTimeMillis(),
                            idRecord = it.id,
                            speed = infoTracking.speed,
                            calo = infoTracking.kCal,
                            distance = infoTracking.distance.toString(),
                        )
                    )
                }
            } catch (ex: Exception) {
                Log.e(TAG, "updateRecordLocal: ${ex.message}")
            }
        }
    }
}
