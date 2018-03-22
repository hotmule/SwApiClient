package com.example.thr.starwarsencyclopedia.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentActivity
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.thr.starwarsencyclopedia.R
import com.example.thr.starwarsencyclopedia.mvp.models.gson.ItemBaseDetails
import com.example.thr.starwarsencyclopedia.mvp.presenters.ItemPresenter
import com.example.thr.starwarsencyclopedia.mvp.views.ItemView
import com.example.thr.starwarsencyclopedia.ui.adapters.SectionsPagerAdapter
import com.example.thr.starwarsencyclopedia.ui.fragments.CardsFragment
import com.example.thr.starwarsencyclopedia.ui.fragments.InfoFragment
import kotlinx.android.synthetic.main.activity_item.*
import kotlin.collections.ArrayList
import android.view.MenuItem


class ItemActivity : MvpAppCompatActivity(), ItemView {

    @InjectPresenter
    lateinit var itemPresenter: ItemPresenter

    companion object {
        var NAME_ARG = "name"
        val CATEGORY_ARG = "category"
        val ID_ARG = "id"
        val STRING_DETAILS_ARG = "stringDetails"
        val LINK_DETAILS_ARG = "linkDetails"

        fun buildIntent(fragmentActivity: FragmentActivity?, name: String, category: String, id: String): Intent {
            val intent = Intent(fragmentActivity, ItemActivity::class.java)
            intent.putExtra(NAME_ARG, name)
            intent.putExtra(CATEGORY_ARG, category)
            intent.putExtra(ID_ARG, id)
            return intent
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isRotated", true)
    }

    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        if (savedInstanceState == null) {
            val category = intent.getStringExtra(CATEGORY_ARG)
            val name = intent.getStringExtra(NAME_ARG)
            val id = intent.getStringExtra(ID_ARG)

            val categoriesNames = resources.getStringArray(R.array.categories_names)
            itemPresenter.onItemSelected(name, category, id, categoriesNames)
        }
    }

    override fun setupActionBar(itemName: String) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = itemName
    }

    override fun setupTabs(itemCategory: String,
                           stringDetails: ArrayList<String>,
                           itemDetails : ArrayList<ArrayList<ItemBaseDetails>>) {

        sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        setupInfoTab(itemCategory, stringDetails)
        setupLinkTabs(itemCategory, itemDetails)

        viewPager.adapter = sectionsPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setupInfoTab(itemCategory: String, stringDetails: ArrayList<String>){
        val args = Bundle()
        args.putStringArrayList(STRING_DETAILS_ARG, stringDetails)
        args.putString(CATEGORY_ARG, itemCategory)

        val itemInfoFragment = InfoFragment()
        itemInfoFragment.arguments = args

        sectionsPagerAdapter.addFragment(itemInfoFragment, "Info")
    }

    private fun setupLinkTabs(itemCategory: String, itemDetails : ArrayList<ArrayList<ItemBaseDetails>>){
        var itemCardsFragment: CardsFragment

        val arrayId = resources.getIdentifier(itemCategory + "_tabs_names", "array", baseContext.packageName)
        val itemTabsNames = resources.getStringArray(arrayId)

        for ((i, tabName) in itemTabsNames.withIndex()) {
            val args = Bundle()
            args.putParcelableArrayList(LINK_DETAILS_ARG, itemDetails[i])

            itemCardsFragment = CardsFragment()
            itemCardsFragment.arguments = args

            sectionsPagerAdapter.addFragment(itemCardsFragment, tabName)
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}
