package com.example.conectaconmigo

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.conectaconmigo.Cuentas.Cuenta
import com.example.conectaconmigo.Juegos.FormsGame
import com.example.conectaconmigo.Juegos.NumberGame
import com.example.conectaconmigo.Juegos.TwinsGame
import com.example.conectaconmigo.Juegos.WhatFruitThis
import com.example.conectaconmigo.Juegos.WhatSoundAnimal

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val btnAnimals = findViewById<AppCompatButton>(R.id.buttonAnimals)
        btnAnimals.setOnClickListener { animalGame() }
        val buttonFruits = findViewById<AppCompatButton>(R.id.buttonFruits)
        buttonFruits.setOnClickListener { fruitGame() }
        val buttonNumbers = findViewById<AppCompatButton>(R.id.buttonNumbers)
        buttonNumbers.setOnClickListener { numberGame() }
        val buttonMemory = findViewById<AppCompatButton>(R.id.buttonMemory)
        buttonMemory.setOnClickListener { twinsGame() }
        val buttonForms = findViewById<AppCompatButton>(R.id.buttonForms)
        buttonForms.setOnClickListener { formsGame() }
        val buttonAccount = findViewById<AppCompatButton>(R.id.cuenta)
        buttonAccount.setOnClickListener { Account() }


        // Supongamos que tienes 5 botones con los IDs: button1, button2, ..., button5
        val buttons = arrayOf(
            btnAnimals,
            buttonFruits,
            buttonNumbers,
            buttonMemory,
            buttonForms
        )
        val animationDuration = 1000L

        // Aplica la animación a cada botón
        for (button in buttons) {
            startButtonAnimation(button, animationDuration)
        }
    }

    private fun startButtonAnimation(button: AppCompatButton, duration: Long) {
        val animator = ObjectAnimator.ofFloat(button, View.TRANSLATION_Y, -25f)
        animator.duration = duration

        animator.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                // Cuando la animación hacia arriba termina, anima hacia abajo
                ObjectAnimator.ofFloat(button, View.TRANSLATION_Y, 0f)
                    .setDuration(duration)
                    .start()
            }
        })

        // Repetir la animación infinitamente
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.repeatMode = ObjectAnimator.REVERSE

        animator.start()
    }

    private fun animalGame(){
        val intent = Intent(this, WhatSoundAnimal::class.java)
        startActivity(intent)
    }

    private fun fruitGame(){
        val intent = Intent(this, WhatFruitThis::class.java)
        startActivity(intent)
    }

    private fun numberGame(){
        val intent = Intent(this, NumberGame::class.java)
        startActivity(intent)
    }

    private fun twinsGame(){
        val intent = Intent(this, TwinsGame::class.java)
        startActivity(intent)
    }

    private fun formsGame(){
        val intent = Intent(this, FormsGame::class.java)
        startActivity(intent)
    }

    private fun Account(){
        val intent = Intent(this,Cuenta::class.java)
        startActivity(intent)
        finish()
    }
}