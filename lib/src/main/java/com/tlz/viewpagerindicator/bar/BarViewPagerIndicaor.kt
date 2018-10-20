package com.tlz.viewpagerindicator.bar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import com.tlz.viewpagerindicator.R
import com.tlz.viewpagerindicator.ViewPagerIndicator

/**
 * Created by Tomlezen.
 * Data: 2018/7/18.
 * Time: 15:34.
 */
class BarViewPagerIndicator(ctx: Context, attrs: AttributeSet) : ViewPagerIndicator<RectF>(ctx, attrs) {

    private val barScaleDirct: Int
    private val barHeight: Float
    private val barMinWidth: Float
    private val barMaxWidth: Float
    private val barGap: Int
    private var barColor: Int

    private val widthOffset: Float
        get() = barMaxWidth - barMinWidth

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        val ta = resources.obtainAttributes(attrs, R.styleable.BarViewPagerIndicator)
        barScaleDirct = ta.getInt(R.styleable.BarViewPagerIndicator_bvpi_scale_direction, 0)

        barHeight = ta.getDimensionPixelSize(R.styleable.BarViewPagerIndicator_bvpi_bar_height, dimen(R.dimen.def_bar_height)).toFloat()
        barMinWidth = ta.getDimensionPixelSize(R.styleable.BarViewPagerIndicator_bvpi_bar_min_width, dimen(R.dimen.def_bar_min_width)).toFloat()
        barMaxWidth = ta.getDimensionPixelSize(R.styleable.BarViewPagerIndicator_bvpi_bar_max_width, dimen(R.dimen.def_bar_max_width)).toFloat()

        barColor = ta.getColor(R.styleable.BarViewPagerIndicator_bvpi_color, resources.getColor(R.color.def_bar_color))

        barGap = ta.getDimensionPixelSize(R.styleable.DotViewPagerIndicator_dvpi_dot_gap, dimen(R.dimen.def_bar_gap))

        ta.recycle()

        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = barColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val itemCount = viewPager?.adapter?.count ?: 0
        val widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec)
        var widthSize = widthSpecSize
        if (widthSpecMode == MeasureSpec.UNSPECIFIED || widthSpecMode == MeasureSpec.AT_MOST) {
            widthSize = when (barScaleDirct) {
                0 -> (paddingLeft + paddingRight + (barGap + barMinWidth) * (itemCount - 1) + barMaxWidth).toInt() + 2
                else -> (paddingLeft + paddingRight + (barGap + barMaxWidth / 2 + barMinWidth / 2) * (itemCount - 1) + barMaxWidth).toInt() + 2
            }
        }
        val heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec)
        var heightSize = heightSpecSize
        if (heightSpecMode == MeasureSpec.UNSPECIFIED || heightSpecMode == MeasureSpec.AT_MOST) {
            heightSize = (paddingTop + paddingBottom + barHeight + 2).toInt()
        }
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onCustomDraw(cvs: Canvas) {
        if (viewPager != null && viewPager?.adapter?.count ?: 0 > 0) {
            items.forEach {
                cvs.drawRoundRect(it, barHeight / 2f, barHeight / 2f, paint)
            }
        }
    }

    override fun calculate() {
        items.clear()
        val itemCount = viewPager?.adapter?.count ?: 0
        if (itemCount > 0) {
            val currentItem = viewPager?.currentItem ?: 0
            // 计算各个点的位置.
            val y = height / 2f
            when (barScaleDirct) {
                0 -> {
                    var startX = width / 2 - ((barGap + barMinWidth) * (itemCount - 1) + barMaxWidth) / 2f
                    (0 until itemCount).mapTo(items) {
                        val l = startX
                        val r = l + if (currentItem == it) barMaxWidth else barMinWidth
                        startX = r + barGap
                        RectF(l, y - barHeight / 2, r, y + barHeight / 2)
                    }
                }
                else -> {
                    var startX = width / 2 - ((barGap + barMaxWidth / 2 + barMinWidth / 2) * (itemCount - 1) + barMaxWidth) / 2f + barMaxWidth / 2
                    (0 until itemCount).mapTo(items) {
                        val d = (if (currentItem == it) barMaxWidth else barMinWidth) / 2
                        val l = startX - d
                        val r = startX + d
                        startX += barMaxWidth / 2 + barMinWidth / 2 + barGap
                        RectF(l, y - barHeight / 2, r, y + barHeight / 2)
                    }
                }
            }

        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (items.isNotEmpty()) {
            if (position < items.size - 1) {
                when (barScaleDirct) {
                    0 -> {
                        items[position + 1].apply {
                            set(right - barMinWidth - widthOffset * positionOffset, top, right, bottom)
                        }
                        items[position].apply {
                            set(left, top, left + barMinWidth + widthOffset * (1 - positionOffset), bottom)
                        }
                    }
                    else -> {
                        items[position + 1].apply {
                            val centerX = centerX()
                            val d = (widthOffset * positionOffset + barMinWidth) / 2
                            set(centerX - d, top, centerX + d, bottom)
                        }
                        items[position].apply {
                            val centerX = centerX()
                            val d = (widthOffset * (1 - positionOffset) + +barMinWidth) / 2
                            set(centerX - d, top, centerX + d, bottom)
                        }
                    }
                }
            } else {
                when (barScaleDirct) {
                    0 -> {
                        items[position].apply {
                            set(right - barMinWidth - widthOffset, top, right, bottom)
                        }
                        items[position - 1].apply {
                            set(left, top, left + barMinWidth, bottom)
                        }
                    }
                    else -> {
                        items[position].apply {
                            val centerX = centerX()
                            set(centerX - barMaxWidth / 2, top, centerX + barMaxWidth / 2, bottom)
                        }
                        items[position - 1].apply {
                            val centerX = centerX()
                            set(centerX - barMinWidth / 2, top, centerX + barMinWidth / 2, bottom)
                        }
                    }
                }
            }

            postInvalidate()
        }
    }

    override fun onAdapterChanged(viewPager: ViewPager, oldAdapter: PagerAdapter?, newAdapter: PagerAdapter?) {
        // 清空数据
        items.clear()
    }

}