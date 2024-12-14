package {package}.core.domain.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


/**
 * sealed interface XYZEvent {
 *     data class Error(
 *         val error: NetworkError,
 *     ) : XYZEvent
 * }
 *
 * ObserveAsEvents(events = viewModel.events) { event ->
 *     when (event) {
 *         is XYZEvent.Error ->
 *             Toast
 *                 .makeText(
 *                     context,
 *                     event.error.toString(context),
 *                     Toast.LENGTH_LONG,).show()
 *     }
 * }
 */

@Composable
fun <T> ObserveAsEvents(
    events: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: (T) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            // deal with edge case that event may lost when config change like screen rotated
            withContext(Dispatchers.Main.immediate) {
                events.collect(onEvent)
            }
        }
    }
}
