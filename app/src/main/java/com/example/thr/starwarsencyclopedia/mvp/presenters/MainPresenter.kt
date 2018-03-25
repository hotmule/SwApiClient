package com.example.thr.starwarsencyclopedia.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.example.thr.starwarsencyclopedia.mvp.models.bus.ClearHistoryEvent
import com.example.thr.starwarsencyclopedia.mvp.views.MainView
import com.example.thr.starwarsencyclopedia.mvp.global.BasePresenter
import com.example.thr.starwarsencyclopedia.mvp.models.bus.OpenCategoryEvent
import com.example.thr.starwarsencyclopedia.mvp.models.bus.SearchEvent
import org.greenrobot.eventbus.EventBus


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