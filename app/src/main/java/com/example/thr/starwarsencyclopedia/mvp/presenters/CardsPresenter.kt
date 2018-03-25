package com.example.thr.starwarsencyclopedia.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.example.thr.starwarsencyclopedia.app.SwApi
import com.example.thr.starwarsencyclopedia.app.SwApp
import com.example.thr.starwarsencyclopedia.mvp.models.bus.ClearHistoryEvent
import com.example.thr.starwarsencyclopedia.common.Utils
import com.example.thr.starwarsencyclopedia.mvp.global.SwService
import com.example.thr.starwarsencyclopedia.mvp.global.BasePresenter
import com.example.thr.starwarsencyclopedia.mvp.models.HistoryDao
import com.example.thr.starwarsencyclopedia.mvp.models.bus.OpenCategoryEvent
import com.example.thr.starwarsencyclopedia.mvp.models.bus.SearchEvent
import com.example.thr.starwarsencyclopedia.mvp.models.gson.ItemBaseDetails
import com.example.thr.starwarsencyclopedia.mvp.views.CardsView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*
import javax.inject.Inject


@InjectViewState
class CardsPresenter : BasePresenter<CardsView>() {

    init {
        SwApp.appComponent.inject(this)
        EventBus.getDefault().register(this)
    }

    @Inject
    lateinit var swService: SwService

    @Inject
    lateinit var historyDao: HistoryDao

    private lateinit var categoryName: String
    private var pageIsLast = false
    private var isSearchPage = false

    @Subscribe
    fun openSelectedCategory(event: OpenCategoryEvent) {
        viewState.clearRecyclerView()
        viewState.hideTextMessage()

        this.categoryName = event.category.toLowerCase()

        if (categoryName != "history") loadData(1)
        else loadHistory()
    }

    private fun loadData(pageNumber: Int) {
        viewState.showProgress()

        pageIsLast = false
        isSearchPage = false

        val observable = swService.categoryResult(categoryName, pageNumber)
        val subscription = observable
                .compose(Utils.applySchedulers())
                .subscribe(
                        { answer -> onLoadingSuccess(answer.previous, answer.next, answer.results) },
                        { error -> onLoadingFailed() })
        unSubscribeOnDestroy(subscription)
    }

    private fun onLoadingSuccess(previousPage: String, nextPage: String, itemBaseDetails: ArrayList<ItemBaseDetails>) {
        viewState.hideProgress()

        if (previousPage == null)
            viewState.setCards(itemBaseDetails)
        else
            viewState.setMoreCards(itemBaseDetails)

        if (nextPage == null)
            pageIsLast = true
    }

    private fun onLoadingFailed() {
        viewState.hideProgress()
        viewState.showMessage("No connection to the server")
    }

    private fun loadHistory() {
        pageIsLast = true

        val history = historyDao.loadAllData()

        if (history.isEmpty())
            showEmptyHistoryMessage()
        else {
            viewState.setCards(history)
        }
    }

    fun onItemClick(item: ItemBaseDetails) {
        historyDao.addData(item)

        viewState.openSelectedItem(
                item.name,
                Utils.categoryFromUrl(item.url),
                Utils.idFromUrl(item.url))
    }

    fun onBottomReached(currentCount: Int) {
        if (!pageIsLast && !isSearchPage) {
            val pageNumber = currentCount / SwApi.PAGE_SIZE + 1
            loadData(pageNumber)
        }
    }

    private fun onLoadingSearchSuccess(itemBaseDetails: ArrayList<ItemBaseDetails>){
        viewState.setCards(itemBaseDetails)
        viewState.hideProgress()
    }

    fun onItemDetailsReceived(itemDetails: ArrayList<ItemBaseDetails>) {
        pageIsLast = true
        viewState.setCards(itemDetails)
    }

    @Subscribe
    fun onHistoryClear(event: ClearHistoryEvent) {
        historyDao.deleteAllData()
        viewState.deleteCards()
        showEmptyHistoryMessage()
    }

    @Subscribe
    fun onSearchQueryReceived(event: SearchEvent) {
        val searchQuery = event.query
        val category = event.category

        viewState.showProgress()
        isSearchPage = true
        this.categoryName = category.toLowerCase()

        if (searchQuery.isNotEmpty()) {
            if (category != "History") {
                val observable = swService.searchInCategory(categoryName, searchQuery)

                val subscription = observable
                        .compose(Utils.applySchedulers())
                        .subscribe(
                                { answer -> onLoadingSearchSuccess(answer.results) },
                                { error -> onLoadingFailed() })
                unSubscribeOnDestroy(subscription)
            } else
                onLoadingSearchSuccess(historyDao.searchDataByName(searchQuery))
        }
    }

    private fun showEmptyHistoryMessage(){
        viewState.showTextMessage("History is empty")
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}