package ru.hotmule.swapiclient.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import ru.hotmule.swapiclient.mvp.SwService
import javax.inject.Inject;
import ru.hotmule.swapiclient.app.SwApp
import ru.hotmule.swapiclient.app.SwApi
import ru.hotmule.swapiclient.mvp.views.ItemView
import org.greenrobot.eventbus.EventBus
import ru.hotmule.swapiclient.mvp.models.bus.SetInfoTabEvent
import ru.hotmule.swapiclient.mvp.models.bus.SetLinksTabEvent
import ru.hotmule.swapiclient.mvp.models.gson.categories.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
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
    private lateinit var linkDetails: ArrayList<String>

    fun onItemSelected(itemName: String, itemCategory: String, itemId: String, categories: Array<String>) {
        viewState.setupActionBar(itemName)
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { answer -> onLoadingSuccess(answer) },
                        { error -> onLoadingFailed() })
        unSubscribeOnDestroy(subscription)
    }

    private fun onLoadingSuccess(answer: Any) {
        sortDetailsAndLinks(answer)

        viewState.makeProgressBarInvisible()
        viewState.makeTabLayoutVisible()

        viewState.setupTabs(itemCategory)

        EventBus.getDefault().post(SetInfoTabEvent(itemCategory, stringDetails))
        EventBus.getDefault().postSticky(SetLinksTabEvent(linkDetails))
    }

    private fun onLoadingFailed() {
        viewState.showError()
        viewState.makeProgressBarInvisible()
    }

    private fun sortDetailsAndLinks(answer: Any) {
        stringDetails = arrayListOf()
        linkDetails = arrayListOf()

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
            linkDetails.add(detailAsString)
    }
}