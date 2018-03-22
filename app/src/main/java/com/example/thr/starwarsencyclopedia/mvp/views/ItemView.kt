package com.example.thr.starwarsencyclopedia.mvp.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.thr.starwarsencyclopedia.mvp.models.gson.ItemBaseDetails

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface ItemView : MvpView {
    fun setupTabs(itemCategory: String,
                  stringDetails: ArrayList<String>,
                  itemDetails: ArrayList<ArrayList<ItemBaseDetails>>)

    fun makeTabLayoutVisible()

    fun makeProgressBarVisible()

    fun makeProgressBarInvisible()

    @StateStrategyType(SkipStrategy::class)
    fun showError()

    fun setupActionBar(itemName: String)
}