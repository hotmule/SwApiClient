package ru.hotmule.swapiclient.mvp.models.gson.categories

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Character(@Expose var height: String,
                @Expose var mass: String,
                @Expose @SerializedName(value = "hair_color") var hairColor: String,
                @Expose @SerializedName(value = "skin_color") var skinColor: String,
                @Expose @SerializedName(value = "eye_color") var eyeColor: String,
                @Expose @SerializedName(value = "birth_year") var birthYear: String,
                @Expose var gender: String,
                @Expose var homeworld: String,
                @Expose var films: List<String>,
                @Expose var species: List<String>,
                @Expose var vehicles: List<String>,
                @Expose var starships: List<String>)