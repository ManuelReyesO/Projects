package com.example.conectaconmigo.Juegos

import android.content.ContentValues
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.conectaconmigo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random
import kotlin.random.nextInt

private val numberImage = listOf(
    R.drawable.uno,
    R.drawable.dos,
    R.drawable.tres,
    R.drawable.cuatro,
    R.drawable.cinco,
    R.drawable.seis,
    R.drawable.siete,
    R.drawable.ocho,
    R.drawable.nueve,
    R.drawable.diez,
)

private val numbers = listOf(
    "Uno",
    "Dos",
    "Tres",
    "Cuatro",
    "Cinco",
    "Seis",
    "Siete",
    "Ocho",
    "Nueve",
    "Diez"
)

private val random = Random
private var error: Int = 0
private var level: Int = 0
private var mediaPlayer: MediaPlayer? = null

class NumberGame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number_game)
        level = 0
        error = 0
        startNumbers()
    }

    private fun startNumbers() {
        val timeLayout = findViewById<LinearLayoutCompat>(R.id.timeScreen)
        val button1 = findViewById<AppCompatButton>(R.id.buttonNumber1)
        val button2 = findViewById<AppCompatButton>(R.id.buttonNumber2)
        val button3 = findViewById<AppCompatButton>(R.id.buttonNumber3)
        val starCal = findViewById<FrameLayout>(R.id.Calificacion)
        val number = findViewById<ImageView>(R.id.number)

        button1.visibility = View.INVISIBLE
        button2.visibility = View.INVISIBLE
        button3.visibility = View.INVISIBLE
        number.visibility = View.INVISIBLE
        starCal.visibility = View.INVISIBLE


        if (level <= 2) {
            cuentaAtras(timeLayout)
            Handler().postDelayed({
                // Código que se ejecutará después de la pausa
                setNumbers(button1, button2, button3, number)
                startGame(button1, button2, button3, number)
            }, 2000)
        } else {
            starsRate()
        }
    }

    private fun setNumbers(
        button1: AppCompatButton,
        button2: AppCompatButton,
        button3: AppCompatButton,
        number: ImageView
    ) {
        button1.visibility = View.VISIBLE
        button2.visibility = View.VISIBLE
        button3.visibility = View.VISIBLE
        number.visibility = View.VISIBLE
        val randomNumbers = mutableSetOf<Int>()

        while (randomNumbers.size < 3) {
            val randomNumber = random.nextInt(numbers.size)
            randomNumbers.add(randomNumber)
        }

        val (random1, random2, random3) = randomNumbers.toList()

        number.setBackgroundResource(numberImage[random1])
        number.tag = random1

        when (random.nextInt(1..3)) {
            1 -> {
                button1.text = numbers[random1]
                button1.tag = random1
                button2.text = numbers[random2]
                button3.text = numbers[random3]
            }

            2 -> {
                button2.text = numbers[random1]
                button2.tag = random1
                button1.text = numbers[random2]
                button3.text = numbers[random3]
            }

            3 -> {
                button3.text = numbers[random1]
                button3.tag = random1
                button2.text = numbers[random2]
                button1.text = numbers[random3]
            }
        }
    }

    //Gestion de espuestas de los botones
    private fun startGame(
        button1: AppCompatButton,
        button2: AppCompatButton,
        button3: AppCompatButton,
        number: ImageView
    ) {
        fun checkButtonTag(button: Button) {
            if (button.tag == number.tag) {
                level++
                button.tag = null
                mediaPlayer = MediaPlayer.create(this, R.raw.entreniveles)
                mediaPlayer!!.start()
                startNumbers()
            } else {
                error++
                mediaPlayer = MediaPlayer.create(this, R.raw.incorrect)
                mediaPlayer!!.start()
            }
        }
        button1.setOnClickListener { checkButtonTag(button1) }
        button2.setOnClickListener { checkButtonTag(button2) }
        button3.setOnClickListener { checkButtonTag(button3) }
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
                    timeLayout.visibility = View.INVISIBLE
                }
            }
        }
        handler.post(countdownRunnable)
    }

    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis = 1000 // Tiempo en milisegundos antes de que cambien las imágenes
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

        if (error == 0) {
            Cal = 3
        }
        if (error in 1..3) {
            Cal = 2
        }
        if (error > 3) {
            Cal = 1
        }

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

            db.collection("Usuarios").document(email).collection("Juegos").document("Numeros").get().addOnSuccessListener {
                val vals = it.get("vecComp").toString()
                value = vals.toInt()
                value = value + 1

                val updates = hashMapOf(
                    "vecComp" to value.toString(),
                    "estrellas" to Cal.toString()
                )
                db.collection("Usuarios").document(email).collection("Juegos").document("Numeros")
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
                "masterNum" to "1",
            )
            db.collection("Usuarios").document(email).collection("Logros").document("Numeros")
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
            db.collection("Usuarios").document(email).collection("Logros").document("Numeros")
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