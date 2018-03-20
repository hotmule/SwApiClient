package com.example.thr.starwarsencyclopedia.ui.fragments

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
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
import com.example.thr.starwarsencyclopedia.ui.activities.MainActivity
import com.example.thr.starwarsencyclopedia.ui.adapters.CardsAdapter
import kotlinx.android.synthetic.main.fragment_cards.*


class CardsFragment :
        MvpAppCompatFragment(),
        CardsView,
        CardsAdapter.OnBottomReachedListener,
        CardsAdapter.OnItemClickListener {

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

        val category = arguments?.getString(MainActivity.CATEGORY_ARG)
        val itemDetails = arguments?.getParcelableArrayList<ItemBaseDetails>("itemDetails")

        if (category != null)
            cardsPresenter.onSelectedCategoryReceived(category)

        if (itemDetails != null)
            cardsPresenter.onItemDetailsReceived(itemDetails)

        arguments?.clear()

        floatingActionButton.setOnClickListener {
            cardsPresenter.onClearHistoryButtonPressed()
        }
    }

    fun searchItemsInCategory(category: String, searchQuery: String) {
        cardsPresenter.onSearchQueryReceived(category, searchQuery)
    }

    override fun setCards(data: ArrayList<ItemBaseDetails>) {
        cardsAdapter.setData(data)
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

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private var alertDialog: AlertDialog? = null

    override fun showConfirmDialog() {
        alertDialog = AlertDialog.Builder(context!!, R.style.AlertDialogStyle)
                .setMessage(getString(R.string.delete_browsing_history))
                .setPositiveButton(getString(R.string.yes),
                        { _, _ -> cardsPresenter.onClearHistoryConfirmed() })
                .setNeutralButton(getString(R.string.cancel),
                        { _, _ -> cardsPresenter.onAlertDialogDismiss() })
                .setOnDismissListener { cardsPresenter.onAlertDialogDismiss() }
                .show()
    }

    override fun onPause() {
        super.onPause()
        cardsPresenter.onAlertDialogDismiss()
    }

    override fun hideConfirmDialog() {
        alertDialog?.dismiss()
    }

    override fun showClearHistoryButton() {
        floatingActionButton.visibility = View.VISIBLE
    }

    override fun hideClearHistoryButton() {
        floatingActionButton.visibility = View.GONE
    }
}