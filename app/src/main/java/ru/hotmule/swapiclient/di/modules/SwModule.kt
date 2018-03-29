package ru.hotmule.swapiclient.di.modules

import ru.hotmule.swapiclient.app.SwApi
import ru.hotmule.swapiclient.mvp.global.SwService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ApiModule::class])
class SwModule {
    @Provides
    @Singleton
    fun provideSwService(swApi: SwApi): SwService = SwService(swApi)
}