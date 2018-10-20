package com.tlz.viewpagerindicator

import android.content.Context
import android.database.DataSetObserver
import android.graphics.Canvas
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
abstract class ViewPagerIndicator<Item>(ctx: Context, attrs: AttributeSet) : View(ctx, attrs) {

    protected var viewPager: ViewPager? = null

    protected val items = mutableListOf<Item>()

    /**
     * ViewPager适配器改变监听.
     */
    protected val onAdapterChangeListener = ViewPager.OnAdapterChangeListener { viewPager, oldAdapter, newAdapter ->
        oldAdapter?.unregisterDataSetObserver(dataSetObserver)
        newAdapter?.registerDataSetObserver(dataSetObserver)
        onAdapterChanged(viewPager, oldAdapter, newAdapter)
        requestLayout()
    }

    /**
     * 适配器数据更改.
     */
    protected val dataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            super.onChanged()
            postInvalidate()
        }
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
        this.viewPager?.adapter?.unregisterDataSetObserver(dataSetObserver)
        this.viewPager = vp
        this.viewPager?.addOnAdapterChangeListener(onAdapterChangeListener)
        this.viewPager?.addOnPageChangeListener(onPageChangeListener)
        this.viewPager?.adapter?.registerDataSetObserver(dataSetObserver)

        requestLayout()
    }

    @CallSuper
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            calculate()
        }
    }

    final override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
        if (!check()) {
            requestLayout()
            if (!check()) {
                calculate()
            }
        }
        canvas?.let { onCustomDraw(it) }
    }

    protected abstract fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)
    open protected fun onPageScrollStateChanged(state: Int) {}
    open protected fun onPageSelected(position: Int) {}
    open protected fun onAdapterChanged(viewPager: ViewPager, oldAdapter: PagerAdapter?, newAdapter: PagerAdapter?) {}
    open protected fun check(): Boolean = items.size == viewPager?.adapter?.count ?: 0
    open protected abstract fun calculate()
    open protected abstract fun onCustomDraw(cvs: Canvas)

    protected fun dimen(id: Int) = resources.getDimensionPixelSize(id)

}