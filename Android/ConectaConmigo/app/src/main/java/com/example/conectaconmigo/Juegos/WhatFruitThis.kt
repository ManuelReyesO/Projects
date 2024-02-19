package com.example.conectaconmigo.Juegos
import android.annotation.SuppressLint
import android.content.ContentValues
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import com.example.conectaconmigo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random
import kotlin.random.nextInt

private var level = 0
private val fruitImages = mutableListOf(
    R.drawable.durazano,
    R.drawable.fresa,
    R.drawable.kiwi,
    R.drawable.limon,
    R.drawable.manzana,
    R.drawable.melon,
    R.drawable.naranja,
    R.drawable.pera,
    R.drawable.pina,
    R.drawable.sandia,
)

private val fruitImagesBN = mutableListOf(
    R.drawable.duraznobn,
    R.drawable.fresabn,
    R.drawable.kiwibn,
    R.drawable.limonbn,
    R.drawable.manzanabn,
    R.drawable.melonbn,
    R.drawable.naranjabn,
    R.drawable.perabn,
    R.drawable.pinabn,
    R.drawable.sandiabn,
)
private val random = Random
private var randomIndex = random.nextInt(fruitImages.size)
private var error: Int = 0
private var mediaPlayer: MediaPlayer? = null

class WhatFruitThis : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_what_fruit_this)
        level = 0
        error = 0
        startFruits()
    }

    private fun startFruits() {
        val timeLayout = findViewById<LinearLayoutCompat>(R.id.timeScreen)
        val starCal = findViewById<FrameLayout>(R.id.Calificacion)
        val fruit1 = findViewById<CardView>(R.id.fruit1)
        val fruit2 = findViewById<CardView>(R.id.fruit2)
        val fruit3 = findViewById<CardView>(R.id.fruit3)
        val answerFruit = findViewById<CardView>(R.id.answerFruit)
        starCal.visibility = View.INVISIBLE
        fruit1.visibility = View.INVISIBLE
        fruit2.visibility = View.INVISIBLE
        fruit3.visibility = View.INVISIBLE
        answerFruit.visibility = View.INVISIBLE

        if (level < 4) {
            cuentaAtras(timeLayout)
            Handler().postDelayed({
                setImages(fruit1, fruit2, fruit3, answerFruit)
            }, 2000)

            randomIndex = random.nextInt(fruitImages.size)
            fruitGame(fruit1, fruit2, fruit3, answerFruit)
        }
        else{
            fruit1.visibility = View.INVISIBLE
            fruit2.visibility = View.INVISIBLE
            fruit3.visibility = View.INVISIBLE
            answerFruit.visibility = View.INVISIBLE
            starsRate()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun fruitGame(fruit1:CardView, fruit2:CardView, fruit3:CardView, answerFruit:CardView){

        var isDragging = false
        var offsetX = 0f
        var offsetY = 0f
        var initialX = 0f
        var initialY = 0f

        val touchListener = View.OnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isDragging = true
                    initialX = view.x
                    initialY = view.y
                    offsetX = event.x
                    offsetY = event.y
                    view.elevation = 10f
                }
                MotionEvent.ACTION_MOVE -> {
                    if (isDragging) {
                        view.elevation = 10f
                        val newX = event.rawX - offsetX
                        val newY = event.rawY - offsetY
                        view.x = newX
                        view.y = newY
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (isDragging) {
                        isDragging = false
                        view.scaleX = 1f
                        view.scaleY = 1f
                        val x = event.rawX
                        val y = event.rawY
                        val targetLocation = IntArray(2)
                        answerFruit.getLocationOnScreen(targetLocation)
                        if (x >= targetLocation[0] && x <= targetLocation[0] + answerFruit.width &&
                            y >= targetLocation[1] && y <= targetLocation[1] + answerFruit.height) {
                            if (view.tag == answerFruit.tag) {
                                val centerX = targetLocation[0] + answerFruit.width / 2
                                val centerY = targetLocation[1] + answerFruit.height / 2
                                val deltaX = centerX - view.width / 2 - view.x
                                val deltaY = centerY - view.height / 1.45 - view.y
                                val translateAnimation = TranslateAnimation(0f, deltaX, 0f, deltaY.toFloat())
                                translateAnimation.duration = 500
                                translateAnimation.fillAfter = false
                                translateAnimation.setAnimationListener(object : Animation.AnimationListener {
                                    override fun onAnimationStart(animation: Animation) {
                                    }

                                    override fun onAnimationEnd(animation: Animation) {
                                        view.x = initialX
                                        view.y = initialY
                                        startFruits()
                                    }

                                    override fun onAnimationRepeat(animation: Animation) {
                                    }
                                })
                                view.startAnimation(translateAnimation)
                                view.elevation = answerFruit.elevation + 1f
                                mediaPlayer = MediaPlayer.create(this, R.raw.entreniveles)
                                mediaPlayer!!.start()

                                level ++

                            } else {
                                val retX = initialX - view.x
                                val rety = initialY - view.y

                                val returnAnimation = TranslateAnimation(0f, retX, 0f, rety)
                                returnAnimation.duration = 500
                                returnAnimation.fillAfter = false
                                returnAnimation.setAnimationListener(object : Animation.AnimationListener {
                                    override fun onAnimationStart(animation: Animation) {
                                    }

                                    override fun onAnimationEnd(animation: Animation) {
                                        view.x = initialX
                                        view.y = initialY
                                        view.elevation = 0f
                                    }

                                    override fun onAnimationRepeat(animation: Animation) {
                                    }
                                })
                                view.startAnimation(returnAnimation)
                                error ++
                            }
                        }
                        else {
                            view.x = initialX
                            view.y = initialY
                        }
                    }
                }
            }
            true
        }
        fruit1.setOnTouchListener(touchListener)
        fruit2.setOnTouchListener(touchListener)
        fruit3.setOnTouchListener(touchListener)
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

                    handler.postDelayed(this, 500) // 1000ms (1 segundo)
                } else {
                    timeLayout.visibility = View.INVISIBLE
                }
            }
        }

        handler.removeCallbacks(countdownRunnable)
        handler.post(countdownRunnable)
    }

    private fun setImages(fruit1:CardView, fruit2:CardView, fruit3:CardView, answerFruit:CardView){

        fruit1.visibility = View.VISIBLE
        fruit2.visibility = View.VISIBLE
        fruit3.visibility = View.VISIBLE
        answerFruit.visibility = View.VISIBLE

        val randomNumbers = mutableSetOf<Int>()

        while (randomNumbers.size < 3) {
            val randomNumber = random.nextInt(fruitImages.size)
            randomNumbers.add(randomNumber)
        }

        val (random1, random2, random3) = randomNumbers.toList()

        when(random.nextInt(1..3)){
            1 ->{
                fruit1.tag = random3
                fruit1.setBackgroundResource(fruitImages[random3])
                answerFruit.tag = random3
                answerFruit.setBackgroundResource(fruitImagesBN[random3])
                fruit2.setBackgroundResource(fruitImages[random1])
                fruit3.setBackgroundResource(fruitImages[random2])
            }
            2 ->{
                fruit2.tag = random3
                fruit2.setBackgroundResource(fruitImages[random3])
                answerFruit.tag = random3
                answerFruit.setBackgroundResource(fruitImagesBN[random3])
                fruit1.setBackgroundResource(fruitImages[random1])
                fruit3.setBackgroundResource(fruitImages[random2])
            }
            3 ->{
                fruit3.tag = random3
                fruit3.setBackgroundResource(fruitImages[random3])
                answerFruit.tag = random3
                answerFruit.setBackgroundResource(fruitImagesBN[random3])
                fruit2.setBackgroundResource(fruitImages[random1])
                fruit1.setBackgroundResource(fruitImages[random2])
            }
        }
    }

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

            db.collection("Usuarios").document(email).collection("Juegos").document("Frutas").get().addOnSuccessListener {
                val vals = it.get("vecComp").toString()
                value = vals.toInt()
                value = value + 1

                val updates = hashMapOf(
                    "vecComp" to value.toString(),
                    "estrellas" to Cal.toString()
                )
                db.collection("Usuarios").document(email).collection("Juegos").document("Frutas")
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
                "masterFruit" to "1",
            )
            db.collection("Usuarios").document(email).collection("Logros").document("Frutas")
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
            db.collection("Usuarios").document(email).collection("Logros").document("Frutas")
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