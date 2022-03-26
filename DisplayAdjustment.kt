package com.kyoungss.cleaner

import android.content.Context
import android.content.res.Resources
import android.graphics.Insets
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class DisplayAdjustment private constructor(context: Context) {

    private var deviceWidth: Int = 0
    private var deviceHeight: Int = 0

//    private val defaultDisPlayWidth: Int = 720
//    private val defaultDisplayHeight: Int = 1280
    private val defaultDisPlayWidth: Int = 360
    private val defaultDisplayHeight: Int = 760


    private val matchParent: Int = RelativeLayout.LayoutParams.MATCH_PARENT
    private val wrapContent: Int = RelativeLayout.LayoutParams.WRAP_CONTENT


    companion object : SingletonHolder<DisplayAdjustment, Context>(::DisplayAdjustment)

    init {
        val windowManager: WindowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION_CODES.R <= Build.VERSION.SDK_INT) {
            val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
            val insets: Insets =
                windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            deviceWidth = windowMetrics.bounds.width() - insets.left - insets.right
            deviceHeight = windowMetrics.bounds.height() - insets.top - insets.bottom
        } else {
            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getRealMetrics(displayMetrics)
            deviceWidth = displayMetrics.widthPixels
            deviceHeight = displayMetrics.heightPixels
        }

    }

    /**
     * 디바이스 너비 값
     */
    fun getDeviceWidth(): Float {
        return deviceWidth.toFloat()
    }

    /**
     * 디바이스 높이 값
     */
    @Suppress("UNUSED")
    fun getDeviceHeight(): Float {
        return deviceHeight.toFloat()
    }

    /**
     * 너비 값에 대한 비율 계산
     * @param value 너비 값
     */
    fun calculateWidth(value: Float): Int {
        return (deviceWidth * value / defaultDisPlayWidth).roundToInt()
    }

    /**
     * 높이 값에 대한 비율 계산
     * @param value 높이 값
     */
    fun calculateHeight(value: Float): Int {
        return (deviceHeight * value / defaultDisplayHeight).roundToInt()
    }

    /**
     * 정사각형 모양일 경우 디바이스 높이, 너비 값중 작은 값으로 계산을 진행
     * 디바이스의 경우 높이,  너비가 다르기 때문에 한가지 값으로만 진행
     * @param value value
     */
    private fun calculationSquare(value: Float): Int {
        return if (deviceHeight > deviceWidth) {
            calculateWidth(value)
        } else calculateHeight(value)
    }

//    /**
//     * 레이아웃 생성 및 높이, 너비 조정
//     *
//     * @param width width
//     * @param height height
//     */
//    fun relativeLayoutScaleAdjustment(width: Int, height: Int): RelativeLayout.LayoutParams? {
//        val relativeLayout = RelativeLayout.LayoutParams(width, height)
//        setScale(relativeLayout, width.toFloat(), height.toFloat())
//        return relativeLayout
//    }

//    /**
//     * 레이아웃 생성 및 높이, 너비 조정
//     * @param width width
//     * @param height height
//     */
//    fun linearLayoutScaleAdjustment(width: Int, height: Int): LinearLayout.LayoutParams? {
//        val linearLayout = LinearLayout.LayoutParams(width, height)
//        setScale(linearLayout, width.toFloat(), height.toFloat())
//        return linearLayout
//    }

//    fun setScale(layoutParams: ViewGroup.LayoutParams, width: Int, height: Int) {
//        setScale(layoutParams, width.toFloat(), height.toFloat())
//    }

    fun setScale(layoutParams: ViewGroup.LayoutParams, width: Float, height: Float) {
        if (0 < height && 0 < width && width == height) {
            layoutParams.width = calculationSquare(width)
            layoutParams.height = calculationSquare(height)
            return
        }

        if (wrapContent.toFloat() == width || matchParent.toFloat() == width) {
            layoutParams.width = width.toInt()
        } else {
            layoutParams.width = calculateWidth(width)
        }

        if (wrapContent.toFloat() == height || matchParent.toFloat() == height) {
            layoutParams.height = height.toInt()
        } else {
            layoutParams.height = calculateHeight(height)
        }
    }

//    fun setScale(layoutParams: RelativeLayout.LayoutParams, width: Int, height: Int) {
//        setScale(layoutParams, width.toFloat(), height.toFloat())
//    }

    /**
     * 레이아웃 사이즈 조정
     * @param width width
     * @param height height
     */
    fun setScale(layoutParams: RelativeLayout.LayoutParams, width: Float, height: Float) {
        if (0 < height && 0 < width && width == height) {
            layoutParams.width = calculationSquare(width)
            layoutParams.height = calculationSquare(height)
            return
        }

        if (wrapContent.toFloat() == width || matchParent.toFloat() == width) {
            layoutParams.width = width.toInt()
        } else {
            layoutParams.width = calculateWidth(width)
        }

        if (wrapContent.toFloat() == height || matchParent.toFloat() == height) {
            layoutParams.height = height.toInt()
        } else {
            layoutParams.height = calculateHeight(height)
        }
    }

    /**
     * 레이아웃 사이즈 조정
     * @param width  width
     * @param height height
     */
    @Suppress("UNUSED")
    fun setScale(layoutParams: LinearLayout.LayoutParams, width: Float, height: Float) {
        if (0 < height && 0 < width && width == height) {
            layoutParams.width = calculationSquare(width)
            layoutParams.height = calculationSquare(height)
            return
        }

        if (wrapContent.toFloat() == width || matchParent.toFloat() == width) {
            layoutParams.width = width.toInt()
        } else {
            layoutParams.width = calculateWidth(width)
        }

        if (wrapContent.toFloat() == height || matchParent.toFloat() == height) {
            layoutParams.height = height.toInt()
        } else {
            layoutParams.height = calculateHeight(height)
        }
    }

//    fun RelativeLayout.setMargins(left: Int, top: Int, right: Int, bottom: Int) {
//        val lp: RelativeLayout.LayoutParams = layoutParams as RelativeLayout.LayoutParams
//        lp.let {
//            lp.leftMargin = left
//            lp.topMargin = top
//            lp.rightMargin = right
//            lp.bottomMargin = bottom
//            layoutParams = lp
//        }
//    }

//    fun LinearLayout.setMargins(left: Int, top: Int, right: Int, bottom: Int) {
//        val lp: LinearLayout.LayoutParams = layoutParams as LinearLayout.LayoutParams
//        lp.let {
//            lp.leftMargin = left
//            lp.topMargin = top
//            lp.rightMargin = right
//            lp.bottomMargin = bottom
//            layoutParams = lp
//        }
//    }

    fun setHeightScale(layoutParams: ViewGroup.LayoutParams, height: Float) {
        if (0 < height) {
            layoutParams.height = calculationSquare(height)
            return
        }

        if (wrapContent.toFloat() == height || matchParent.toFloat() == height) {
            layoutParams.height = height.toInt()
        } else {
            layoutParams.height = calculateHeight(height)
        }
    }

    fun setWidthScale(layoutParams: ViewGroup.LayoutParams, width: Float) {
        if (0 < width) {
            layoutParams.width = calculationSquare(width)
            return
        }

        if (wrapContent.toFloat() == width || matchParent.toFloat() == width) {
            layoutParams.width = width.toInt()
        } else {
            layoutParams.width = calculateWidth(width)
        }
    }

    fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.parent is RelativeLayout) {
            val layoutParams: RelativeLayout.LayoutParams = view.layoutParams as RelativeLayout.LayoutParams
            layoutParams.setMargins(
                calculateWidth(left.toFloat()),
                calculateHeight(top.toFloat()),
                calculateWidth(right.toFloat()),
                calculateHeight(bottom.toFloat())
            )
        } else if (view.parent is LinearLayout) {
            val layoutParams: LinearLayout.LayoutParams = view.layoutParams as LinearLayout.LayoutParams
            layoutParams.setMargins(
                calculateWidth(left.toFloat()),
                calculateHeight(top.toFloat()),
                calculateWidth(right.toFloat()),
                calculateHeight(bottom.toFloat())
            )
        }
    }

    /**
     * 레이아웃 마진 조정 조정
     */
    fun setMargins(layoutParams: RelativeLayout.LayoutParams, left: Float, top: Float, right: Float, bottom: Float) {
        layoutParams.setMargins(
            calculateWidth(left),
            calculateHeight(top),
            calculateWidth(right),
            calculateHeight(bottom)
        )
    }

    /**
     * 레이아웃 마진 조정 조정
     */
    @Suppress("UNUSED")
    fun setMargins(layoutParams: RecyclerView.LayoutParams, left: Float, top: Float, right: Float, bottom: Float) {
        layoutParams.setMargins(
            calculateWidth(left),
            calculateHeight(top),
            calculateWidth(right),
            calculateHeight(bottom)
        )
    }

    /**
     * 레이아웃 마진 조정 조정
     */
    @Suppress("UNUSED")
    fun setMargins(layoutParams: LinearLayout.LayoutParams, left: Float, top: Float, right: Float, bottom: Float) {
        layoutParams.setMargins(
            calculateWidth(left),
            calculateHeight(top),
            calculateWidth(right),
            calculateHeight(bottom)
        )
    }

    /**
     * 뷰 패딩 조정 조정
     */
    @Suppress("UNUSED")
    fun setPadding(
        view: View,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float
    ) {
        view.setPadding(
            calculateWidth(left),
            calculateHeight(top),
            calculateWidth(right),
            calculateHeight(bottom)
        )
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    @Suppress("UNUSED")
    fun dpToSp(dp: Float, context: Context): Int {
        return (dpToPx(dp, context) / context.resources
            .displayMetrics.scaledDensity).toInt()
    }

    /**
     * 텍스트 사이즈 너비 기준 계산
     * 텍스트 뷰 너비 안에 들어갈 수 있는 text
     *
     * @param textViewWidth   텍스트 뷰 너비
     * @param textDefaultSize 텍스트 사이즈
     * @param str             텍스트 내용
     */
    fun calculationTextSize(
        textView: TextView,
        textViewWidth: Float,
        textDefaultSize: Int,
        str: String?
    ) {
        var size = textDefaultSize
        val textSize = textView.paint.measureText(str).toInt()
        if (calculateWidth(textViewWidth) > textSize) {
            textView.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                calculateWidth(size.toFloat()).toFloat()
            )
            textView.text = str
            return
        }

        textView.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            calculateWidth(size--.toFloat()).toFloat()
        )

        calculationTextSize(textView, textViewWidth, size, str)
    }

    fun calculationTextSize(
        textView: TextView,
        textViewWidth: Float,
        textViewHeight: Float,
        textDefaultSize: Int,
        str: String
    ) {
        var size = textDefaultSize
        val textWidthSize = textView.paint.measureText(str).toInt()
        if (calculateWidth(textViewWidth) > textWidthSize) {
            val textHeightSize = getTextHeight(str, textView.paint, textWidthSize, size.toFloat())
            if (textViewHeight > textHeightSize) {
                textView.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    calculateWidth(size.toFloat()).toFloat()
                )
                textView.text = str
                return
            }
        }

        textView.setTextSize(
            TypedValue.COMPLEX_UNIT_PX, calculateWidth(size--.toFloat()).toFloat()
        )

        calculationTextSize(textView, textViewWidth, textViewHeight, size, str)
    }

    @Suppress("UNUSED")
    fun calculationTextSizes(
        textView: TextView,
        textViewWidth: Float,
        textViewHeight: Float,
        textDefaultSize: Int,
        str: String
    ) {
        var size = textDefaultSize
        val textWidthSize = textView.paint.measureText(str).toInt()
        if (calculateWidth(textViewWidth) > textWidthSize) {
            val textHeightSize =
                getTextHeight(str, textView.paint, textWidthSize, size.toFloat())
            if (textViewHeight > textHeightSize) {
                textView.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    calculateWidth(size.toFloat()).toFloat()
                )
                textView.text = str
                return
            }
        }

        textView.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            calculateWidth(size--.toFloat()).toFloat()
        )

        calculationTextSizes(textView, textViewWidth, textViewHeight, size, str)
    }

    fun calculationTextSize(
        button: Button,
        textViewWidth: Float,
        textViewHeight: Float,
        textDefaultSize: Int,
        str: String
    ) {
        var size = textDefaultSize
        val textWidthSize = button.paint.measureText(str).toInt()
        if (calculateWidth(textViewWidth) > textWidthSize) {
            val textHeightSize =
                getTextHeight(str, button.paint, textWidthSize, size.toFloat())
            if (textViewHeight > textHeightSize) {
                button.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    calculateWidth(size.toFloat()).toFloat()
                )
                button.text = str
                return
            }
        }

        button.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            calculateWidth(size--.toFloat()).toFloat()
        )

        calculationTextSize(button, textViewWidth, textViewHeight, size, str)
    }

    private fun getTextHeight(
        str: String,
        paint: TextPaint,
        width: Int,
        textSize: Float
    ): Int {
        paint.textSize = textSize

        if (Build.VERSION_CODES.M < Build.VERSION.SDK_INT) {
            val staticLayoutBuilder: StaticLayout.Builder = StaticLayout.Builder.obtain(
                str,
                0,
                str.length,
                paint,
                width
            ).setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0.0f, 1.0f)
                .setIncludePad(true)

            return staticLayoutBuilder.build().height
        } else {
            @Suppress("DEPRECATION")
            val layout = StaticLayout(
                str,
                paint,
                width,
                Layout.Alignment.ALIGN_NORMAL,
                1.0f,
                0.0f,
                true
            )
            return layout.height
        }
    }

    @Suppress("UNUSED")
    fun dp2px(resources: Resources, dp: Float): Float {
        val scale = resources.displayMetrics.density
        return dp * scale + 0.5f
    }

    @Suppress("UNUSED")
    fun sp2px(resources: Resources, sp: Float): Float {
        val scale = resources.displayMetrics.scaledDensity
        return sp * scale
    }

}