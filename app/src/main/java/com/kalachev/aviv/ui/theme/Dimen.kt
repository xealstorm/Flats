package com.kalachev.aviv.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.kalachev.aviv.R

object Spacing {
    val NONE:Dp
        @Composable
        get() = dimensionResource(id = R.dimen.spacing_none)
    val XXS:Dp
        @Composable
        get() = dimensionResource(id = R.dimen.spacing_xxs)
    val XS:Dp
        @Composable
        get() = dimensionResource(id = R.dimen.spacing_xs)
    val S:Dp
        @Composable
        get() = dimensionResource(id = R.dimen.spacing_s)
    val M:Dp
        @Composable
        get() = dimensionResource(id = R.dimen.spacing_m)
    val L:Dp
        @Composable
        get() = dimensionResource(id = R.dimen.spacing_l)
    val XL:Dp
        @Composable
        get() = dimensionResource(id = R.dimen.spacing_xl)
}

object Sizing {
    val DASHBOARD_ITEM_IMAGE_HEIGHT: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.dashboard_item_image_height)

    val DETAILS_ITEM_IMAGE_HEIGHT: Dp
        @Composable
        get() = dimensionResource(id = R.dimen.details_item_image_height)
}