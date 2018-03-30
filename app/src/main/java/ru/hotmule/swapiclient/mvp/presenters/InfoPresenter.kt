package ru.hotmule.swapiclient.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.hotmule.swapiclient.mvp.views.InfoView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import ru.hotmule.swapiclient.bus.SetInfoTabEvent

@InjectViewState
class InfoPresenter : MvpPresenter<InfoView>() {

    init {
        EventBus.getDefault().register(this)
    }

    @Subscribe
    fun onInfoTabDataReceived(event: SetInfoTabEvent) {
        val capitalizedStringDetails = arrayListOf<String>()

        for (detail in event.stringDetails)
            capitalizedStringDetails.add(detail.capitalize())

        viewState.setInfo(event.category, capitalizedStringDetails)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
