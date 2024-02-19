package com.example.conectaconmigo.Juegos

import android.content.ContentValues
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
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

private val random = Random

private val twinsImages = mutableListOf(
    R.drawable.buho,
    R.drawable.cerdo,
    R.drawable.elefante,
    R.drawable.gato,
    R.drawable.grillo,
    R.drawable.mono,
    R.drawable.pajarito,
    R.drawable.perro,
    R.drawable.pollito,
    R.drawable.tigre,
    R.drawable.vaca,
)

private var mediaPlayer: MediaPlayer? = null

class TwinsGame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_twins_game)
        val cards = mutableListOf<ImageView>()

        val niv = findViewById<FrameLayout>(R.id.NivComp)
        niv.visibility = View.INVISIBLE

        for (i in 1..10) {
            val resId = resources.getIdentifier("card$i", "id", packageName)
            val card = findViewById<ImageView>(resId)
            cards.add(card)
        }

        setTwins(cards)
        startGame(cards)
    }

    //Metodo para acomodar las parejas
    private fun setTwins(cards: MutableList<ImageView>) {
        val randomNumbers = mutableSetOf<Int>()
        val timeLayout = findViewById<LinearLayoutCompat>(R.id.timeScreen)
        timeLayout.visibility=View.INVISIBLE


        while (randomNumbers.size < 5) {
            val randomNumber = random.nextInt(twinsImages.size)
            randomNumbers.add(randomNumber)
        }

        val randomTwins = randomNumbers.toList()

        for (i in 0 until 10) {
            if (i < 4) {
                cards[i].tag = randomTwins[i]
            } else {
                cards[i].tag = randomTwins[i % 5]
            }
        }

        val handler = Handler()

        for (imageView in cards){
            val drawableId = twinsImages[imageView.tag as Int]
            imageView.animate()
                .rotationYBy(180f)
                .setDuration(500)
                .withEndAction {
                    imageView.setBackgroundResource(drawableId)
                }
                .start()
        }

        handler.postDelayed({
            for (imageView in cards) {
                imageView.animate()
                    .rotationYBy(180f)
                    .setDuration(500)
                    .withEndAction {
                        imageView.setBackgroundResource(R.drawable.backcard)
                    }
                    .start()
            }
            timeLayout.visibility=View.VISIBLE
            cuentaAtras(timeLayout)
        }, 1500)
    }

    private var card1: ImageView? = null
    private var card2: ImageView? = null


    //Animaciones de las tarjetas
    private fun startGame(cards: MutableList<ImageView>) {
        for (imageView in cards) {
            imageView.setOnClickListener {
                if (card1 == null && card2 == null) {
                    val drawableId = twinsImages[imageView.tag as Int]

                    // Realiza la animación de volteo
                    imageView.animate()
                        .rotationYBy(180f)
                        .setDuration(500)
                        .withEndAction {
                            imageView.setBackgroundResource(drawableId)
                        }
                        .start()

                    card1 = imageView
                } else if (card1 != null && card2 == null && card1 != imageView) {
                    val drawableId = twinsImages[imageView.tag as Int]

                    imageView.animate()
                        .rotationYBy(180f)
                        .setDuration(500)
                        .withEndAction {
                            imageView.setBackgroundResource(drawableId)
                            Handler().postDelayed({
                                verifyAnswer(cards)
                            }, 500)
                        }
                        .start()
                    card2 = imageView
                }
            }
        }
    }

    //Verificador de las parejas
    private fun verifyAnswer(cards: MutableList<ImageView>) {
        if (card1 != null && card2 != null) {
            val tag1 = card1?.tag as Int
            val tag2 = card2?.tag as Int

            if (tag1 == tag2) {
                card1?.visibility = View.INVISIBLE
                card2?.visibility = View.INVISIBLE
            } else {
                card1?.setBackgroundResource(R.drawable.backcard)
                card2?.setBackgroundResource(R.drawable.backcard)

            }
            card1 = null
            card2 = null
        }

        var todasInvisibles = true
        for (i in 0 until  10) {
            if (cards[i].visibility != View.INVISIBLE) {
                todasInvisibles = false
                break
            }
        }

        if (todasInvisibles) {
            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            val email = currentUser?.email.toString()
            val db = FirebaseFirestore.getInstance()
            var value:Int = 0

            mediaPlayer = MediaPlayer.create(this, R.raw.nivelcompleto)
            mediaPlayer!!.start()

            db.collection("Usuarios").document(email).collection("Juegos").document("Memoria").get().addOnSuccessListener {
                val vals = it.get("vecComp").toString()
                value = vals.toInt()
                value = value + 1

                val updates = hashMapOf(
                    "vecComp" to value.toString()
                )
                db.collection("Usuarios").document(email).collection("Juegos").document("Memoria")
                    .update(updates as Map<String, Any>)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            logroVerify(value)
                        } else {
                            Log.w(ContentValues.TAG, "Error updating document", task.exception)
                        }
                    }
            }
            val niv = findViewById<FrameLayout>(R.id.NivComp)
            val btnNiv = findViewById<AppCompatButton>(R.id.btnNivComp)
            niv.visibility = View.VISIBLE
            btnNiv.setOnClickListener {
                finish()
            }
        }
        else {
        }
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

    private fun logroVerify(value:Int){
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val email = currentUser?.email.toString()

        if(value==10) {
            val updates = hashMapOf(
                "masterMemory" to "1",
            )
            db.collection("Usuarios").document(email).collection("Logros").document("Memoria")
                .update(updates as Map<String, Any>)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                    } else {
                        Log.w(ContentValues.TAG, "Error updating document", task.exception)
                    }
                }
        }
        else {}
    }
}