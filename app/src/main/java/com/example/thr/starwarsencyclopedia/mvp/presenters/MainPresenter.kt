package com.example.thr.starwarsencyclopedia.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.example.thr.starwarsencyclopedia.mvp.views.MainView
import com.example.thr.starwarsencyclopedia.mvp.SwService
import javax.inject.Inject;
import com.example.thr.starwarsencyclopedia.app.SwApp
import com.example.thr.starwarsencyclopedia.common.Utils
import com.example.thr.starwarsencyclopedia.app.SwApi
import com.example.thr.starwarsencyclopedia.mvp.models.HistoryDao
import com.example.thr.starwarsencyclopedia.mvp.models.gson.ItemBaseDetails


@InjectViewState
class MainPresenter : BasePresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        onCategorySelected("Films")
    }

    lateinit var category: String

    fun onCategorySelected(selectedCategory: String) {
        this.category = selectedCategory
        viewState.setupActivity(selectedCategory)
        viewState.openCategory(selectedCategory)
    }

    fun onSearchQueryChanges(query: String) {
        viewState.showSearchResults(category, query)
    }
}