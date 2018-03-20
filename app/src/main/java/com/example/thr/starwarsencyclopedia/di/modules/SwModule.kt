package com.example.thr.starwarsencyclopedia.di.modules

import com.example.thr.starwarsencyclopedia.app.SwApi
import com.example.thr.starwarsencyclopedia.mvp.SwService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ApiModule::class])
class SwModule {
    @Provides
    @Singleton
    fun provideSwService(swApi: SwApi): SwService = SwService(swApi)
}