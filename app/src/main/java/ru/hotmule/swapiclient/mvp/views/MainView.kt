package ru.hotmule.swapiclient.mvp.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.*

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainView : MvpView {
    fun setupActivity(selectedCategory: String)

    fun showClearHistoryButton()

    fun hideClearHistoryButton()

    fun showConfirmDialog()

    fun hideConfirmDialog()
}