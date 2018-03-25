package com.example.thr.starwarsencyclopedia.mvp.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.thr.starwarsencyclopedia.mvp.models.gson.ItemBaseDetails

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface CardsView : MvpView {
    @StateStrategyType(SkipStrategy::class)
    fun openSelectedItem(name: String, category: String, id: String)

    fun setCards(data: ArrayList<ItemBaseDetails>)

    fun setMoreCards(data: ArrayList<ItemBaseDetails>)

    fun deleteCards()

    fun showMessage(message: String)

    fun showTextMessage(message: String)

    fun hideTextMessage()

    fun showProgress()

    fun hideProgress()

    fun clearRecyclerView()
}