package ru.hotmule.swapiclient.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.hotmule.swapiclient.R
import ru.hotmule.swapiclient.mvp.presenters.InfoPresenter
import ru.hotmule.swapiclient.mvp.views.InfoView
import kotlinx.android.synthetic.main.fragment_info.*


class InfoFragment : MvpAppCompatFragment(), InfoView {

    @InjectPresenter
    lateinit var infoPresenter: InfoPresenter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun setInfo(category: String, stringDetails: ArrayList<String>) {
        val itemDetailNamesArrayId = resources.getIdentifier(
                category + "_detail_names",
                "array",
                context?.packageName)

        val itemDetailsNames = resources.getStringArray(itemDetailNamesArrayId)
        val itemDetailsHashMap = arrayListOf<HashMap<String, String>>()

        val DETAIL_NAME_ARG = "detailName"
        val DETAIL_ARG = "detail"

        var map: HashMap<String, String>
        for ((i) in stringDetails.withIndex()) {
            map = HashMap()
            map[DETAIL_NAME_ARG] = itemDetailsNames[i]
            map[DETAIL_ARG] = stringDetails[i]
            itemDetailsHashMap.add(map)
        }

        val adapter = SimpleAdapter(context,
                itemDetailsHashMap,
                R.layout.simple_list_item_custom,
                arrayOf(DETAIL_NAME_ARG, DETAIL_ARG),
                intArrayOf(android.R.id.text1, android.R.id.text2))
        infoListView.adapter = adapter
    }
}