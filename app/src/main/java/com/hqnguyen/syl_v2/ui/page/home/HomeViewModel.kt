package com.hqnguyen.syl_v2.ui.page.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hqnguyen.syl_v2.data.repository.RecordRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repositoryImpl: RecordRepositoryImpl) :
    ViewModel() {
        private val TAG = this.javaClass.name
    init {
        getAllRecord()
    }

    private val mutableState = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = mutableState.asStateFlow()

    private fun getAllRecord() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryImpl.getAllRecord().collect {
                Log.d(TAG, "getAllRecord: $it")
                mutableState.emit(mutableState.value.copy(listRecord = it))
            }
        }
    }
}