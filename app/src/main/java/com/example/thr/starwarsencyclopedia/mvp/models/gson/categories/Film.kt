package com.example.thr.starwarsencyclopedia.mvp.models.gson.categories

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Film(@Expose @SerializedName(value = "episode_id") var episodeId: Int,
           @Expose @SerializedName(value = "opening_crawl") var openingCrawl: String,
           @Expose @SerializedName(value = "release_date") var releaseDate: String,
           @Expose var director: String,
           @Expose var producer: String,
           @Expose var characters: List<String>,
           @Expose var planets: List<String>,
           @Expose var starships: List<String>,
           @Expose var vehicles: List<String>,
           @Expose var species: List<String>)