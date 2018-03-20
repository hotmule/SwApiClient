package com.example.thr.starwarsencyclopedia.mvp.presenters

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView

import rx.Subscription
import rx.subscriptions.CompositeSubscription

open class BasePresenter<View : MvpView> : MvpPresenter<View>() {
    private val compositeSubscription = CompositeSubscription()

    protected fun unSubscribeOnDestroy(subscription: Subscription) {
        compositeSubscription.add(subscription)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeSubscription.clear()
    }
}