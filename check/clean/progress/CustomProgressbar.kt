package com.kyoungss.cleaner.check.clean.progress

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import com.kyoungss.cleaner.R
import com.kyoungss.cleaner.check.clean.CleanData
import com.kyoungss.cleaner.data.RepositoryImpl
import kotlin.collections.ArrayList

class CustomProgressbar@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {
    private var animate = false
    private var animateDuration: Long = 1000  // In milliseconds
    private var progressAnimator: ValueAnimator? = null
    private var progressValue: Int = 0
    private val paintProgress = Paint(Paint.ANTI_ALIAS_FLAG)
    private var totalValue: Int = 100
    private var progressInterpolator: Interpolator = LinearInterpolator()
    private val circleBounds = RectF()
    private var startAngle: Float = -270f

    var cleanPathList: ArrayList<CleanData> = arrayListOf()

    companion object{
        var progressList = arrayListOf<CleanProgressData>()
    }
    init {
        if(attrs!= null){
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressView)

            totalValue = typedArray.getInt(R.styleable.CircularProgressView_totalValue, 100)
            progressValue = typedArray.getInt(R.styleable.CircularProgressView_progressValue, 0)
            animateDuration = typedArray.getInt(R.styleable.CircularProgressView_animateDuration, 300).toLong()
            animate = typedArray.getBoolean(R.styleable.CircularProgressView_animate, false)
            typedArray.recycle()

        }
        progressValue = getValidProgressValue(progressValue)


    }

    @SuppressLint("DrawAllocation")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val paintBackground = Paint()
        val paintProgress = Paint()

        paintBackground.color = Color.GRAY
        paintBackground.style = Paint.Style.STROKE
        paintBackground.strokeWidth = 30f

        canvas?.drawArc(20f, 20f, 655.3f, 655.3f, 45f, -270f, false
            , paintBackground)

        paintProgress.color =Color.BLUE
        paintProgress.strokeWidth = 30f
        paintProgress.style = Paint.Style.STROKE


        val progressSweepAngle = if(totalValue == progressValue) 270f else ((270f / totalValue) * progressValue)

        canvas?.drawArc(20f, 20f, 655.3f, 655.3f,135f,progressSweepAngle,false, paintProgress)
    }


    fun setProgress(progress: Int, animate: Boolean = this.animate) {
        progressValue = 0

        val validProgress = getValidProgressValue(progress)

        if (animate) {
            // Cancel any on-going animation

            val animator = ValueAnimator.ofInt(this.progressValue, validProgress)
            animator.interpolator = progressInterpolator
            animator.duration = animateDuration
            animator.addUpdateListener {

                this.progressValue = getValidProgressValue(it.animatedValue as Int)
                RepositoryImpl.setProgressTextLive(progressValue)

                invalidate()
            }
            animator.start()
            progressAnimator = animator
        } else {
            this.progressValue = validProgress
            invalidate()
        }

    }


    fun getValidProgressValue(input: Int): Int {
        return when {
            input < 0 -> 0
            input > totalValue -> totalValue
            else -> input
        }
    }
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        progressAnimator?.cancel()
    }

}