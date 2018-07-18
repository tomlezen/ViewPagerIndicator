package com.tlz.viewpagerindicator.dot

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.tlz.viewpagerindicator.R
import com.tlz.viewpagerindicator.ViewPagerIndicator


/**
 * Created by Tomlezen.
 * Data: 2018/7/18.
 * Time: 10:49.
 */
class DotViewPagerIndicator(ctx: Context, attrs: AttributeSet) : ViewPagerIndicator(ctx, attrs) {

    private val minRadius: Float
    private val maxRadius: Float
    private val indicatorColor: Int
    private val gap: Int
    private val acceleration: Double

    private val radiusOffset: Float
        get() = maxRadius - minRadius

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val dotView = DotView()
    private val dots = mutableListOf<PointF>()


    init {
        val ta = resources.obtainAttributes(attrs, R.styleable.DotViewPagerIndicator)

        minRadius = ta.getDimensionPixelSize(R.styleable.DotViewPagerIndicator_dvpi_min_radius, dimen(R.dimen.def_dot_min_radius)).toFloat()
        maxRadius = ta.getDimensionPixelSize(R.styleable.DotViewPagerIndicator_dvpi_max_radius, dimen(R.dimen.def_dot_max_radius)).toFloat()

        indicatorColor = ta.getColor(R.styleable.DotViewPagerIndicator_dvpi_color, resources.getColor(R.color.def_dot_color))

        acceleration = ta.getFloat(R.styleable.DotViewPagerIndicator_dvpi_acceleration, .5f).toDouble()

        gap = ta.getDimensionPixelSize(R.styleable.DotViewPagerIndicator_dvpi_dot_gap, dimen(R.dimen.def_dot_gap))

        ta.recycle()

        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = indicatorColor

        dotView.indicatorColor = indicatorColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val itemCount = viewPager?.adapter?.count ?: 0
        val widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec)
        var widthSize = widthSpecSize
        if (widthSpecMode == MeasureSpec.UNSPECIFIED || widthSpecMode == MeasureSpec.AT_MOST) {
            widthSize = (paddingLeft + paddingRight + minRadius * 2 * itemCount + gap * (itemCount - 1) * 2 + (maxRadius - minRadius) * 2).toInt() + 2
        }
        val heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec)
        var heightSize = heightSpecSize
        if (heightSpecMode == MeasureSpec.UNSPECIFIED || heightSpecMode == MeasureSpec.AT_MOST) {
            heightSize = (paddingTop + paddingBottom + maxRadius * 2 + 2).toInt()
        }
        setMeasuredDimension(widthSize, heightSize)
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            dots.clear()
            val itemCount = viewPager?.adapter?.count ?: 0
            if (itemCount > 0) {
                val currentItem = viewPager?.currentItem ?: 0
                // 计算各个点的位置.
                // 点到点中心距离
                val dist = minRadius * 2 + gap
                val y = height / 2f
                // 得到最左边开始绘制的中心点.
                val startX = width / 2 - dist * (itemCount - 1) / 2f
                (0 until itemCount).mapTo(dots, {
                    PointF(startX + dist * it, y).apply {
                        // 如果时ViewPager选择的位置，则初始化dot的位置.
                        if (it == currentItem) {
                            dotView.headPoint.x = x
                            dotView.headPoint.y = y
                            dotView.headPoint.radius = maxRadius
                            dotView.footPoint.x = x
                            dotView.footPoint.y = y
                            dotView.footPoint.radius = maxRadius
                        }
                    }
                })
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { cvs ->
            if (viewPager != null && viewPager?.adapter?.count ?: 0 > 0) {
                // 绘制圆点
                dots.forEach {
                    cvs.drawCircle(it.x, it.y, minRadius, paint)
                }

                dotView.onDraw(cvs)
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (dots.isNotEmpty()) {
            if (position < dots.size - 1) {
                dotView.headPoint.radius = minRadius + positionOffset * radiusOffset
                dotView.footPoint.radius = minRadius + (1 - positionOffset) * radiusOffset

                dotView.headPoint.x = dots[position + 1].x
                dotView.footPoint.x = dots[position].x
            } else {
                dotView.headPoint.x = dots[position].x
                dotView.footPoint.x = dots[position].x
                dotView.headPoint.radius = maxRadius
                dotView.footPoint.radius = maxRadius
            }

            postInvalidate()
        }
    }

}