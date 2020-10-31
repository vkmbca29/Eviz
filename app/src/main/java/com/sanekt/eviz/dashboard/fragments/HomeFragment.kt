package com.sanekt.eviz.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.sanekt.eviz.R
import com.sanekt.eviz.dashboard.adapter.FragmentPageAdapter


class HomeFragment : Fragment() {

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        tabLayout = view.findViewById(R.id.tab_layout)
        viewPager = view.findViewById(R.id.view_pager)

        val fragmentPageAdapter = activity?.supportFragmentManager?.let { FragmentPageAdapter(it, 0) }

        fragmentPageAdapter!!.addFragment(
            CardFragment(),
            "My Cards"
        )
        fragmentPageAdapter.addFragment(
            CardFragment(),
            "Received Ca.."
        )
        fragmentPageAdapter.addFragment(
            CardFragment(),
            "Templates"
        )
        fragmentPageAdapter.addFragment(
            CardFragment(),
            "Loyalty Card"
        )

        viewPager?.adapter = fragmentPageAdapter
        tabLayout!!.setupWithViewPager(viewPager)

//        for (i in 0 until tabLayout!!.tabCount) {
//            if(i > 0){
//            val tab = tabLayout!!.getTabAt(i)
//            val relativeLayout = LayoutInflater.from(activity)
//                .inflate(R.layout.tab_layout, tabLayout, false) as RelativeLayout
//            val tabTextView = relativeLayout.findViewById<View>(R.id.tab_title) as TextView
//            tabTextView.text = tab!!.text
//            tab!!.customView = relativeLayout
//            tab!!.select()
//            }
//
//        }

        return view
    }


}