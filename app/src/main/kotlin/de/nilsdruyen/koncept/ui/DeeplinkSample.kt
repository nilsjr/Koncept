package de.nilsdruyen.koncept.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import de.nilsdruyen.koncept.navigation.DeeplinkRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * Deeplink sample
 *
 * send via terminal:
 * adb shell am start -W -a android.intent.action.VIEW
 * -d "koncept://deeplink/2022-11-10T10%3A53%3A19.000Z?rawDate2=2022-11-10T10%3A53%3A19.000Z"
 */

@Composable
fun DeeplinkSample(
    route: DeeplinkRoute,
    viewModel: DeeplinkViewModel = hiltViewModel<DeeplinkViewModel, DeeplinkViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(route)
        },
    ),
) {
    val date = viewModel.dateState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        Text(date.value, modifier = Modifier.align(Alignment.Center))
    }
}

private const val INPUT_DATE = "uuuu-MM-dd'T'HH:mm:ss[.SSSX][Z][ZZZZZ]"
internal val inputFormatter = DateTimeFormatter.ofPattern(INPUT_DATE)

@HiltViewModel(assistedFactory = DeeplinkViewModel.Factory::class)
class DeeplinkViewModel @AssistedInject constructor(@Assisted route: DeeplinkRoute) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(route: DeeplinkRoute): DeeplinkViewModel
    }

    val dateState = MutableStateFlow("")

    init {
        val rawDate = route.rawDate.ifEmpty { "empty" }
        val rawDate2 = route.rawDate2.ifEmpty { "empty" }

        dateState.value = "$rawDate - $rawDate2"

        if (route.rawDate2.isNotEmpty()) {
            viewModelScope.launch {
                delay(2000)
                runCatching { OffsetDateTime.parse(route.rawDate2, inputFormatter) }
                    .onSuccess { date -> dateState.value = "$rawDate - ${date.month.name}" }
                    .onFailure { dateState.value = "$rawDate - invalid" }
            }
        }
    }
}
