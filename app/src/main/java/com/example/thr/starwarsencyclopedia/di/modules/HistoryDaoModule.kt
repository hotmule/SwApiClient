package com.example.thr.starwarsencyclopedia.di.modules

import com.example.thr.starwarsencyclopedia.mvp.models.HistoryDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HistoryDaoModule {
    @Provides
    @Singleton
    fun provideHistoryDao(): HistoryDao = HistoryDao()
}
