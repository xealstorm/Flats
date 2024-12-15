package com.kalachev.aviv.di

import androidx.lifecycle.SavedStateHandle
import com.kalachev.aviv.layer.presentation.details.DetailsViewModel
import com.kalachev.aviv.layer.presentation.feed.FeedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<FeedViewModel> { (handle: SavedStateHandle) ->
        FeedViewModel(
            getAllFlats = get(),
            syncFlats = get(),
            savedStateHandle = handle,
            feedItemMappings = get(),
        )
    }

    viewModel<DetailsViewModel>{ (handle: SavedStateHandle) ->
        DetailsViewModel(
            getFlatDetails = get(),
            syncFlatDetails = get(),
            savedStateHandle = handle,
            detailsMappings = get(),
        )
    }
}
