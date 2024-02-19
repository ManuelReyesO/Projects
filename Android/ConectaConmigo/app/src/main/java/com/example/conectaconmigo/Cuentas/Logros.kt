package com.example.conectaconmigo.Cuentas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.conectaconmigo.Menu
import com.example.conectaconmigo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Logros : AppCompatActivity() {

    private val logrosImg = listOf(
        R.drawable.animaleslogro,
        R.drawable.frutaslogro,
        R.drawable.numeroslogro,
        R.drawable.figuraslogro,
        R.drawable.memorialogro,
    )

    private val logrosTxt = listOf(
        "Maestro oyente",
        "Maestro frutero",
        "Maestro númerico",
        "Maestro geométrico",
        "Maestro memorizador"
    )

    private val listDoc = listOf(
        "Sonidos",
        "Frutas",
        "Numeros",
        "Formas",
        "Memoria"
    )

    private val listArch = listOf(
        "masterSound",
        "masterFruit",
        "masterNum",
        "masterForms",
        "masterMemory"
    )

    private var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logros)
        val buttonGames = findViewById<AppCompatButton>(R.id.juegos)
        buttonGames.setOnClickListener { Games() }
        val buttonAccount = findViewById<AppCompatButton>(R.id.cuenta)
        buttonAccount.setOnClickListener { Account() }

        setArchives()
    }

    private fun setArchives(){
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val email = currentUser?.email.toString()

        ArchVerify(email)
    }

    private fun ArchVerify(email:String){
        val imageViews = listOf(
            findViewById<ImageView>(R.id.arcImg1),
            findViewById<ImageView>(R.id.arcImg2),
            findViewById<ImageView>(R.id.arcImg3),
            findViewById<ImageView>(R.id.arcImg4),
            findViewById<ImageView>(R.id.arcImg5)
        )

        val textViews = listOf(
            findViewById<TextView>(R.id.arcTxt1),
            findViewById<TextView>(R.id.arcTxt2),
            findViewById<TextView>(R.id.arcTxt3),
            findViewById<TextView>(R.id.arcTxt4),
            findViewById<TextView>(R.id.arcTxt5)
        )


        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val email = currentUser?.email.toString()
        var value: Int = 0

        fun obtenerLogro(i: Int) {
            if (i < listDoc.size) {
                val v = listDoc[i]
                val a = listArch[i]
                db.collection("Usuarios").document(email).collection("Logros").document(v).get()
                    .addOnSuccessListener { documentSnapshot ->
                        val vals = documentSnapshot.getString(a)
                        if (vals != null) {
                            value = vals.toInt()

                            if (value == 1) {
                                imageViews[i].setBackgroundResource(logrosImg[i])
                                textViews[i].text = logrosTxt[i]
                            }
                        }
                        // Llamamos recursivamente para la siguiente iteración
                        obtenerLogro(i + 1)
                    }
                    .addOnFailureListener { exception ->
                        // Manejar errores aquí si es necesario
                        Log.e("Firestore", "Error al obtener datos", exception)

                        // Llamamos recursivamente para la siguiente iteración incluso en caso de error
                        obtenerLogro(i + 1)
                    }
            } else {
                // Después de completar todas las iteraciones
                ArchVerify(email)
            }
        }

// Llamamos a la función para la primera iteración
        obtenerLogro(0)
    }

    private fun Games(){
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
        finish()
    }

    private fun Account(){
        val intent = Intent(this,Cuenta::class.java)
        startActivity(intent)
        finish()
    }
}