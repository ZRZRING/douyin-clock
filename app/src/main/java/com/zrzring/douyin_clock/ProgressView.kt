package com.zrzring.douyin_clock

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat

class ProgressView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var progressAnimator: ValueAnimator? = null
    private var currentProgress = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 4.dpToPx()
        color = ContextCompat.getColor(context, R.color.shadow)
    }

    fun updateProgress(seconds: Int) {
        val target = (seconds + 1) * 6f

        progressAnimator?.cancel()
        progressAnimator = ValueAnimator.ofFloat(currentProgress, target).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                currentProgress = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val size = width.coerceAtMost(height).toFloat()
        val padding = paint.strokeWidth / 2

        paint.alpha = 30
        canvas.drawArc(
            padding, padding,
            size - padding, size - padding,
            -90f, 360f,
            false, paint
        )

        paint.alpha = 255
        canvas.drawArc(
            padding, padding,
            size - padding, size - padding,
            -90f, currentProgress % 360,
            false, paint
        )
    }
}

fun Int.dpToPx(): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
)