package com.example.conectaconmigo.Juegos

import android.content.ContentValues
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.conectaconmigo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class WhatSoundAnimal : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    //lista de sonidos de animales
    private val soundList = listOf(
        R.raw.buho,
        R.raw.cerdo,
        R.raw.elefante,
        R.raw.gato,
        R.raw.grillo,
        R.raw.mono,
        R.raw.pajarito,
        R.raw.perro,
        R.raw.pollito,
        R.raw.tigre,
        R.raw.vaca,
    )

    //lista para imagenes de animales
    private val animalImage = listOf(
        R.drawable.button_buho,
        R.drawable.button_cerdo,
        R.drawable.button_elefante,
        R.drawable.button_gato,
        R.drawable.button_grillo,
        R.drawable.button_mono,
        R.drawable.button_pajarito,
        R.drawable.button_perro,
        R.drawable.button_pollito,
        R.drawable.button_tigre,
        R.drawable.button_vaca,
    )

    //Imagenes de la cuenta regresiva
    val segundos = intArrayOf(
        R.drawable.numero3,
        R.drawable.numero2,
        R.drawable.numero1,
    )

    private val random = Random
    private var randomIndex = random.nextInt(soundList.size)
    private var error: Int = 0
    private var level: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_what_sound_animal)
        level = 0
        error = 0
        startGame()
    }

    private fun startGame() {
        val timeLayout = findViewById<LinearLayoutCompat>(R.id.timeScreen)
        val starCal = findViewById<FrameLayout>(R.id.Calificacion)
        val playButton = findViewById<AppCompatButton>(R.id.buttonSound)
        val animalbtn1 = findViewById<AppCompatButton>(R.id.btnAnimal1)
        val animalbtn2 = findViewById<AppCompatButton>(R.id.btnAnimal2)
        val animalbtn3 = findViewById<AppCompatButton>(R.id.btnAnimal3)
        val animalbtn4 = findViewById<AppCompatButton>(R.id.btnAnimal4)

        starCal.visibility = View.INVISIBLE
        animalbtn1.visibility = View.INVISIBLE
        animalbtn2.visibility = View.INVISIBLE
        animalbtn3.visibility = View.INVISIBLE
        animalbtn4.visibility = View.INVISIBLE

        randomIndex = random.nextInt(soundList.size)

        //Gestion de los niveles
        if (level <= 2) {
            cuentaAtras(timeLayout)
            Handler().postDelayed({
                // Código que se ejecutará después de la pausa
                setButtons(animalbtn1, animalbtn2, animalbtn3, animalbtn4)
            }, 2000)

            playButton.setOnClickListener { playAnimal(randomIndex) }
            animalbtn1.setOnClickListener { answer(animalbtn1) }
            animalbtn2.setOnClickListener { answer(animalbtn2) }
            animalbtn3.setOnClickListener { answer(animalbtn3) }
            animalbtn4.setOnClickListener { answer(animalbtn4) }
        } else {

            starsRate()
        }
    }

    //Verificacion de respuesta
    private fun answer(button: AppCompatButton) {
        if (button.tag != null) {
            if (button.tag == randomIndex) {
                mediaPlayer!!.stop()
                level++
                button.tag = null
                mediaPlayer = MediaPlayer.create(this, R.raw.entreniveles)
                mediaPlayer!!.start()
                startGame()
            }
        }
        else{
            error++
            mediaPlayer = MediaPlayer.create(this, R.raw.incorrect)
            mediaPlayer!!.start()
        }
    }

    /*private fun setButtons(button1: AppCompatButton, button2: AppCompatButton, button3: AppCompatButton, button4: AppCompatButton) {
        button1.visibility = View.VISIBLE
        button2.visibility = View.VISIBLE
        button3.visibility = View.VISIBLE
        button4.visibility = View.VISIBLE

        val randomNumbers = mutableSetOf<Int>()

        while (randomNumbers.size < 3) {
            val randomNumber = random.nextInt(animalImage.size)
            if(randomNumber != randomIndex){randomNumbers.add(randomNumber)}

        }

        val (random1, random2, random3) = randomNumbers.toList()

        when(random.nextInt(1..4)){
            1 ->{
                button1.tag = randomIndex
                button1.setBackgroundResource(animalImage[randomIndex])
                button2.setBackgroundResource(animalImage[random1])
                button3.setBackgroundResource(animalImage[random2])
                button4.setBackgroundResource(animalImage[random3])

            }
            2 ->{
                button2.tag = randomIndex
                button2.setBackgroundResource(animalImage[randomIndex])
                button1.setBackgroundResource(animalImage[random1])
                button3.setBackgroundResource(animalImage[random2])
                button4.setBackgroundResource(animalImage[random3])

            }
            3 ->{
                button3.tag = randomIndex
                button3.setBackgroundResource(animalImage[randomIndex])
                button2.setBackgroundResource(animalImage[random1])
                button1.setBackgroundResource(animalImage[random2])
                button4.setBackgroundResource(animalImage[random3])

            }
            4 ->{
                button4.tag = randomIndex
                button4.setBackgroundResource(animalImage[randomIndex])
                button3.setBackgroundResource(animalImage[random1])
                button2.setBackgroundResource(animalImage[random2])
                button1.setBackgroundResource(animalImage[random3])
            }
        }
    }*/

    //Posicionamiento de imagenes de los botones
    private fun setButtons(vararg buttons: AppCompatButton) {
        buttons.forEach { it.visibility = View.VISIBLE }

        val randomNumbers = (0 until animalImage.size).toMutableList().apply { remove(randomIndex) }.shuffled().take(3)

        val (random1, random2, random3) = randomNumbers

        val selectedButton = buttons[random.nextInt(4)]
        selectedButton.tag = randomIndex
        selectedButton.setBackgroundResource(animalImage[randomIndex])

        val otherButtons = buttons.filter { it != selectedButton }
        otherButtons[0].setBackgroundResource(animalImage[random1])
        otherButtons[1].setBackgroundResource(animalImage[random2])
        otherButtons[2].setBackgroundResource(animalImage[random3])
    }

    //Reproduccion de audios de animales
    private fun playAnimal(index: Int) {
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
        }

        val selectedSound = soundList[index]
        mediaPlayer = MediaPlayer.create(this, selectedSound)

        mediaPlayer!!.start()
    }

    private fun cuentaAtras(timeLayout: View) {
        val handler = Handler()
        var count = 4
        val imageSeg = findViewById<ImageView>(R.id.segundos)
        timeLayout.visibility = View.VISIBLE

        val countdownRunnable = object : Runnable {
            override fun run() {
                if (count >= 1) {
                    val drawableId = when (count) {
                        4 -> R.drawable.numero3
                        3 -> R.drawable.numero2
                        2 -> R.drawable.numero1
                        else -> R.drawable.ya
                    }
                    imageSeg.setImageResource(drawableId)
                    count--

                    handler.postDelayed(this, 500)
                } else {
                    playAnimal(randomIndex)
                    timeLayout.visibility = View.INVISIBLE
                }
            }
        }

        handler.removeCallbacks(countdownRunnable)
        handler.post(countdownRunnable)
    }

    //Medicion de desempeño del jugador
    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis = 1000
    private val starFilledRes = R.drawable.calest
    private val stars: List<ImageView> by lazy {
        listOf(findViewById(R.id.star1), findViewById(R.id.star2), findViewById(R.id.star3))
    }

    private fun starsRate() {
        val starCal = findViewById<FrameLayout>(R.id.Calificacion)
        starCal.visibility = View.VISIBLE
        starCal.elevation = 10F
        val btnStar = findViewById<AppCompatButton>(R.id.btnStars)
        var Cal = 0

        mediaPlayer = MediaPlayer.create(this, R.raw.nivelcompleto)
        mediaPlayer!!.start()

        if (error == 0){Cal = 3}
        if(error in 1..3){Cal = 2}
        if(error > 3){Cal = 1}

        for ((index, star) in stars.withIndex()) {
            handler.postDelayed({
                runOnUiThread {
                    // Cambia la imagen de la estrella actual
                    if (index < Cal) {
                        star.setImageResource(starFilledRes)
                    }
                }
            }, (index + 1) * delayMillis.toLong())
        }


        btnStar.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            val email = currentUser?.email.toString()
            val db = FirebaseFirestore.getInstance()
            var value:Int = 0

            db.collection("Usuarios").document(email).collection("Juegos").document("Sonidos").get().addOnSuccessListener {
                val vals = it.get("vecComp").toString()
                value = vals.toInt()
                value = value + 1

                val updates = hashMapOf(
                    "vecComp" to value.toString(),
                    "estrellas" to Cal.toString()
                )
                db.collection("Usuarios").document(email).collection("Juegos").document("Sonidos")
                    .update(updates as Map<String, Any>)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            logroVerify(value, Cal)
                            finish()
                        } else {
                            Log.w(ContentValues.TAG, "Error updating document", task.exception)
                        }
                    }
            }
        }
    }

    private fun logroVerify(value:Int, Cal:Int){
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val email = currentUser?.email.toString()

        if(value==10) {
            val updates = hashMapOf(
                "masterSound" to "1",
            )
            db.collection("Usuarios").document(email).collection("Logros").document("Sonidos")
                .update(updates as Map<String, Any>)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                    } else {
                        Log.w(ContentValues.TAG, "Error updating document", task.exception)
                    }
                }
        }
        else {}

        if(Cal == 3){
            val updates = hashMapOf(
                "starMax" to "1",
            )
            db.collection("Usuarios").document(email).collection("Logros").document("Sonidos")
                .update(updates as Map<String, Any>)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                    } else {
                        Log.w(ContentValues.TAG, "Error updating document", task.exception)
                    }
                }
        }
    }
}