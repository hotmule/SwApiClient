package com.example.thr.starwarsencyclopedia.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import com.example.thr.starwarsencyclopedia.R
import com.example.thr.starwarsencyclopedia.ui.activities.ItemActivity
import kotlinx.android.synthetic.main.fragment_info.*


class InfoFragment : Fragment() {

    companion object {
        val DETAIL_NAME_ARG = "detailName"
        val DETAIL_ARG = "detail"
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemCategory = arguments?.getString(ItemActivity.CATEGORY_ARG)

        val itemDetailNamesArrayId = resources.getIdentifier(
                itemCategory + "_detail_names",
                "array",
                context?.packageName)

        val itemDetails = arguments?.getStringArrayList(ItemActivity.STRING_DETAILS_ARG)
        val itemDetailsNames = resources.getStringArray(itemDetailNamesArrayId)
        val itemDetailsHashMap = arrayListOf<HashMap<String, String>>()

        var map: HashMap<String, String>
        if (itemDetails != null) {
            for ((i) in itemDetails.withIndex()) {
                map = HashMap()
                map[DETAIL_NAME_ARG] = itemDetailsNames[i]
                map[DETAIL_ARG] = itemDetails[i]
                itemDetailsHashMap.add(map)
            }
        }

        val adapter = SimpleAdapter(context,
                itemDetailsHashMap,
                R.layout.simple_list_item_custom,
                arrayOf(DETAIL_NAME_ARG, DETAIL_ARG),
                intArrayOf(android.R.id.text1, android.R.id.text2))
        infoListView.adapter = adapter
    }
}