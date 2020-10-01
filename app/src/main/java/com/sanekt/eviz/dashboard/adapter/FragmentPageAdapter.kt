package com.sanekt.eviz.dashboard.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class FragmentPageAdapter : FragmentStatePagerAdapter {

    var fragments: MutableList<Fragment>? = null
    var titles: MutableList<String>? = null

    constructor(fm: FragmentManager, behavior: Int): super(fm, behavior) {
        fragments = ArrayList()
        titles = ArrayList()
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragments!!.add(fragment)
        titles!!.add(title)
    }

    override fun getCount(): Int {
        return fragments!!.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments!![position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles!![position]
    }
}
