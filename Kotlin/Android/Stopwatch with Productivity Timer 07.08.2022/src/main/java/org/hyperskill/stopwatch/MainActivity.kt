package org.hyperskill.stopwatch

import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private object Const {
        const val NOTIFICATION_ID: Int = 393939
        const val CHANNEL_ID: String = "org.hyperskill"
    }

    lateinit var textView: TextView
    lateinit var progressBar: ProgressBar
    lateinit var notificationManager: NotificationManager
    lateinit var timerNotification: Notification

    private var mSec = 0L
    private val handler = Handler(Looper.getMainLooper())
    private var limit: Long? = null

    private val timer: Runnable = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun run() {
            mSec += 1000
            val color = Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
            textView.text = String.format("%1\$tM:%1\$tS", mSec)
            progressBar.indeterminateTintList = ColorStateList.valueOf(color);
            if (limit != null && mSec > limit!! * 1000) {
                textView.setTextColor(Color.RED)
                notificationManager.notify(Const.NOTIFICATION_ID, timerNotification)
            }
            handler.postDelayed(this, 1000)
        }
    }

    private fun startTimer() {
        if (mSec == 0L) {
            handler.postDelayed(timer, 1000)
        }
    }

    private fun resetTimer() {
        handler.removeCallbacks(timer)
        mSec = 0
        textView.text = getString(R.string.startTime)
        textView.setTextColor(Color.parseColor("#008577"))
    }

    private fun buildNotify(): Notification {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this)
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentTitle("Notification");
        builder.setContentText("Timer exceeded");
        builder.setChannelId(Const.CHANNEL_ID)
        return builder.build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        timerNotification = buildNotify()
        textView = findViewById(R.id.textView)
        progressBar = findViewById(R.id.progressBar)
        val settingsButton = findViewById<Button>(R.id.settingsButton)
        settingsButton.setOnClickListener {
            val contentView = LayoutInflater.from(this).inflate(R.layout.dialog_main, null, false)
            AlertDialog.Builder(this)
                .setTitle("Set upper limit in seconds")
                .setView(contentView)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    val edittext = contentView.findViewById<EditText>(R.id.upperLimitEditText)
                    limit = edittext.text.toString().toLong()
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
        }

        findViewById<Button>(R.id.startButton).setOnClickListener {
            startTimer()
            progressBar.visibility = View.VISIBLE
            settingsButton.isEnabled = false
        }
        findViewById<Button>(R.id.resetButton).setOnClickListener {
            resetTimer()
            progressBar.visibility = View.INVISIBLE
            settingsButton.isEnabled = true
        }
    }
}