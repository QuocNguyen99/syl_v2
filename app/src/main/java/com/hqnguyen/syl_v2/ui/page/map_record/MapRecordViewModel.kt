package com.hqnguyen.syl_v2.ui.page.map_record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hqnguyen.syl_v2.data.InfoTracking
import com.hqnguyen.syl_v2.data.entity.RecordEntity
import com.hqnguyen.syl_v2.data.repository.RecordRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapRecordViewModel @Inject constructor(private val repositoryImpl: RecordRepositoryImpl) :
    ViewModel() {

    private val mutableState = MutableStateFlow(MapState())
    val state: StateFlow<MapState> = mutableState.asStateFlow()

    fun handleEvent(event: MapEvent) = when (event) {
        is MapEvent.UpdateInfoTracking -> updateInfoTracking(event.infoTracking)
        is MapEvent.Start -> updateTimeStart()
    }

    private fun updateTimeStart() {
        viewModelScope.launch {
            mutableState.emit(
                mutableState.value.copy(
                    timeStart = System.currentTimeMillis()
                )
            )
        }
    }

    private fun updateInfoTracking(infoTracking: InfoTracking) {
        viewModelScope.launch {
            mutableState.emit(
                mutableState.value.copy(
                    infoTracking = infoTracking
                )
            )
            saveRecord(infoTracking)
        }
    }

    private fun saveRecord(infoTracking: InfoTracking) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryImpl.insertRecord(
                RecordEntity(
                    id = System.currentTimeMillis(),
                    speed = infoTracking.speed,
                    calo = infoTracking.kCal,
                    distance = infoTracking.distance.toString(),
                    timeStart = state.value.timeStart
                )
            )
        }
    }
}