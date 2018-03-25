package com.example.thr.starwarsencyclopedia.ui.activities

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.thr.starwarsencyclopedia.R
import com.example.thr.starwarsencyclopedia.mvp.presenters.MainPresenter
import com.example.thr.starwarsencyclopedia.mvp.views.MainView
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.widget.SearchView
import android.view.View
import com.example.thr.starwarsencyclopedia.ui.fragments.CardsFragment
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


class MainActivity :
        MvpAppCompatActivity(),
        MainView,
        NavigationView.OnNavigationItemSelectedListener {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private val SEARCH_QUERY_TAG = "searchQuery"
    private var savedStateSearchQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigationDrawer()

        if (savedInstanceState != null)
            savedStateSearchQuery = savedInstanceState.getString(SEARCH_QUERY_TAG)

        mainFloatingActionButton.setOnClickListener {
            presenter.onClearHistoryButtonClicked()
        }

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

        RxSearchView.queryTextChanges(searchView)
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { query -> presenter.onSearchQueryChanges(query.toString(), savedStateSearchQuery) }

        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean { return true }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                presenter.onCategorySelected()
                return true
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val selectedCategory = item.title.toString()
        presenter.saveSelectedCategory(selectedCategory)
        presenter.onCategorySelected()
        return true
    }

    override fun setupActivity(selectedCategory: String) {
        supportActionBar?.title = selectedCategory
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun showClearHistoryButton() {
        mainFloatingActionButton .visibility = View.VISIBLE
    }

    override fun hideClearHistoryButton() {
        mainFloatingActionButton.visibility = View.GONE
    }

    private var alertDialog: AlertDialog? = null

    override fun showConfirmDialog() {
        alertDialog = AlertDialog.Builder(this, R.style.AlertDialogStyle)
                .setMessage(getString(R.string.delete_browsing_history))
                .setPositiveButton(getString(R.string.yes),
                        { _, _ -> presenter.onClearHistoryConfirmed() })
                .setNeutralButton(getString(R.string.cancel),
                        { _, _ -> presenter.onAlertDialogDismiss() })
                .setOnDismissListener { presenter.onAlertDialogDismiss() }
                .show()
    }

    override fun hideConfirmDialog() {
        alertDialog?.dismiss()
    }

    override fun onPause() {
        super.onPause()
        presenter.onAlertDialogDismiss()
    }
}