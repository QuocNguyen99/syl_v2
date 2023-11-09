package com.hqnguyen.syl_v2.ui.page.map_record

import android.R.id
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hqnguyen.syl_v2.data.InfoTracking
import com.hqnguyen.syl_v2.data.entity.InfoRecordEntity
import com.hqnguyen.syl_v2.data.entity.RecordEntity
import com.hqnguyen.syl_v2.data.repository.InfoRecordRepositoryImpl
import com.hqnguyen.syl_v2.data.repository.RecordRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapRecordViewModel @Inject constructor(
    private val repositoryRecordImpl: RecordRepositoryImpl,
    private val repositoryInfoImpl: InfoRecordRepositoryImpl
) :
    ViewModel() {

    private val mutableState = MutableStateFlow(MapState())
    val state: StateFlow<MapState> = mutableState.asStateFlow()

    private var currentRecord: RecordEntity? = null

    fun handleEvent(event: MapEvent) = when (event) {
        is MapEvent.UpdateInfoTracking -> updateInfoTracking(event.infoTracking)
        is MapEvent.Start -> updateTimeStart()
        is MapEvent.Stop -> stopRecord(event.countTime)
    }

    private fun updateTimeStart() {
        viewModelScope.launch {
            mutableState.emit(
                mutableState.value.copy(
                    timeStart = System.currentTimeMillis()
                )
            )
            saveRecordLocal()
        }
    }

    private fun stopRecord(countTime: Long) {
        viewModelScope.launch {
            updateRecordLocal(countTime)
        }
    }

    private fun updateInfoTracking(infoTracking: InfoTracking) {
        viewModelScope.launch {
            mutableState.emit(
                mutableState.value.copy(
                    infoTracking = infoTracking
                )
            )
            saveInfoRecordLocal(infoTracking)
        }
    }

    private fun saveRecordLocal() {
        viewModelScope.launch(Dispatchers.IO) {
            currentRecord = RecordEntity(
                id = System.currentTimeMillis(),
                timeStart = state.value.timeStart,
                countTime = 0L
            )

            currentRecord?.let {
                repositoryRecordImpl.insertRecord(
                    it
                )
            }
        }
    }

    private fun updateRecordLocal(countTime: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            currentRecord?.let {
                repositoryRecordImpl.updateRecord(
                    it.copy(countTime = countTime)
                )
            }
        }
    }

    private fun saveInfoRecordLocal(infoTracking: InfoTracking) {
        viewModelScope.launch(Dispatchers.IO) {
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
        }
    }
}