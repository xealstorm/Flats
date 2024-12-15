package com.kalachev.aviv.layer.presentation.details

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kalachev.aviv.layer.presentation.details.model.DetailsEvent
import com.kalachev.aviv.layer.presentation.details.model.DetailsModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    detailsId: Long?,
    detailsViewModel: DetailsViewModel = koinViewModel()
) {
    val detailsState = detailsViewModel.detailsState.collectAsState()

    LaunchedEffect(Unit) {
        detailsViewModel.handleEvent(DetailsEvent.ScreenOpened(detailsId ?: 0))
    }

    DetailsContent(
        modifier = modifier,
        details = detailsState.value.details,
        isLoading = detailsState.value.isLoading,
        errorText = detailsState.value.error?.message
    )
}

@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    details: DetailsModel? = null,
    isLoading: Boolean = false,
    errorText: String? = null
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        text = details?.imageUrl ?: "No details",
        style = MaterialTheme.typography.bodyLarge
    )
}
