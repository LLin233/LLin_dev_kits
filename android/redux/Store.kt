/**
 * @author Le Lin
 * @date 3/26/23
 */

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * A [Store] is our state container for a given screen.
 *
 * @param[initialState] This is the initial state of the screen when it is first created.
 * @param[reducer] A system for taking in the current state, and a new action, and outputting the
 * updated state.
 * @param[middlewares] This is a list of [Middleware] entities for handling any side effects
 * for actions dispatched to this store.
 */
class Store<S : State, A : Action, E : SideEffect>(
    initialState: S,
    private val reducer: Reducer<S, A>,
    private val middlewares: List<Middleware<S, A, E>> = emptyList(),
) {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state
    private val _sideEffect = Channel<E>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    private val currentState: S
        get() = _state.value

    /**
     * dispatch
     */
    suspend fun dispatch(action: A, sideEffect: E? = null) {
        middlewares.forEach { middleware ->
            middleware.process(action, currentState, sideEffect, this)
        }

        val newState = reducer.reduce(currentState, action)
        _state.value = newState
        sideEffect?.let {
            _sideEffect.send(sideEffect)
        }
    }
}