package com.kalachev.aviv.layer.presentation.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kalachev.aviv.R
import com.kalachev.aviv.layer.presentation.feed.model.FeedEvent
import com.kalachev.aviv.layer.presentation.feed.model.FeedItemModel
import com.kalachev.aviv.layer.presentation.feed.model.FeedUiAction
import com.kalachev.aviv.ui.theme.Sizing
import com.kalachev.aviv.ui.theme.Spacing
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: FeedViewModel = koinViewModel()
) {
    val feedState by viewModel.feedViewState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleEvent(FeedEvent.ScreenOpened)

        viewModel.feedUiAction.collectLatest { feedUiAction: FeedUiAction ->
            when (feedUiAction) {
                is FeedUiAction.OpenDetails -> navController.navigate("detailsScreen/${feedUiAction.id}")
            }
        }
    }

    FeedContent(
        modifier = modifier,
        feedItems = feedState.items,
        isLoading = feedState.isLoading,
        errorText = feedState.error?.message
    ) { feedEvent ->
        viewModel.handleEvent(feedEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedContent(
    modifier: Modifier = Modifier,
    feedItems: List<FeedItemModel>,
    isLoading: Boolean,
    errorText: String?,
    handleEvent: (FeedEvent) -> Unit
) {
    PullToRefreshBox (
        isRefreshing = isLoading,
        onRefresh = { handleEvent(FeedEvent.PullToRefresh) },
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn {
            items(feedItems) { feedItem ->
                ItemView(feedItem) {
                    handleEvent(FeedEvent.ItemTapped(feedItem.id))
                }
            }
        }
    }
}

@Composable
fun ItemView(feedItem: FeedItemModel, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(Spacing.S),
        elevation = CardDefaults.cardElevation(Spacing.XXS),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = Modifier
            .padding(Spacing.M)
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable { onClick() }
                .padding(bottom = Spacing.M)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(feedItem.thumbUrl)
                    .crossfade(true)
                    .error(R.drawable.ic_launcher_foreground)
                    .placeholder(R.drawable.ic_launcher_background)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Sizing.DASHBOARD_ITEM_IMAGE_HEIGHT),
                contentScale = ContentScale.Crop
            )

            Text(
                text = feedItem.city,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Spacing.M, top = Spacing.M, end = Spacing.M)
            )

            Text(
                text = feedItem.price,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Spacing.M, top = Spacing.M, end = Spacing.M)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedContentPreview() {
    val sampleFeedItems = listOf(
        FeedItemModel(
            id = 1,
            thumbUrl = "https://v.seloger.com/s/crop/590x330/visuels/1/7/t/3/17t3fitclms3bzwv8qshbyzh9dw32e9l0p0udr80k.jpg",
            price = "1500000.0",
            city = "Villers-sur-Mer"
        ),
        FeedItemModel(
            id = 2,
            thumbUrl = "https://v.seloger.com/s/crop/590x330/visuels/2/a/l/s/2als8bgr8sd2vezcpsj988mse4olspi5rfzpadqok.jpg",
            price = "3500000.0",
            city = "Deauville"
        ),
        FeedItemModel(
            id = 3,
            thumbUrl = null,
            price = "3000000.0",
            city = "Bordeaux"
        ),
        FeedItemModel(
            id = 4,
            thumbUrl = "https://v.seloger.com/s/crop/590x330/visuels/1/9/f/x/19fx7n4og970dhf186925d7lrxv0djttlj5k9dbv8.jpg",
            price = "5000000.0",
            city = "Nice"
        )
    )

    FeedContent(
        feedItems = sampleFeedItems,
        isLoading = false,
        errorText = null,
        handleEvent = { _ -> }
    )
}