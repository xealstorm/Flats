package com.kalachev.aviv.layer.presentation.details.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailsViewState(
    val details: DetailsModel? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null
): Parcelable