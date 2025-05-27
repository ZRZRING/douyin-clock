import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zrzring.douyin_clock.R
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvHour: TextView
    private lateinit var tvMinute: TextView
    private lateinit var tvSecond: TextView
    private val handler = Handler(Looper.getMainLooper())

    // 时间更新任务（精确到毫秒级同步）
    private val timeUpdater = object : Runnable {
        override fun run() {
            val calendar = Calendar.getInstance()
            val currentMillis = System.currentTimeMillis()

            // 更新秒显示并执行动画
            tvSecond.animate().alpha(0.2f).setDuration(100).withEndAction {
                updateTimeDisplay(calendar)
                tvSecond.animate().alpha(1f).setDuration(100)
            }

            // 计算下一整秒的时间差
            val delay = 1000 - (currentMillis % 1000)
            handler.postDelayed(this, delay)
        }

        private fun updateTimeDisplay(calendar: Calendar) {
            tvHour.text = calendar.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
            tvMinute.text = calendar.get(Calendar.MINUTE).toString().padStart(2, '0')
            tvSecond.text = calendar.get(Calendar.SECOND).toString().padStart(2, '0')
        }
    }

    private fun initViews() {
        tvHour = findViewById(R.id.tvHour)
        tvMinute = findViewById(R.id.tvMinute)
        tvSecond = findViewById(R.id.tvSecond)
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
}