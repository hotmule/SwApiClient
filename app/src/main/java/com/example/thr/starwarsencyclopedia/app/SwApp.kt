package com.example.thr.starwarsencyclopedia.app

import android.app.Application
import com.example.thr.starwarsencyclopedia.di.*
import com.example.thr.starwarsencyclopedia.di.modules.HistoryDaoModule
import io.realm.Realm


class SwApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        appComponent = DaggerAppComponent.builder()
                .historyDaoModule(HistoryDaoModule())
                .build()
    }
}