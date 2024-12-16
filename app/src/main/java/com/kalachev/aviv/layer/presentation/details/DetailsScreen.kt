package com.kalachev.aviv.layer.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kalachev.aviv.R
import com.kalachev.aviv.layer.presentation.details.model.DetailsErrorCode
import com.kalachev.aviv.layer.presentation.details.model.DetailsEvent
import com.kalachev.aviv.layer.presentation.details.model.DetailsModel
import com.kalachev.aviv.ui.theme.Sizing
import com.kalachev.aviv.ui.theme.Spacing
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    detailsId: Long?,
    detailsViewModel: DetailsViewModel = koinViewModel()
) {
    val detailsState by detailsViewModel.detailsState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val actionLabel = stringResource(R.string.cta_retry)
    val message = detailsState.error?.let { detailsErrorCode ->
        stringResource(
            when (detailsErrorCode) {
                DetailsErrorCode.REMOTE_EXCEPTION -> R.string.error_code_remote
                DetailsErrorCode.LOCAL_EXCEPTION -> R.string.error_code_local
                DetailsErrorCode.UNKNOWN_EXCEPTION -> R.string.error_code_unknown
            }
        )

    }

    LaunchedEffect(Unit) {
        detailsViewModel.handleEvent(DetailsEvent.ScreenOpened(detailsId ?: 0))
    }

    LaunchedEffect(detailsState.error) {
        if (detailsState.error != null && message != null) {
            val snackbarResult = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = SnackbarDuration.Indefinite
            )
            if (snackbarResult == SnackbarResult.ActionPerformed) {
                detailsViewModel.handleEvent(DetailsEvent.Refresh)
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        DetailsContent(
            details = detailsState.details,
            isLoading = detailsState.isLoading,
        )
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    details: DetailsModel? = null,
    isLoading: Boolean = false,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = Spacing.M),
            verticalArrangement = Arrangement.Top
        ) {
            if (details?.imageUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(details.imageUrl)
                        .crossfade(true)
                        .error(R.drawable.ic_launcher_foreground)
                        .placeholder(R.drawable.ic_launcher_background)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Sizing.DETAILS_ITEM_IMAGE_HEIGHT),
                    contentScale = ContentScale.Crop
                )
            }

            if (details?.city != null) {
                Text(
                    text = details.city,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Spacing.M, top = Spacing.M, end = Spacing.M)
                )
            }

            if (details?.price != null) {
                Text(
                    text = details.price,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Spacing.M, top = Spacing.M, end = Spacing.M)
                )
            }

            if (details?.bedrooms != null) {
                Text(
                    text = details.bedrooms.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Spacing.M, top = Spacing.M, end = Spacing.M)
                )
            }

            if (details?.rooms != null) {
                Text(
                    text = details.rooms.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Spacing.M, top = Spacing.M, end = Spacing.M)
                )
            }

            if (details?.area != null) {
                Text(
                    text = details.area.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Spacing.M, top = Spacing.M, end = Spacing.M)
                )
            }

            if (details?.professional != null) {
                Text(
                    text = details.professional,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Spacing.M, top = Spacing.M, end = Spacing.M)
                )
            }

            if (details?.offerType != null) {
                Text(
                    text = details.offerType.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Spacing.M, top = Spacing.M, end = Spacing.M)
                )
            }

            if (details?.propertyType != null) {
                Text(
                    text = details.propertyType,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Spacing.M, top = Spacing.M, end = Spacing.M)
                )
            }
        }
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(Spacing.M)
                    .align(Alignment.TopCenter),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsContentPreview() {
    val sampleDetailsModel = DetailsModel(
        id = 1,
        imageUrl = "https://v.seloger.com/s/crop/590x330/visuels/1/7/t/3/17t3fitclms3bzwv8qshbyzh9dw32e9l0p0udr80k.jpg",
        price = "1500000.0",
        city = "Villers-sur-Mer",
        bedrooms = 3,
        area = 550.0,
        professional = "GSL OWNERS",
        propertyType = "Maison - Villa",
        offerType = 1,
        rooms = 7
    )

    DetailsContent(
        details = sampleDetailsModel,
        isLoading = true,
    )
}