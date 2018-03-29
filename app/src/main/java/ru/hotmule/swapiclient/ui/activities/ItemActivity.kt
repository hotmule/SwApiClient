package ru.hotmule.swapiclient.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentActivity
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import ru.hotmule.swapiclient.mvp.presenters.ItemPresenter
import ru.hotmule.swapiclient.mvp.views.ItemView
import ru.hotmule.swapiclient.ui.adapters.SectionsPagerAdapter
import ru.hotmule.swapiclient.ui.fragments.CardsFragment
import ru.hotmule.swapiclient.ui.fragments.InfoFragment
import kotlinx.android.synthetic.main.activity_item.*
import android.view.MenuItem
import com.hotmule.swapiclient.R


class ItemActivity : MvpAppCompatActivity(), ItemView {

    @InjectPresenter
    lateinit var itemPresenter: ItemPresenter

    companion object {
        var NAME_ARG = "name"
        val CATEGORY_ARG = "category"
        val ID_ARG = "id"
        val VIEW_PAGER_POSITION_ARG = "viewPagerPosition"

        fun buildIntent(fragmentActivity: FragmentActivity?, name: String, category: String, id: String): Intent {
            val intent = Intent(fragmentActivity, ItemActivity::class.java)
            intent.putExtra(NAME_ARG, name)
            intent.putExtra(CATEGORY_ARG, category)
            intent.putExtra(ID_ARG, id)
            return intent
        }
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

    override fun setupTabs(itemCategory: String) {

        sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        sectionsPagerAdapter.addFragment(InfoFragment(), "Info")

        val arrayId = resources.getIdentifier(itemCategory + "_tabs_names", "array", baseContext.packageName)
        val itemTabsNames = resources.getStringArray(arrayId)

        var itemCardsFragment: CardsFragment
        for ((i, tabName) in itemTabsNames.withIndex()) {
            itemCardsFragment = CardsFragment()
            val args = Bundle()
            args.putInt(VIEW_PAGER_POSITION_ARG, i + 1)
            itemCardsFragment.arguments = args
            sectionsPagerAdapter.addFragment(itemCardsFragment, tabName)
        }

        viewPager.adapter = sectionsPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
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