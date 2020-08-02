package com.tushar.yogtime

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var textTimer: TextView? = null
    private var timer: CountDownTimer? = null

    // Minutes into Seconds
    private val yogTime: Long = 10 * 60

    //
    var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Hide Status bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Set label
        setTitle(R.string.Home_Label)

        // Show Timer Countdown
        textTimer = findViewById(R.id.home_text_message)
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

        timer?.cancel()
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
                            "Feel.mp3"
                        else {
                            "Hear.mp3"
                        }
                        playSound(fileName)

                    } else {
                        playSound("OpenYourEyes.mp3")
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
        return (timeLeft / 60).toString() + ":" + (timeLeft - ((timeLeft / 60) * 60)).toString()
    }
}
