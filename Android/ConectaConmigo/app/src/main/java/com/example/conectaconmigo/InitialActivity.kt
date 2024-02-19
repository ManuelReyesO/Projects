package com.example.conectaconmigo

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class InitialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        val textView = findViewById<TextView>(R.id.myTextView)
        val icono = findViewById<ImageView>(R.id.logo1)
        val titulo = findViewById<TextView>(R.id.title2)
        val btnEmp = findViewById<AppCompatButton>(R.id.buttonEmpezar)

        val auth = FirebaseAuth.getInstance()

        // Inicia sesión automáticamente si hay un usuario previamente autenticado


        btnEmp.setOnClickListener {
            val currentUser: FirebaseUser? = auth.currentUser
            if (currentUser != null) {
                val intent = Intent(this, Menu::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, Main::class.java)
                startActivity(intent)
            }
        }
        icono.visibility = View.VISIBLE


        // Animación de aparición gradual
        textView.alpha = 0f
        icono.alpha = 0f
        titulo.alpha = 0f
        btnEmp.alpha = 0f

        val duration = 2000L

        // Iniciar la animación para textView
        textView.animate()
            .alpha(1f)
            .setDuration(duration)
            .withEndAction {
                // Cuando la animación de textView termina, iniciar la animación de icono
                textView.animate()
                    .alpha(0f)
                    .setDuration(duration)
                    .withEndAction {
                        // Cuando la animación de icono termina, iniciar la animación de titulo
                        icono.animate()
                            .alpha(1f)
                            .setDuration(duration)
                            .withEndAction {
                                // Continuar de manera similar para otras vistas (btnEmp, etc.)
                                titulo.animate()
                                    .alpha(1f)
                                    .setDuration(duration)
                                    .withEndAction {
                                        btnEmp.animate()
                                            .alpha(1f)
                                            .setDuration(duration)
                                            .withEndAction{
                                                startButtonAnimation(btnEmp,1000L)
                                            }
                                    }
                            }
                    }
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
}
