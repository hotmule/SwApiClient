package com.example.thr.starwarsencyclopedia.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import com.example.thr.starwarsencyclopedia.R
import kotlinx.android.synthetic.main.fragment_item_info.*


class ItemInfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_item_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemCategory = arguments?.getString("category")

        val itemDetailNamesArrayId = resources.getIdentifier(
                itemCategory + "_detail_names",
                "array",
                context?.packageName)

        val itemDetails = arguments?.getStringArrayList("stringDetails")
        val itemDetailsNames = resources.getStringArray(itemDetailNamesArrayId)
        val itemDetailsHashMap = arrayListOf<HashMap<String, String>>()

        var map: HashMap<String, String>
        if (itemDetails != null) {
            for ((i) in itemDetails.withIndex()) {
                map = HashMap()
                map["Detail_name"] = itemDetailsNames[i]
                map["Detail"] = itemDetails[i]
                itemDetailsHashMap.add(map)
            }
        }

        val adapter = SimpleAdapter(context,
                itemDetailsHashMap,
                android.R.layout.simple_list_item_2,
                arrayOf("Detail_name", "Detail"),
                intArrayOf(android.R.id.text1, android.R.id.text2))
        infoListView.adapter = adapter
    }
}