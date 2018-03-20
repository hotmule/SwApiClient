package com.example.thr.starwarsencyclopedia.mvp.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.*

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainView : MvpView {
    @StateStrategyType(SkipStrategy::class)
    fun openCategory(selectedCategory: String)

    fun setupActivity(selectedCategory: String)

    fun showSearchResults(category: String, query: String)
}