package com.example.currencyconverter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
            setUpLogoAnimation()
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

    private fun setUpLogoAnimation() {
        val animationView = binding.splashScreenCurrencyExchangeLogoAnimation
        animationView.setAnimation(com.example.currencyconverter.brandkit.R.raw.lottie_coin_animation)
        animationView.playAnimation()
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
        private const val DELAY: Long = 3
    }
}