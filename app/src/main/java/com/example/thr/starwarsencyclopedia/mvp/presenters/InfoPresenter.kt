package com.example.thr.starwarsencyclopedia.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.thr.starwarsencyclopedia.mvp.models.bus.SetInfoTabEvent
import com.example.thr.starwarsencyclopedia.mvp.views.InfoView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@InjectViewState
class InfoPresenter : MvpPresenter<InfoView>() {

    init {
        EventBus.getDefault().register(this)
    }

    @Subscribe
    fun onInfoTabDataReceived(event: SetInfoTabEvent) {
        viewState.setInfo(event.category, event.stringDetails)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
