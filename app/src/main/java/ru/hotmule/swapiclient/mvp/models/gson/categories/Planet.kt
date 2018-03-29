package ru.hotmule.swapiclient.mvp.models.gson.categories

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Planet(@Expose @SerializedName(value = "rotation_period") var rotationPeriod: String,
             @Expose @SerializedName(value = "orbital_period") var orbitalPeriod: String,
             @Expose @SerializedName(value = "surface_water") var surfaceWater: String,
             @Expose var diameter: String,
             @Expose var climate: String,
             @Expose var gravity: String,
             @Expose var terrain: String,
             @Expose var population: String,
             @Expose var residents: List<String>,
             @Expose var films: List<String>)