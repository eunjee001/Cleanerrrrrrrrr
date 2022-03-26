package com.kyoungss.cleaner.expand

import android.animation.Animator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout

class Expandable @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private var expansion: Float? = null
    private var interpolator: ExpandInterpolator
    private var duration = DEFAULT_DURATION
    private var state = 0
    private var orientation = 0
//    private var parallax = 1f

    private var animator: ValueAnimator? = null

    companion object {
        const val COLLAPSED = 0
        const val COLLAPSING = 1
        const val EXPANDING = 2
        const val EXPANDED = 3
        const val DEFAULT_DURATION = 300
        const val VERTICAL = 1
        const val HORIZONTAL = 0
    }

    init {
        expansion = 0f
        interpolator = ExpandInterpolator()
        orientation = VERTICAL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = measuredHeight

        val size = if (orientation == LinearLayout.HORIZONTAL) width else height

        visibility = if (expansion == 0f && size == 0) GONE else VISIBLE

        val expansionDelta = size - Math.round(size * expansion!!)

        val parallaxDelta: Float = expansionDelta.toFloat()
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (orientation == HORIZONTAL) {
                    var direction = -1
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && layoutDirection == LAYOUT_DIRECTION_RTL) {
                        direction = 1
                    }
                    child.translationX = direction * parallaxDelta
                } else {
                    child.translationY = -parallaxDelta
                }
            }

        if (orientation == HORIZONTAL) {
            setMeasuredDimension(width - expansionDelta, height)
        } else {
            setMeasuredDimension(width, height - expansionDelta)
        }
    }

    private fun isExpanded(): Boolean {
        return state == EXPANDING || state == EXPANDED
    }

    fun expand() {
        expand(true)
    }

    private fun expand(animate: Boolean) {
        setExpanded(true, animate)
    }

    fun collapse() {
        collapse(true)
    }

    private fun collapse(animate: Boolean) {
        setExpanded(false, animate)
    }

    private fun setExpanded(expand: Boolean, animate: Boolean) {
        if (expand == isExpanded()) {
            return
        }
        val targetExpansion = if (expand) 1 else 0
        if (animate) {
            animateSize(targetExpansion)
        } else {
            setExpansion(targetExpansion.toFloat())
        }
    }

    fun setDuration(duration: Int) {
        this.duration = duration
    }

    fun setExpansion(expansion: Float) {
        if (this.expansion == expansion) {
            return
        }

        val delta = expansion - this.expansion!!
        if (expansion == 0f) {
            state = COLLAPSED
        } else if (expansion == 1f) {
            state = EXPANDED
        } else if (delta < 0) {
            state = COLLAPSING
        } else if (delta > 0) {
            state = EXPANDING
        }
        visibility = if (state == COLLAPSED) GONE else VISIBLE
        this.expansion = expansion
        requestLayout()
    }

    private fun animateSize(targetExpansion: Int) {
        if (animator != null) {
            animator!!.cancel()
            animator = null
        }
        animator = ValueAnimator.ofFloat(expansion!!, targetExpansion.toFloat())
        animator!!.setInterpolator(interpolator)
        animator!!.setDuration(duration.toLong())
        animator!!.addUpdateListener(AnimatorUpdateListener { valueAnimator -> setExpansion(valueAnimator.animatedValue as Float) })
        animator!!.addListener(ExpansionListener(targetExpansion))
        animator!!.start()
    }

    private inner class ExpansionListener(private val targetExpansion: Int) : Animator.AnimatorListener {
        private var canceled = false

        override fun onAnimationStart(animation: Animator) {
            state = if (targetExpansion == 0) COLLAPSING else EXPANDING
        }

        override fun onAnimationEnd(animation: Animator) {
            if (!canceled) {
                state = if (targetExpansion == 0) COLLAPSED else EXPANDED
                setExpansion(targetExpansion.toFloat())
            }
        }

        override fun onAnimationCancel(animation: Animator) {
            canceled = true
        }

        override fun onAnimationRepeat(animation: Animator) {}
    }
}