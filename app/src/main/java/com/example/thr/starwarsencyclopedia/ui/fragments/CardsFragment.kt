package com.example.thr.starwarsencyclopedia.ui.fragments

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.thr.starwarsencyclopedia.R
import com.example.thr.starwarsencyclopedia.mvp.models.gson.ItemBaseDetails
import com.example.thr.starwarsencyclopedia.mvp.presenters.CardsPresenter
import com.example.thr.starwarsencyclopedia.mvp.views.CardsView
import com.example.thr.starwarsencyclopedia.ui.activities.ItemActivity
import com.example.thr.starwarsencyclopedia.ui.adapters.CardsAdapter
import kotlinx.android.synthetic.main.fragment_cards.*


class CardsFragment :
        MvpAppCompatFragment(),
        CardsView,
        CardsAdapter.OnBottomReachedListener,
        CardsAdapter.OnItemClickListener {

    override fun clearRecyclerView() {
        cardsAdapter.clear()
    }

    @InjectPresenter
    lateinit var cardsPresenter: CardsPresenter

    private lateinit var cardsAdapter: CardsAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_cards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardsAdapter = CardsAdapter(this, this)
        recyclerView.adapter = cardsAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val viewPagerPosition = arguments?.getInt(ItemActivity.VIEW_PAGER_POSITION_ARG)
        if (viewPagerPosition != null)
            cardsPresenter.onViewPagerPositionReceived(viewPagerPosition)

        arguments?.clear()
    }

    override fun setCards(data: ArrayList<ItemBaseDetails>) {
        if (data.size != 0)
            cardsAdapter.setData(data)
        else
            showTextMessage("No data")
    }

    override fun setMoreCards(data: ArrayList<ItemBaseDetails>) {
        cardsAdapter.addMoreData(data)
    }

    override fun onBottomReached() {
        cardsPresenter.onBottomReached(cardsAdapter.itemCount)
    }

    override fun onItemClick(item: ItemBaseDetails) {
        cardsPresenter.onItemClick(item)
    }

    override fun openSelectedItem(name: String, category: String, id: String) {
        startActivity(ItemActivity.buildIntent(activity, name, category, id))
    }

    override fun showMessage(message: String) {
        val snackbar = Snackbar.make(frameLayout, message, Snackbar.LENGTH_LONG)
        snackbar.setAction("Ok", { snackbar.dismiss() })
        snackbar.show()
    }

    override fun deleteCards() {
        cardsAdapter.clear()
    }

    override fun showTextMessage(message: String) {
        messageTextView.text = message
        messageTextView.visibility = View.VISIBLE
    }

    override fun hideTextMessage() {
        messageTextView.visibility = View.GONE
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }
}