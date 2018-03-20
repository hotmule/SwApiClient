package com.example.thr.starwarsencyclopedia.ui.activities

import android.content.Intent
import android.support.design.widget.TabLayout

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentActivity
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.thr.starwarsencyclopedia.R
import com.example.thr.starwarsencyclopedia.app.SwApi
import com.example.thr.starwarsencyclopedia.mvp.models.gson.ItemBaseDetails
import com.example.thr.starwarsencyclopedia.mvp.presenters.ItemPresenter
import com.example.thr.starwarsencyclopedia.mvp.views.ItemView
import com.example.thr.starwarsencyclopedia.ui.adapters.SectionsPagerAdapter
import com.example.thr.starwarsencyclopedia.ui.fragments.CardsFragment
import com.example.thr.starwarsencyclopedia.ui.fragments.ItemInfoFragment

import kotlinx.android.synthetic.main.activity_item.*
import kotlin.collections.ArrayList


class ItemActivity : MvpAppCompatActivity(), ItemView {

    @InjectPresenter
    lateinit var itemPresenter: ItemPresenter

    companion object {
        fun buildIntent(fragmentActivity: FragmentActivity?, name: String, category: String, id: String): Intent {
            val intent = Intent(fragmentActivity, ItemActivity::class.java)
            intent.putExtra(SwApi.NAME_ARG, name)
            intent.putExtra(SwApi.CATEGORY_ARG, category)
            intent.putExtra(SwApi.ID_ARG, id)
            return intent
        }
    }

    private var sectionsPagerAdapter: SectionsPagerAdapter? = null
    private lateinit var category: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(SwApi.NAME_ARG)

        category = intent.getStringExtra(SwApi.CATEGORY_ARG)
        val id = intent.getStringExtra(SwApi.ID_ARG)
        val categoriesNames = resources.getStringArray(R.array.categories_names)

        itemPresenter.requestItemDetails(category, id, categoriesNames)

        sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
    }

    override fun setupTabs(stringDetails: ArrayList<String>, itemDetails : ArrayList<ArrayList<ItemBaseDetails>>) {
        setupInfoTab(stringDetails)
        setupLinkTabs(itemDetails)
        viewPager.adapter = sectionsPagerAdapter
    }

    private fun setupInfoTab(stringDetails: ArrayList<String>){
        val itemInfoFragment = ItemInfoFragment()

        val args = Bundle()
        args.putStringArrayList("stringDetails", stringDetails)
        args.putString("category", category)

        itemInfoFragment.arguments = args
        sectionsPagerAdapter?.addFragment(itemInfoFragment, "Info")
    }

    private fun setupLinkTabs(itemDetails : ArrayList<ArrayList<ItemBaseDetails>>){
        var itemCardsFragment: CardsFragment

        val itemTabsNamesArrayId = resources.getIdentifier(
                category + "_tabs_names",
                "array",
                baseContext.packageName)
        val itemTabsNames = resources.getStringArray(itemTabsNamesArrayId)

        for ((i, tabName) in itemTabsNames.withIndex()) {
            itemCardsFragment = CardsFragment()
            val args = Bundle()
            args.putParcelableArrayList("itemDetails", itemDetails[i])
            itemCardsFragment.arguments = args

            sectionsPagerAdapter?.addFragment(itemCardsFragment, tabName)
        }
    }

    override fun showError() {
        val snackbar = Snackbar.make(main_content, "No connection to the server", Snackbar.LENGTH_LONG)
        snackbar.setAction("Ok", { snackbar.dismiss() })
        snackbar.show()
    }

    override fun makeTabLayoutVisible() {
        tabLayout.visibility = View.VISIBLE
    }

    override fun makeProgressBarVisible() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun makeProgressBarInvisible() {
        progress_bar.visibility = View.GONE
    }
}
