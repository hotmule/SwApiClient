package ru.hotmule.swapiclient.mvp.models.gson.categories

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Specie(@Expose @SerializedName(value = "average_height") var averageHeight: String,
             @Expose @SerializedName(value = "skin_colors") var skinColors: String,
             @Expose @SerializedName(value = "hair_colors") var hairColors: String,
             @Expose @SerializedName(value = "eye_colors") var eyeColors: String,
             @Expose @SerializedName(value = "average_lifespan") var averageLifespan: String,
             @Expose var classification: String,
             @Expose var designation: String,
             @Expose var homeworld: String,
             @Expose var language: String,
             @Expose var people: List<String>,
             @Expose var films: List<String>)