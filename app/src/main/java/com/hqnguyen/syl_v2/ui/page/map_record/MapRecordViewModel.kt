package com.hqnguyen.syl_v2.ui.page.map_record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hqnguyen.syl_v2.data.InfoTracking
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapRecordViewModel @Inject constructor() : ViewModel() {

    private val mutableState = MutableStateFlow(MapState())
    val state: StateFlow<MapState> = mutableState.asStateFlow()

    fun handleEvent(event: MapEvent) = when (event) {
        is MapEvent.UpdateInfoTracking -> updateInfoTracking(event.infoTracking)
    }

    fun updateInfoTracking(infoTracking: InfoTracking){
        viewModelScope.launch {
            mutableState.emit(mutableState.value.copy(
                infoTracking = infoTracking
            ))
        }
    }
}