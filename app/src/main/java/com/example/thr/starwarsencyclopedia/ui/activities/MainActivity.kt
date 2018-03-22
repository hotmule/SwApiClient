package com.example.thr.starwarsencyclopedia.ui.activities

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.thr.starwarsencyclopedia.R
import com.example.thr.starwarsencyclopedia.mvp.presenters.MainPresenter
import com.example.thr.starwarsencyclopedia.mvp.views.MainView
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.widget.SearchView
import com.example.thr.starwarsencyclopedia.ui.fragments.CardsFragment
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


class MainActivity :
        MvpAppCompatActivity(),
        MainView,
        NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val CATEGORY_ARG = "category"
    }

    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    private val SEARCH_QUERY_TAG = "searchQuery"
    private var savedStateSearchQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigationDrawer()

        if (savedInstanceState != null)
            savedStateSearchQuery = savedInstanceState.getString(SEARCH_QUERY_TAG)

    }

    private lateinit var drawerToggle: ActionBarDrawerToggle

    private fun setupNavigationDrawer() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawerToggle = ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close)

        drawerLayout.addDrawerListener(drawerToggle)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem) = drawerToggle.onOptionsItemSelected(item)

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    private lateinit var searchView: SearchView

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val menuItem = menu.findItem(R.id.searchView)
        setupSearchView(menuItem)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        savedStateSearchQuery = searchView.query.toString()
        outState?.putString(SEARCH_QUERY_TAG, savedStateSearchQuery)
    }

    private lateinit var searchCardsFragment: CardsFragment

    private fun setupSearchView(menuItem: MenuItem) {
        searchView = menuItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search_in_category)

        if (savedStateSearchQuery.isNotEmpty()) {
            menuItem.expandActionView()
            searchView.setQuery(savedStateSearchQuery, true)
        }

        searchCardsFragment = CardsFragment()

        searchView.setOnSearchClickListener {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.mainFrameLayout, searchCardsFragment)
                    .addToBackStack(null)
                    .commit()
        }

        RxSearchView.queryTextChanges(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { query -> mainPresenter.onSearchQueryChanges(query.toString()) }

        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean { return true }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                onBackPressed()
                return true
            }
        })
    }

    override fun showSearchResults(category: String, query: String) {
        if (query != savedStateSearchQuery && query.isNotEmpty())
            try {
                searchCardsFragment.searchItemsInCategory(category, query)
            } catch (e: UninitializedPropertyAccessException) {
                //fix up
            }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val selectedCategory = item.title.toString()
        mainPresenter.onCategorySelected(selectedCategory)
        return true
    }

    override fun openCategory(selectedCategory: String) {
        val historyCardsFragment = CardsFragment()
        val args = Bundle()
        args.putString(CATEGORY_ARG, selectedCategory)
        historyCardsFragment.arguments = args

        supportFragmentManager.beginTransaction()
                .replace(R.id.mainFrameLayout, historyCardsFragment)
                .commit()
    }

    override fun setupActivity(selectedCategory: String) {
        supportActionBar?.title = selectedCategory
        drawerLayout.closeDrawer(GravityCompat.START)
    }
}