package com.kalachev.aviv.layer.presentation.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kalachev.aviv.layer.presentation.feed.model.FeedEvent
import com.kalachev.aviv.layer.presentation.feed.model.FeedItemModel
import com.kalachev.aviv.layer.presentation.feed.model.FeedUiAction
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

@Composable
fun FeedContent(
    modifier: Modifier = Modifier,
    feedItems: List<FeedItemModel>,
    isLoading: Boolean,
    errorText: String?,
    handleEvent: (FeedEvent) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
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
    Text(
        text = feedItem.city,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    )
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
        handleEvent = { _ -> /* Handle event for preview */ }
    )
}