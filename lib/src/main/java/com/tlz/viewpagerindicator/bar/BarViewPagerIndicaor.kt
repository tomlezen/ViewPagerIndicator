package com.tlz.viewpagerindicator.bar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.tlz.viewpagerindicator.R
import com.tlz.viewpagerindicator.ViewPagerIndicator

/**
 * Created by Tomlezen.
 * Data: 2018/7/18.
 * Time: 15:34.
 */
class BarViewPagerIndicaor(ctx: Context, attrs: AttributeSet) : ViewPagerIndicator(ctx, attrs) {

    private val barHeight: Float
    private val barMinWidth: Float
    private val barMaxWidth: Float
    private val barGap: Int
    private var barColor: Int

    private val widthOffset: Float
        get() = barMaxWidth - barMinWidth

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val bars = mutableListOf<RectF>()

    init {
        val ta = resources.obtainAttributes(attrs, R.styleable.BarViewPagerIndicaor)

        barHeight = ta.getDimensionPixelSize(R.styleable.BarViewPagerIndicaor_bvpi_bar_height, dimen(R.dimen.def_bar_height)).toFloat()
        barMinWidth = ta.getDimensionPixelSize(R.styleable.BarViewPagerIndicaor_bvpi_bar_min_width, dimen(R.dimen.def_bar_min_width)).toFloat()
        barMaxWidth = ta.getDimensionPixelSize(R.styleable.BarViewPagerIndicaor_bvpi_bar_max_width, dimen(R.dimen.def_bar_max_width)).toFloat()

        barColor = ta.getColor(R.styleable.BarViewPagerIndicaor_bvpi_color, resources.getColor(R.color.def_bar_color))

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
            widthSize = (paddingLeft + paddingRight + (barGap + barMinWidth) * (itemCount - 1) + barMaxWidth).toInt() + 2
        }
        val heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec)
        var heightSize = heightSpecSize
        if (heightSpecMode == MeasureSpec.UNSPECIFIED || heightSpecMode == MeasureSpec.AT_MOST) {
            heightSize = (paddingTop + paddingBottom + barHeight + 2).toInt()
        }
        setMeasuredDimension(widthSize, heightSize)
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            bars.clear()
            val itemCount = viewPager?.adapter?.count ?: 0
            if (itemCount > 0) {
                val currentItem = viewPager?.currentItem ?: 0
                // 计算各个点的位置.
                val y = height / 2f
                // 得到最左边开始绘制的位置.
                var startX = width / 2 - ((barGap + barMinWidth) * (itemCount - 1) + barMaxWidth) / 2f
                (0 until itemCount).mapTo(bars, {
                    val l = startX
                    val r = l + if (currentItem == it) barMaxWidth else barMinWidth
                    startX = r + barGap
                    RectF(l, y - barHeight / 2, r, y + barHeight / 2)
                })
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { cvs ->
            if (viewPager != null && viewPager?.adapter?.count ?: 0 > 0) {
                bars.forEach {
                    cvs.drawRoundRect(it, barHeight / 2f, barHeight / 2f, paint)
                }
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (bars.isNotEmpty()) {
            if (position < bars.size - 1) {
                bars[position + 1].apply {
                    set(right - barMinWidth - widthOffset * positionOffset, top, right, bottom)
                }
                bars[position].apply {
                    set(left, top, left + barMinWidth + widthOffset * (1 - positionOffset), bottom)
                }
            } else {
                bars[position].apply {
                    set(right - barMinWidth - widthOffset, top, right, bottom)
                }
                bars[position - 1].apply {
                    set(left, top, left + barMinWidth, bottom)
                }
            }

            postInvalidate()
        }
    }

}