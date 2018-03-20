package com.example.thr.starwarsencyclopedia.ui.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter


class SectionsPagerAdapter(fragment: FragmentManager) : FragmentPagerAdapter(fragment) {

    private var fragmentsList: MutableList<Fragment> = mutableListOf()
    private var fragmentsTitlesList: MutableList<String> = mutableListOf()

    fun addFragment(fragment: Fragment, title: String) {
        fragmentsList.add(fragment)
        fragmentsTitlesList.add(title)
    }

    override fun getPageTitle(position: Int) = fragmentsTitlesList[position]

    override fun getItem(position: Int): Fragment = fragmentsList[position]

    override fun getCount() = fragmentsList.size
}