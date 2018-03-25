package com.example.thr.starwarsencyclopedia.mvp.global

import com.example.thr.starwarsencyclopedia.app.SwApi
import rx.Observable

class SwService(private var swApi: SwApi) {

    fun categoryResult(category: String, page: Int) = swApi.categoryResult(category, page)

    fun searchInCategory(category: String, query: String) = swApi.searchInCategory(category, query)

    fun itemBaseDetails(category: String, id: String) = swApi.itemBaseDetails(category, id)

    fun filmDetails(id: String) = swApi.filmDetails(id)

    fun characterDetails(id: String) = swApi.characterDetails(id)

    fun planetDetails(id: String) = swApi.planetDetails(id)

    fun specieDetails(id: String) = swApi.specieDetails(id)

    fun starshipDetails(id: String) = swApi.starshipDetails(id)

    fun vehicleDetails(id: String) = swApi.vehicleDetails(id)
}
