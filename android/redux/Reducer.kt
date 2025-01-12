
/**
 * @author Le Lin
 * @date 3/26/23
 */

interface Reducer<S : State, A : Action> {
    /**
     * Given a [currentState], [action] and [SideEffect]that the user took, produce a new [State].
     *
     * This will give us clear and predictable state management, that ensures each state is associated
     * with some specific user intent or action.
     */
    fun reduce(currentState: S, action: A): S
}