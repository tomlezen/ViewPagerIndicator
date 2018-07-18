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

        btn_change_adapter.setOnClickListener {
            vp_first.adapter = CustomViewPagerAdapter()
            vp_second.adapter = CustomViewPagerAdapter()
        }
    }

    class CustomViewPagerAdapter : PagerAdapter() {

        private val itemCount = Random().nextInt(4) + 4

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
