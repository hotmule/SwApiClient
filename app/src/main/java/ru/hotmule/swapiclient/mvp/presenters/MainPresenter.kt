package ru.hotmule.swapiclient.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import ru.hotmule.swapiclient.mvp.views.MainView
import ru.hotmule.swapiclient.mvp.global.BasePresenter
import org.greenrobot.eventbus.EventBus
import ru.hotmule.swapiclient.mvp.models.bus.ClearHistoryEvent
import ru.hotmule.swapiclient.mvp.models.bus.OpenCategoryEvent
import ru.hotmule.swapiclient.mvp.models.bus.SearchEvent


@InjectViewState
class MainPresenter : BasePresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        saveSelectedCategory("Films")
        onCategorySelected()
    }

    lateinit var category: String

    fun saveSelectedCategory(selectedCategory: String) {
        this.category = selectedCategory
    }

    fun onCategorySelected() {
        viewState.setupActivity(category)

        if (category == "History")
            viewState.showClearHistoryButton()
        else
            viewState.hideClearHistoryButton()

        EventBus.getDefault().post(OpenCategoryEvent(category))
    }

    fun onSearchQueryChanges(query: String, savedQuery: String) {
        if (query != savedQuery && query.isNotEmpty())
            EventBus.getDefault().post(SearchEvent(category, query))
    }

    fun onClearHistoryButtonClicked() {
        viewState.showConfirmDialog()
    }

    fun onClearHistoryConfirmed() {
        EventBus.getDefault().post(ClearHistoryEvent())
    }

    fun onAlertDialogDismiss() {
        viewState.hideConfirmDialog()
    }
}