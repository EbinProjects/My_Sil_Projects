package com.example.jskotlin.adapter


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import androidx.fragment.app.FragmentPagerAdapter
import com.example.jskotlin.fragments.ProfileFragment
import com.example.jskotlin.fragments.TabSecondFragment
import com.example.jskotlin.fragments.TabThirdFragment


class MyAdapter (private val myContext: Context, fm: FragmentManager,
                 internal var totalTabs: Int) : FragmentPagerAdapter(fm){
    override fun getCount(): Int {
    return totalTabs
    }

    override fun getItem(p0: Int): Fragment {
        when (p0) {
            0 -> {
                return ProfileFragment()
            }
            1 -> {
                return TabSecondFragment()
            }
            2 -> {

                return TabThirdFragment()
            }
            else -> {
                return ProfileFragment()
            }
        }
    }
}
