package com.tlz.viewpagerindicator

import android.content.Context
import android.support.annotation.CallSuper
import android.support.annotation.Nullable
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View

/**
 * Created by Tomlezen.
 * Data: 2018/7/18.
 * Time: 15:14.
 */
abstract class ViewPagerIndicator(ctx: Context, attrs: AttributeSet) : View(ctx, attrs) {

    protected var viewPager: ViewPager? = null

    /**
     * ViewPager适配器改变监听.
     */
    protected val onAdapterChangeListener = ViewPager.OnAdapterChangeListener { viewPager, oldAdapter, newAdapter ->
        onAdapterChanged(viewPager, oldAdapter, newAdapter)
        requestLayout()
    }

    /**
     * ViewPager页面切换监听.
     */
    protected val onPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(state: Int) {
            this@ViewPagerIndicator.onPageScrollStateChanged(state)
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            this@ViewPagerIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            this@ViewPagerIndicator.onPageSelected(position)
        }
    }

    /**
     * 初始化.
     * @param vp ViewPager
     */
    @CallSuper
    open fun setupWithViewPager(vp: ViewPager) {
        this.viewPager?.removeOnAdapterChangeListener(onAdapterChangeListener)
        this.viewPager?.removeOnPageChangeListener(onPageChangeListener)
        this.viewPager = vp
        this.viewPager?.addOnAdapterChangeListener(onAdapterChangeListener)
        this.viewPager?.addOnPageChangeListener(onPageChangeListener)

        requestLayout()
    }

    protected abstract fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)
    open protected fun onPageScrollStateChanged(state: Int) {}
    open protected fun onPageSelected(position: Int) {}
    open protected fun onAdapterChanged(viewPager: ViewPager, oldAdapter: PagerAdapter?, newAdapter: PagerAdapter?) {}

    protected fun dimen(id: Int) = resources.getDimensionPixelSize(id)

}