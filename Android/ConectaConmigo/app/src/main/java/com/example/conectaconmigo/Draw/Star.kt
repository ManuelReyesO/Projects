package com.example.conectaconmigo.Draw

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.conectaconmigo.R

private val handler = Handler(Looper.getMainLooper())
private val delayMillis = 1000 // Tiempo en milisegundos antes de que cambien las imágenes
private val starFilledRes = R.drawable.calest

class Star : AppCompatActivity() {

    private val stars: List<ImageView> by lazy {
        listOf(findViewById(R.id.star1), findViewById(R.id.star2), findViewById(R.id.star3))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_star)
        // Lanza la función para cambiar las imágenes con un retraso
        changeImagesOneByOne()
    }

    private fun changeImagesOneByOne() {
        for ((index, star) in stars.withIndex()) {
            handler.postDelayed({
                runOnUiThread {
                    // Cambia la imagen de la estrella actual
                    if(index<2){
                        star.setImageResource(starFilledRes)
                    }
                }
            }, (index + 1) * delayMillis.toLong())
        }
    }
}