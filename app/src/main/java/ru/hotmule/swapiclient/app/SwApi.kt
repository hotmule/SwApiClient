package ru.hotmule.swapiclient.app

import ru.hotmule.swapiclient.mvp.models.gson.Category
import ru.hotmule.swapiclient.mvp.models.gson.ItemBaseDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.hotmule.swapiclient.mvp.models.gson.categories.*
import rx.Observable

interface SwApi {
    companion object {
        var BASE_URL = "https://swapi.co/api/"
        var PAGE_SIZE = 10
    }

    @GET("{category}/")
    fun searchInCategory(@Path("category") category: String,
                         @Query("search") query: String): Observable<Category>

    @GET("{category}/")
    fun categoryResult(@Path("category") category: String,
                       @Query("page") page: Int) : Observable<Category>


    @GET("{category}/{id}/")
    fun itemBaseDetails(@Path("category") category: String,
                        @Path("id") id: String) : Observable<ItemBaseDetails>

    @GET("people/{id}/")
    fun characterDetails(@Path("id") id: String) : Observable<Character>

    @GET("films/{id}/")
    fun filmDetails(@Path("id") id: String) : Observable<Film>

    @GET("planets/{id}/")
    fun planetDetails(@Path("id") id: String) : Observable<Planet>

    @GET("species/{id}/")
    fun specieDetails(@Path("id") id: String) : Observable<Specie>

    @GET("starships/{id}/")
    fun starshipDetails(@Path("id") id: String) : Observable<Starship>

    @GET("vehicles/{id}/")
    fun vehicleDetails(@Path("id") id: String) : Observable<Vehicle>
}