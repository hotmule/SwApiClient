package ru.hotmule.swapiclient.app

import android.app.Application
import io.realm.Realm
import ru.hotmule.swapiclient.dagger.AppComponent
import ru.hotmule.swapiclient.dagger.DaggerAppComponent
import ru.hotmule.swapiclient.dagger.modules.HistoryDaoModule


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