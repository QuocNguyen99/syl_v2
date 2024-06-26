package com.hqnguyen.syl_v2.persentation.page.map_record

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hqnguyen.syl_v2.data.InfoTracking
import com.hqnguyen.syl_v2.data.entity.InfoRecordEntity
import com.hqnguyen.syl_v2.data.entity.RecordEntity
import com.hqnguyen.syl_v2.data.repository.InfoRecordRepositoryImpl
import com.hqnguyen.syl_v2.data.repository.RecordRepositoryImpl
import com.hqnguyen.syl_v2.service.LocationAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapRecordViewModel @Inject constructor(
    private val repositoryRecordImpl: RecordRepositoryImpl,
    private val repositoryInfoImpl: InfoRecordRepositoryImpl,
    private val locationManager: LocationAction
) : ViewModel() {
    companion object {
        private const val TAG = "MapRecordViewModel"
    }

    private val mutableState = MutableStateFlow(MapState())
    val state: StateFlow<MapState> = mutableState.asStateFlow()

    private var currentRecord: RecordEntity? = null
    private var jobCountTime: Job? = null

    fun handleEvent(event: MapEvent) {
        when (event) {
            is MapEvent.UpdateInfoTracking -> updateInfoTracking(event.infoTracking)
            MapEvent.GetCurrentLocation -> getCurrentLocation()
            is MapEvent.Start -> startRecord()
            is MapEvent.Stop -> stopRecord()
        }
    }

    private fun getCurrentLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentLocation = locationManager.getCurrentLocation()
                mutableState.emit(mutableState.value.copy(currentLocation = currentLocation))
            } catch (ex: Exception) {
                Log.d(TAG, "getCurrentLocation ex: $ex")
            }
        }
    }

    private fun startCountTime() {
        jobCountTime = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                Log.d(TAG, "startCountTime: MapUiState ${mutableState.value.countTime}")
                if (mutableState.value.isRecord && isActive) {
                    mutableState.emit(
                        mutableState.value.copy(
                            countTime = mutableState.value.countTime + 1,
                        )
                    )
                    delay(1000)
                }
            }
        }
    }

    private fun stopCountTime() {
        jobCountTime?.cancel()
        viewModelScope.launch(Dispatchers.IO) {
            mutableState.emit(
                mutableState.value.copy(
                    countTime = 0,
                )
            )
        }
    }

    private fun startRecord() {
        viewModelScope.launch {
            val timeStart = System.currentTimeMillis()
            mutableState.emit(
                mutableState.value.copy(
                    timeStart = timeStart,
                    isRecord = true
                )
            )
            startCountTime()
            saveRecordLocal(timeStart)
        }
    }

    private fun stopRecord() {
        Log.d(TAG, "stopRecord")
        val countTime = mutableState.value.countTime
        stopCountTime()
        viewModelScope.launch {
            updateRecordLocal(countTime)
            mutableState.emit(MapState())
        }
    }

    private fun updateInfoTracking(infoTracking: InfoTracking) {
        viewModelScope.launch {
            try {
                mutableState.emit(
                    mutableState.value.copy(
                        infoTracking = infoTracking
                    )
                )
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
