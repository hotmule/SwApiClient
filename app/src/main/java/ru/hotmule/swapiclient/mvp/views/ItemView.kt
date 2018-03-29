package ru.hotmule.swapiclient.mvp.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface ItemView : MvpView {
    fun setupTabs(itemCategory: String)

    fun makeTabLayoutVisible()

    fun makeProgressBarVisible()

    fun makeProgressBarInvisible()

    @StateStrategyType(SkipStrategy::class)
    fun showError()

    fun setupActionBar(itemName: String)
}