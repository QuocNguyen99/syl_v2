package com.hqnguyen.syl_v2.persentation.page.map_record

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hqnguyen.syl_v2.data.InfoTracking
import com.hqnguyen.syl_v2.data.entity.InfoRecordEntity
import com.hqnguyen.syl_v2.data.entity.RecordEntity
import com.hqnguyen.syl_v2.data.repository.InfoRecordRepositoryImpl
import com.hqnguyen.syl_v2.data.repository.RecordRepositoryImpl
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
    private val repositoryInfoImpl: InfoRecordRepositoryImpl
) : ViewModel() {

    private val mutableState = MutableStateFlow(MapState())
    val state: StateFlow<MapState> = mutableState.asStateFlow()

    private var currentRecord: RecordEntity? = null
    private var jobCountTime: Job? = null
    fun handleEvent(event: MapEvent) = when (event) {
        is MapEvent.UpdateInfoTracking -> updateInfoTracking(event.infoTracking)
        is MapEvent.Start -> startRecord()
        is MapEvent.Stop -> stopRecord()
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
        stopCountTime()
        viewModelScope.launch {
            updateRecordLocal(mutableState.value.countTime)
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
                    repositoryRecordImpl.updateRecord(
                        it.copy(countTime = countTime)
                    )
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
