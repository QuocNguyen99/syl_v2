package com.hqnguyen.syl_v2.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface BaseEvent
interface BaseState
interface BaseEffect
interface HandleEvent {
    fun processEvent(event: BaseEvent)
}

val emptyUiState = object : BaseState {}

abstract class BaseViewModel<Event : BaseEvent, State : BaseState, Effect : BaseEffect> : ViewModel(), HandleEvent {
    private val initialState: State by lazy { createInitialState() }

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event = _event.asSharedFlow()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    /**
     * Init ui state. Default: [emptyUiState].
     *
     * For ViewModel that don't need impl [BaseState] -> Don't need to override this func.
     */
    @Suppress("UNCHECKED_CAST")
    protected open fun createInitialState(): State {
        return emptyUiState as State
    }

    /**
     * Set new Ui State
     */
    protected fun updateUiState(reduce: State.() -> State) {
        val newState = _uiState.value.reduce()
        _uiState.value = newState
    }

    /**
     * Set new Effect
     */
    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }
}