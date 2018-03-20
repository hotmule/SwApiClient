package com.example.thr.starwarsencyclopedia.di.modules

import com.example.thr.starwarsencyclopedia.app.SwApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [RetrofitModule::class])
class ApiModule {
    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): SwApi = retrofit.create(SwApi::class.java)
}