class StoreTest {

    @Test
    fun dispatchSentActionToReducerAndMiddlewares() {
        val inputState = TestState
        val inputAction = TestAction
        val inputSideEffect = TestSideEffect
        val reducer = TestReducer()
        val middleware = ActionCaptureMiddleware<State, Action, SideEffect>()

        val store = Store(
            inputState,
            reducer,
            listOf(middleware),
        )

        runBlocking {
            store.dispatch(inputAction, inputSideEffect)
        }
        middleware.assertActionProcessed(inputAction)
        middleware.assertSideEffectProcessed(inputSideEffect)
        reducer.assertActionProcessed(inputAction)
    }
}

object TestState : State
object TestAction : Action
object TestSideEffect : SideEffect

class TestReducer : Reducer<State, Action> {
    private val actionHistory: MutableList<Action> = mutableListOf()
    override fun reduce(currentState: State, action: Action): State {
        actionHistory.add(action)
        return currentState
    }

    fun assertActionProcessed(expectedAction: Action) {
        assertTrue(actionHistory.contains(expectedAction))
    }
}