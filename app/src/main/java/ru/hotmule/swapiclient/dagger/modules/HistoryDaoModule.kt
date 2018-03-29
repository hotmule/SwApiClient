package ru.hotmule.swapiclient.dagger.modules

import ru.hotmule.swapiclient.mvp.models.HistoryDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HistoryDaoModule {
    @Provides
    @Singleton
    fun provideHistoryDao(): HistoryDao = HistoryDao()
}
