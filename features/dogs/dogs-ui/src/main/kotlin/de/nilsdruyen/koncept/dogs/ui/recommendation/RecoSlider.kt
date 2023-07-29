package de.nilsdruyen.koncept.dogs.ui.recommendation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun RecoSlider(
    viewModel: RecoDelegate,
) {
    val state by viewModel.recoState().collectAsStateWithLifecycle()

    RecoSliderContent(state) {
        viewModel.action(it)
    }
}

@Composable
private fun RecoSliderContent(
    state: RecoState,
    sendIntent: (RecoIntent) -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(state.recoList, key = { it.id.value }) {
            RecoItem(it) {
                val intent = if (it.isFavorite) {
                    RecoIntent.RemoveFavorite(it.id)
                } else {
                    RecoIntent.SetFavorite(it.id)
                }
                sendIntent(intent)
            }
        }
    }
}