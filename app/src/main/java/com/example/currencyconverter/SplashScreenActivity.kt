package com.example.currencyconverter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyconverter.databinding.ActivitySplashScreenBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity(R.layout.activity_splash_screen) {

    private lateinit var binding: ActivitySplashScreenBinding

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)

        if (savedInstanceState == null) {
            setUpLottieAnimation()
            setUpTextViewAnimation()
            startMainActivity()
        }

        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }

    private fun setUpLottieAnimation() {
        val lottieAnimation = binding.splashScreenCurrencyExchangeLogoAnimation
        lottieAnimation.setAnimation(com.example.currencyconverter.brandkit.R.raw.lottie_coin_animation)
        lottieAnimation.speed = 1.8f
        lottieAnimation.playAnimation()
    }

    private fun setUpTextViewAnimation() {
        val textColorId = com.example.currencyconverter.brandkit.R.color.currency_converter_light_500
        val appNameText = getString(R.string.splash_screen_app_name)
        val textSwitcher = binding.splashScreenAppNameTextSwitcher
        textSwitcher.setFactory {
            val textView = TextView(this)
            textView.textSize = 25f
            textView.gravity = Gravity.CENTER
            textView.typeface = Typeface.DEFAULT_BOLD
            textView.setTextColor(resources.getColor(textColorId))
            textView
        }

        val characterArray = appNameText.split("").filter {
            it.isNotEmpty()
        }.toTypedArray()

        var currentCharIndex = 0

        // Create a custom Animation object that only animates the appended text
        val inAnimation = AlphaAnimation(0f, 1f)
        inAnimation.duration = ANIMATION_DELAY / 2
        inAnimation.interpolator = AccelerateInterpolator()

        val outAnimation = AlphaAnimation(1f, 1f) // No animation for outgoing text

        textSwitcher.inAnimation = inAnimation
        textSwitcher.outAnimation = outAnimation

        fun displayNextCharacter() {
            val nextCharacter = characterArray[currentCharIndex]
            val currentText = (textSwitcher.currentView as TextView).text
            val newText = if (currentText.isNullOrEmpty()) nextCharacter else "$currentText $nextCharacter"
            textSwitcher.setText(newText)
            currentCharIndex++
        }

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                displayNextCharacter()

                if (currentCharIndex == characterArray.size) {
                    handler.removeCallbacks(this)
                } else {
                    handler.postDelayed(this, ANIMATION_DELAY)
                }
            }
        }

        handler.post(runnable)
    }

    private fun startMainActivity() {
        compositeDisposable.add(
            Observable
                .timer(DELAY, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe {
                    startActivity(Intent(
                        this@SplashScreenActivity,
                        CurrencyConverterActivity::class.java
                    ))
                    finish()
                }
        )
    }

    companion object {
        private const val DELAY: Long = 5
        private const val ANIMATION_DELAY: Long = 150L
    }
}