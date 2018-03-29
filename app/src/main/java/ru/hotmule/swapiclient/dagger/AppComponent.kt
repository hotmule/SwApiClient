package ru.hotmule.swapiclient.dagger

import dagger.Component
import ru.hotmule.swapiclient.dagger.modules.HistoryDaoModule
import ru.hotmule.swapiclient.dagger.modules.SwModule
import ru.hotmule.swapiclient.mvp.presenters.CardsPresenter
import ru.hotmule.swapiclient.mvp.presenters.ItemPresenter
import javax.inject.Singleton

@Singleton
@Component(modules = [SwModule::class, HistoryDaoModule::class])
interface AppComponent {

    fun inject(itemPresenter: ItemPresenter)
    fun inject(cardsPresenter: CardsPresenter)
}