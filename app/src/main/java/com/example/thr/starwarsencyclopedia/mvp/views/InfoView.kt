package com.example.thr.starwarsencyclopedia.mvp.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface InfoView : MvpView {
    fun setInfo(category: String, stringDetails: ArrayList<String>)
}