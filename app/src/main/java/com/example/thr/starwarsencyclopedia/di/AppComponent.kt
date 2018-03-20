package com.example.thr.starwarsencyclopedia.di

import com.example.thr.starwarsencyclopedia.di.modules.HistoryDaoModule
import com.example.thr.starwarsencyclopedia.di.modules.SwModule
import com.example.thr.starwarsencyclopedia.mvp.presenters.CardsPresenter
import com.example.thr.starwarsencyclopedia.mvp.presenters.ItemPresenter
import com.example.thr.starwarsencyclopedia.mvp.presenters.MainPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [SwModule::class, HistoryDaoModule::class])
interface AppComponent {

    fun inject(itemPresenter: ItemPresenter)
    fun inject(cardsPresenter: CardsPresenter)
}