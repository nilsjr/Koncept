package de.nilsdruyen.koncept.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Deeplink sample
 *
 * send via terminal:
 * adb shell am start -W -a android.intent.action.VIEW -d "koncept://deeplink/2022-11-10T10%3A53%3A19.000Z?rawDate2=2022-11-10T10%3A53%3A19.000Z"
 */

@Composable
fun DeeplinkSample(viewModel: DeeplinkViewModel = hiltViewModel()) {
    val date = viewModel.dateState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        Text(date.value, modifier = Modifier.align(Alignment.Center))
    }
}

private const val INPUT_DATE = "uuuu-MM-dd'T'HH:mm:ss[.SSSX][Z][ZZZZZ]"
internal val inputFormatter = DateTimeFormatter.ofPattern(INPUT_DATE)

@HiltViewModel
class DeeplinkViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    val dateState = MutableStateFlow("")

    init {
        val rawDate = savedStateHandle.get<String>("rawDate") ?: "empty"
        val rawDate2 = savedStateHandle.get<String>("rawDate2") ?: "empty"

        dateState.value = "$rawDate - $rawDate2"

        viewModelScope.launch {
            delay(2000)
            val date = OffsetDateTime.parse(rawDate2, inputFormatter)
            dateState.value = "$rawDate - ${date.month.name}"
        }
    }
}