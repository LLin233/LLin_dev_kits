class ActionCaptureMiddleware<S : State, A : Action, E : SideEffect> : Middleware<S, A, E> {
    private val actionHistory: MutableList<Action> = mutableListOf()
    private val sideEffectHistory: MutableList<SideEffect> = mutableListOf()

    override suspend fun process(
        action: A,
        currentState: S,
        sideEffect: E?,
        store: Store<S, A, E>,
    ) {
        actionHistory.add(action)
        sideEffect?.apply {
            sideEffectHistory.add(this)
        }
    }

    fun assertActionProcessed(expectedAction: Action) {
        assertTrue(actionHistory.contains(expectedAction))
    }

    fun assertSideEffectProcessed(expectedSideEffect: SideEffect) {
        assertTrue(sideEffectHistory.contains(expectedSideEffect))
    }
}