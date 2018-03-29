package ru.hotmule.swapiclient.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import ru.hotmule.swapiclient.app.SwApi
import ru.hotmule.swapiclient.app.SwApp
import ru.hotmule.swapiclient.common.Utils
import ru.hotmule.swapiclient.mvp.global.SwService
import ru.hotmule.swapiclient.mvp.global.BasePresenter
import ru.hotmule.swapiclient.mvp.models.HistoryDao
import ru.hotmule.swapiclient.mvp.models.gson.ItemBaseDetails
import ru.hotmule.swapiclient.mvp.views.CardsView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import ru.hotmule.swapiclient.mvp.models.bus.ClearHistoryEvent
import ru.hotmule.swapiclient.mvp.models.bus.OpenCategoryEvent
import ru.hotmule.swapiclient.mvp.models.bus.SearchEvent
import ru.hotmule.swapiclient.mvp.models.bus.SetLinksTabEvent
import javax.inject.Inject
import kotlin.collections.ArrayList


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
    fun onCategorySelected(event: OpenCategoryEvent) {
        viewState.clearRecyclerView()
        viewState.hideTextMessage()

        this.categoryName = event.category.toLowerCase()
        if (categoryName != "history") loadData(1) else loadHistory()
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
        else
            viewState.setCards(history)
    }

    @Subscribe
    fun onClearHistoryButtonClicked(event: ClearHistoryEvent) {
        historyDao.deleteAllData()
        viewState.deleteCards()
        showEmptyHistoryMessage()
    }

    private fun showEmptyHistoryMessage() {
        viewState.showTextMessage("History is empty")
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

    @Subscribe
    fun onSearchQueryEntered(event: SearchEvent) {
        viewState.showProgress()
        isSearchPage = true

        val searchQuery = event.query
        this.categoryName = event.category.toLowerCase()

        if (searchQuery.isNotEmpty()) {
            if (categoryName != "history") {
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

    private fun onLoadingSearchSuccess(itemBaseDetails: ArrayList<ItemBaseDetails>) {
        viewState.setCards(itemBaseDetails)
        viewState.hideProgress()
    }

    private var viewPagerPosition: Int? = null

    fun onViewPagerPositionReceived(viewPagerPosition: Int) {
        this.viewPagerPosition = viewPagerPosition

        val stickyEvent = setLinksTabStickyEvent()

        if (stickyEvent != null) {
            onItemLinksDetailsLoaded(stickyEvent)

            if (stickyEvent.itemLinkDetails.size == viewPagerPosition)
                EventBus.getDefault().removeStickyEvent(stickyEvent)
        }
    }

    private fun setLinksTabStickyEvent() = EventBus.getDefault().getStickyEvent(SetLinksTabEvent::class.java)

    @Subscribe(sticky = true)
    fun onItemLinksDetailsLoaded(event: SetLinksTabEvent) {
        if (viewPagerPosition != null && viewPagerPosition != 0) {
            pageIsLast = true

            val itemLinksDetails = event.itemLinkDetails

            if (itemLinksDetails != null)
                turnLinksToItems(itemLinksDetails[viewPagerPosition!! - 1])
        }
    }

    lateinit var convertedLinks: ArrayList<ItemBaseDetails>

    private fun turnLinksToItems(linksAsString: String) {
        convertedLinks = arrayListOf()

        if (linksAsString != "[]") {
            viewState.showProgress()

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
                val subscription = observable
                        .compose(Utils.applySchedulers())
                        .subscribe({ answer -> onConvertingLinkSuccess(answer, linksArray.size) },
                                {error -> onLoadingFailed()})
                unSubscribeOnDestroy(subscription)
            }
        }

        viewState.setCards(convertedLinks)
    }

    private fun onConvertingLinkSuccess(convertedLink: ItemBaseDetails, linksArraySize: Int) {
        convertedLinks.add(convertedLink)

        if (convertedLinks.size == linksArraySize) {
            viewState.setCards(convertedLinks)
            viewState.hideProgress()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}