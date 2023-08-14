package de.nilsdruyen.koncept.dogs.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.nilsdruyen.koncept.dogs.entity.BreedSortType

@Composable
fun DogListSortDialog(
    selectedType: BreedSortType,
    setResult: (BreedSortType) -> Unit,
) {
    Column(modifier = Modifier.navigationBarsPadding()) {
        BreedSortType.values().forEach {
            val isSelected = selectedType == it
            Text(
                text = it.toString(),
                color = if (isSelected) Color.Gray else Color.LightGray,
                modifier = Modifier
                    .clickable {
                        setResult(it)
                    }
                    .padding(16.dp)
            )
        }
    }
}