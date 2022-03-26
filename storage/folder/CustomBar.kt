package com.kyoungss.cleaner.storage.folder

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class CustomBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppCompatImageView(context, attrs) {

    private val paint = Paint()
    private val path = Path()
    private val strokeWidth = 200f;
    private var bitmap: Bitmap? = null
    private var rectF: RectF? = null
    private var customBarDataList = ArrayList<CustomBarData>()

    var widthSize: Int? = null
    var heightSize: Int? = null

    var canvas: Canvas? = null

    companion object{
        const val TYPE_IMAGE = 0x01
        const val TYPE_AUDIO = 0x02
        const val TYPE_VIDEO = 0x03
        const val TYPE_FILE = 0x04
        const val TYPE_DOCUMENT = 0x05
        const val TYPE_DOWNLOAD = 0x06
        const val TYPE_Rest = 0x07
    }

    init {
        widthSize = 1000
        heightSize = 100

        bitmap = Bitmap.createBitmap(widthSize!!, heightSize!!, Bitmap.Config.ARGB_8888)

        canvas = Canvas()
        canvas!!.setBitmap(bitmap)

        setPaint(paint, -0x131314)
        canvas!!.drawLine(0f, 0f, widthSize!!.toFloat(), 0f, paint)

        rectF = RectF(0f, 0f, bitmap!!.width.toFloat(), bitmap!!.height.toFloat())
    }




    private fun setPaint(paint: Paint, color: Int) {
        paint.color = color
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeWidth = strokeWidth
    }

    fun setDataType(type: Int, value: Int, color: Int) {
        if(!changeData(type, value, color)){
            customBarDataList.add(CustomBarData(type = type, value = value, color = color))
        }
    }

    private fun drawCanvas() {
        var startX = 0;
        var stopX = 0;

        customBarDataList.sortedBy { it.type }

        for (data in customBarDataList) {
            setPaint(paint, data.color)

            stopX += data.value

            canvas!!.drawLine(startX.toFloat(), 0f, stopX.toFloat(), 0f, paint)

            if (TYPE_DOWNLOAD == data.type){
                return
            }

            setPaint(paint, Color.WHITE)
            canvas!!.drawLine(stopX.toFloat(), 0f, (stopX+5).toFloat(), 0f, paint)

            startX = stopX+5
        }
    }

    fun createImanage() {
        drawCanvas()

        scaleType = ScaleType.CENTER_CROP
        setImageBitmap(bitmap)
        invalidate()
    }

    private fun changeData(type: Int, value: Int, color: Int): Boolean {
        var isCanvasData = false

        for (data in customBarDataList) {
            if (type == data.type) {
                data.value = value
                data.color = color

                isCanvasData = true
            }
        }
        return isCanvasData
    }

    override fun onDraw(canvas: Canvas?) {
        path.addRoundRect(rectF!!, 18f, 18f, Path.Direction.CW)
        canvas!!.clipPath(path)

        super.onDraw(canvas)
    }
}