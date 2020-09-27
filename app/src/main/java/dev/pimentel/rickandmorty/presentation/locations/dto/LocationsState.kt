package dev.pimentel.rickandmorty.presentation.locations.dto

import androidx.annotation.DrawableRes
import dev.pimentel.rickandmorty.R

data class LocationsState(
    val locations: List<LocationsItem> = emptyList(),
    @DrawableRes val filterIcon: Int = R.drawable.ic_filter_default,
    val scrollToTheTop: Unit? = null,
    val errorMessage: String? = null
)
