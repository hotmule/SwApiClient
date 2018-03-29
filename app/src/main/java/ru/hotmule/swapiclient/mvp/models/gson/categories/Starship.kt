package ru.hotmule.swapiclient.mvp.models.gson.categories

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Starship(@Expose @SerializedName(value = "cost_in_credits") var costInCredits: String,
               @Expose @SerializedName(value = "max_atmosphering_speed") var maxAtmospheringSpeed: String,
               @Expose @SerializedName(value = "cargo_capacity") var cargoCapacity: String,
               @Expose @SerializedName(value = "hyperdrive_rating") var hyperdriveRating: String,
               @Expose @SerializedName(value = "MGLT") var mglt: String,
               @Expose @SerializedName(value = "starship_class") var starshipClass: String,
               @Expose var model: String,
               @Expose var manufacturer: String,
               @Expose var length: String,
               @Expose var crew: String,
               @Expose var passengers: String,
               @Expose var consumables: String,
               @Expose var pilots: List<String>,
               @Expose var films: List<String>)