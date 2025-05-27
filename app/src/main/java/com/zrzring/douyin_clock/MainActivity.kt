package com.zrzring.douyin_clock

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvHour: TextView
    private lateinit var tvMinute: TextView
    private lateinit var tvSecond: TextView
    private val handler = Handler(Looper.getMainLooper())

    private val timeUpdater = object : Runnable {
        override fun run() {
            if (!this@MainActivity::tvSecond.isInitialized) {
                handler.postDelayed(this, 100)
                return
            }

            val calendar = Calendar.getInstance()
            val currentMillis = System.currentTimeMillis()

            tvSecond.animate()
                .alpha(0.2f)
                .setDuration(100)
                .withEndAction {
                    if (!isFinishing && !isDestroyed) {
                        updateTimeDisplay(calendar)
                        tvSecond.animate()
                            .alpha(1f)
                            .setDuration(100)
                            .start()
                    } else {
                        if (this@MainActivity::tvSecond.isInitialized) {
                            tvSecond.alpha = 1f
                        }
                    }
                }
                .start()

            val delay = 1000 - (currentMillis % 1000)
            handler.postDelayed(this, delay)
        }
    }

    private fun updateTimeDisplay(calendar: Calendar) {
        if (!this@MainActivity::tvHour.isInitialized ||
            !this@MainActivity::tvMinute.isInitialized ||
            !this@MainActivity::tvSecond.isInitialized) {
            return
        }
        tvHour.text = calendar.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
        tvMinute.text = calendar.get(Calendar.MINUTE).toString().padStart(2, '0')
        tvSecond.text = calendar.get(Calendar.SECOND).toString().padStart(2, '0')
    }

    private fun initViews() {
        tvHour = findViewById(R.id.tvHour)
        tvMinute = findViewById(R.id.tvMinute)
        tvSecond = findViewById(R.id.tvSecond)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        initViews()

        val initialCalendar = Calendar.getInstance()
        updateTimeDisplay(initialCalendar)
        if (this::tvSecond.isInitialized) {
            tvSecond.alpha = 1f
        }
    }

    override fun onResume() {
        super.onResume()
        val currentCalendar = Calendar.getInstance()
        updateTimeDisplay(currentCalendar)
        if (this::tvSecond.isInitialized) {
            tvSecond.alpha = 1f
        }
        handler.post(timeUpdater)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(timeUpdater)
        if (this::tvSecond.isInitialized) {
            tvSecond.animate().cancel()
            tvSecond.alpha = 1f
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timeUpdater)
        if (this::tvSecond.isInitialized) {
            tvSecond.animate().cancel()
        }
    }
}