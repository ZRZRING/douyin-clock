package com.zrzring.douyin_clock

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var hourTextView: TextView
    private lateinit var minuteTextView: TextView
    private lateinit var secondProgressView: ProgressView

    private fun initViews() {
        hourTextView = findViewById(R.id.hourTextView)
        minuteTextView = findViewById(R.id.minuteTextView)
        secondProgressView = findViewById(R.id.secondProgressView)
    }

    private val handler = Handler(Looper.getMainLooper())

    private val timeUpdater = object : Runnable {
        private fun updateTimeDisplay(calendar: Calendar) {
            // 等待视图初始化
            if (!this@MainActivity::hourTextView.isInitialized ||
                !this@MainActivity::minuteTextView.isInitialized ||
                !this@MainActivity::secondProgressView.isInitialized
            ) {
                handler.postDelayed(this, 100)
                return
            }
            hourTextView.text = calendar.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
            minuteTextView.text = calendar.get(Calendar.MINUTE).toString().padStart(2, '0')
        }

        override fun run() {
            // 等待视图初始化
            if (!this@MainActivity::hourTextView.isInitialized ||
                !this@MainActivity::minuteTextView.isInitialized ||
                !this@MainActivity::secondProgressView.isInitialized
            ) {
                handler.postDelayed(this, 100)
                return
            }

            val calendar = Calendar.getInstance()
            val currentMillis = System.currentTimeMillis()
            val second = calendar.get(Calendar.SECOND)

            secondProgressView.updateProgress(second)
            updateTimeDisplay(calendar)

            val delay = 1000 - (currentMillis % 1000)
            handler.postDelayed(this, delay)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 保持屏幕常亮
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        initViews()
        handler.post(timeUpdater)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(timeUpdater)
    }

    override fun onResume() {
        super.onResume()
        handler.post(timeUpdater)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timeUpdater)
    }
}