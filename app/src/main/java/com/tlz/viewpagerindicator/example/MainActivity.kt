package com.tlz.viewpagerindicator.example

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vp_first.adapter = CustomViewPagerAdapter()
        vpi_first.setupWithViewPager(vp_first)

        vp_second.adapter = CustomViewPagerAdapter()
        vpi_second.setupWithViewPager(vp_second)

        vp_second2.adapter = CustomViewPagerAdapter()
        vpi_second2.setupWithViewPager(vp_second2)

        vp_third.adapter = CustomViewPagerAdapter()
        vpi_third.setupWithViewPager(vp_third)

        vp_fourth.adapter = CustomViewPagerAdapter()
        vpi_fourth.setupWithViewPager(vp_fourth)

        vp_fifth.adapter = CustomViewPagerAdapter()
        vpi_fifth.setupWithViewPager(vp_fifth)

        btn_change_adapter.setOnClickListener {
            vp_first.adapter = CustomViewPagerAdapter()
//            (vp_first.adapter as CustomViewPagerAdapter).apply {
//                itemCount = 10
//                notifyDataSetChanged()
//            }
            vp_second.adapter = CustomViewPagerAdapter()
//            (vp_second.adapter as CustomViewPagerAdapter).apply {
//                itemCount = 10
//                notifyDataSetChanged()
//            }
            vp_second2.adapter = CustomViewPagerAdapter()
//            (vp_second2.adapter as CustomViewPagerAdapter).apply {
//                itemCount = 10
//                notifyDataSetChanged()
//            }
            vp_third.adapter = CustomViewPagerAdapter()
//            (vp_third.adapter as CustomViewPagerAdapter).apply {
//                itemCount = 10
//                notifyDataSetChanged()
//            }
            vp_fourth.adapter = CustomViewPagerAdapter()
//            (vp_fourth.adapter as CustomViewPagerAdapter).apply {
//                itemCount = 10
//                notifyDataSetChanged()
//            }
            vp_fifth.adapter = CustomViewPagerAdapter()
//            (vp_fifth.adapter as CustomViewPagerAdapter).apply {
//                itemCount = 10
//                notifyDataSetChanged()
//            }
        }
    }

    class CustomViewPagerAdapter : PagerAdapter() {

        var itemCount = 1

        override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

        override fun getCount(): Int = itemCount

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = View(container.context)
            view.setBackgroundColor(Color.rgb(Random().nextInt(255), Random().nextInt(255), Random().nextInt(255)))
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

}
