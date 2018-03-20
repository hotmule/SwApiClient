package com.example.thr.starwarsencyclopedia.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.example.thr.starwarsencyclopedia.mvp.SwService
import javax.inject.Inject;
import com.example.thr.starwarsencyclopedia.app.SwApp
import com.example.thr.starwarsencyclopedia.common.Utils
import com.example.thr.starwarsencyclopedia.app.SwApi
import com.example.thr.starwarsencyclopedia.mvp.models.gson.ItemBaseDetails
import com.example.thr.starwarsencyclopedia.mvp.models.gson.categories.*
import com.example.thr.starwarsencyclopedia.mvp.views.ItemView
import rx.Observable
import kotlin.reflect.full.memberProperties

@InjectViewState
class ItemPresenter : BasePresenter<ItemView>() {

    @Inject
    lateinit var swService: SwService

    init {
        SwApp.appComponent.inject(this)
    }

    private lateinit var itemCategory: String
    private lateinit var categories: Array<String>

    private lateinit var stringDetails: ArrayList<String>
    private lateinit var itemDetails: ArrayList<ArrayList<ItemBaseDetails>>

    fun requestItemDetails(itemCategory: String, itemId: String, categories: Array<String>) {

        viewState.makeProgressBarVisible()

        this.itemCategory = itemCategory
        this.categories = categories

        when (itemCategory) {
            categories[0] -> loadItemDetails(swService.filmDetails(itemId))
            categories[1] -> loadItemDetails(swService.characterDetails(itemId))
            categories[2] -> loadItemDetails(swService.planetDetails(itemId))
            categories[3] -> loadItemDetails(swService.specieDetails(itemId))
            categories[4] -> loadItemDetails(swService.starshipDetails(itemId))
            categories[5] -> loadItemDetails(swService.vehicleDetails(itemId))
        }
    }

    private fun loadItemDetails(observable: Observable<*>) {
        val subscription = observable
                .compose(Utils.applySchedulers())
                .subscribe(
                        { answer -> onLoadingSuccess(answer) },
                        { error -> onLoadingFailed() })
        unSubscribeOnDestroy(subscription)
    }

    private fun onLoadingSuccess(answer: Any) {
        sortDetailsAndLinks(answer)

        viewState.makeProgressBarInvisible()
        viewState.makeTabLayoutVisible()

        viewState.setupTabs(stringDetails, itemDetails)
    }

    private fun onLoadingFailed() {
        viewState.showError()
        viewState.makeProgressBarInvisible()
    }

    private fun sortDetailsAndLinks(answer: Any) {
        stringDetails = arrayListOf()
        itemDetails = arrayListOf()

        when (itemCategory) {
            categories[0] -> for (p in Film::class.memberProperties) {
                val detail = p.get(answer as Film)
                findLink(detail)
            }
            categories[1] -> for (p in Character::class.memberProperties) {
                val detail = p.get(answer as Character)
                findLink(detail)
            }
            categories[2] -> for (p in Planet::class.memberProperties) {
                val detail = p.get(answer as Planet)
                findLink(detail)
            }
            categories[3] -> for (p in Specie::class.memberProperties) {
                val detail = p.get(answer as Specie)
                findLink(detail)
            }
            categories[4] -> for (p in Starship::class.memberProperties) {
                val detail = p.get(answer as Starship)
                findLink(detail)
            }
            categories[5] -> for (p in Vehicle::class.memberProperties) {
                val detail = p.get(answer as Vehicle)
                findLink(detail)
            }
        }
    }

    private fun findLink(detail: Any?) {
        val detailAsString = detail.toString()

        if (!detailAsString.contains(SwApi.BASE_URL) && detailAsString != "[]")
            stringDetails.add(detailAsString)
        else
            turnLinksToItems(detailAsString)
    }

    private fun turnLinksToItems(linksAsString: String){
        val convertedLinks = arrayListOf<ItemBaseDetails>()

        if (linksAsString != "[]") {
            val linksArray = linksAsString
                    .replace("[", "")
                    .replace("]", "")
                    .split(", ")

            var category: String
            var id: String

            for (link in linksArray) {
                category = Utils.categoryFromUrl(link)
                id = Utils.idFromUrl(link)

                val observable = swService.itemBaseDetails(category, id)
                val subscription = observable.subscribe({ answer -> convertedLinks.add(answer) })
                unSubscribeOnDestroy(subscription)
            }
        }

        itemDetails.add(convertedLinks)
    }
}
