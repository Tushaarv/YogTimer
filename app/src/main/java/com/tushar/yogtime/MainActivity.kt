package com.tushar.yogtime

import android.annotation.SuppressLint
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var textTimer: TextView? = null
    private var timer: CountDownTimer? = null

    // Minutes into Seconds
    private val yogTime: Long = 10 * 60

    //
    var mediaPlayer: MediaPlayer? = null

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Hide Status bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Set label
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM

        val customActionBar = layoutInflater.inflate(R.layout.toolbar, null)
        val customActionBarLayoutParams = ActionBar.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT,
            Gravity.CENTER
        )
        supportActionBar?.setCustomView(customActionBar, customActionBarLayoutParams)


        // Show Timer Countdown
        textTimer = findViewById(R.id.home_text_message)
        textTimer?.text = displayTime(yogTime)
    }

    /**
     * Starts the Yog Timer and Resets if clicked again
     */
    fun didClickStart(view: View) {

        // Time Converted to MilliSeconds
        val timerDuration: Long = 1000 * yogTime
        val timerInterval: Long = 1000
        var timLeft = yogTime
        var soundOne = true

        if (timer != null) {
            timer?.cancel()
            timer = null
            (view as Button).text = getString(R.string.home_button_start)
            textTimer?.text = displayTime(yogTime)
            return
        }


        timer = object : CountDownTimer(timerDuration, timerInterval) {
            override fun onFinish() {
                (view as Button).text = getString(R.string.home_button_start)
            }

            override fun onTick(millisUntilFinished: Long) {
                timLeft -= 1
                textTimer?.text = displayTime(timLeft)

                // Set the step for every Minute
                if (timLeft % 60 == 0L) {
                    // Change Text Color
                    textTimer?.setTextColor(resources.getColor(R.color.colorPrimaryDark))

                    // While the Time is not over
                    if (timLeft != 0L) {

                        // Play Alternate Sound
                        soundOne = !soundOne
                        val fileName = if (soundOne)
                            "FeelTheBody.wav"
                        else {
                            "HearTheSounds.wav"
                        }
                        playSound(fileName)

                    } else {
                        playSound("OpenYourEyes.wav")
                    }

                } else {
                    textTimer?.setTextColor(Color.BLACK)
                }
            }
        }
        timer?.start()
        (view as Button).text = getString(R.string.home_button_reset)
    }

    /**
     * Play Give Sound
     */
    private fun playSound(sound: String) {

        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setDataSource(assets.openFd(sound))
        mediaPlayer!!.prepare()
        mediaPlayer!!.setVolume(1f, 1f)
        mediaPlayer!!.start()
    }

    /**
     * Formats the time to display
     */
    fun displayTime(timeLeft: Long): String {
        return (timeLeft / 60).toString() + ":" + String.format(
            "%02d",
            (timeLeft - ((timeLeft / 60) * 60))
        )
    }
}
