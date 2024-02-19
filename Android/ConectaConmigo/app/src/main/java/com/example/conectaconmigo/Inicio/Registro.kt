package com.example.conectaconmigo.Inicio

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.example.conectaconmigo.Main
import com.example.conectaconmigo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


private val docs = listOf(
    "Formas",
    "Frutas",
    "Memoria",
    "Numeros",
    "Sonidos"
)

private val archv = listOf(
    "masterForms",
    "masterFruit",
    "masterMemory",
    "masterNum",
    "masterSound"
)

class Registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        val btnRegistro2 = findViewById<AppCompatButton>(R.id.btnREG2)
        btnRegistro2.setOnClickListener {registro()}
        overridePendingTransition(R.anim.bot_to_top, R.anim.no_animation)
    }

    fun succesRegister() {
        val intent = Intent(this, Main::class.java)
        startActivity(intent)
        finish()
    }

    //Registro
    fun registro() {
        val correo = findViewById<AppCompatEditText>(R.id.edTC).text.toString()
        val pass = findViewById<AppCompatEditText>(R.id.edTP).text.toString()
        val db = FirebaseFirestore.getInstance()
        var i = 0

        if (correo.isEmpty() || pass.isEmpty()) {
        } else {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(
                    correo,
                    pass
                ).addOnCompleteListener {
                    if (it.isSuccessful) {

                        db.collection("Usuarios").document(correo).set(
                            hashMapOf(
                                "apellido" to "",
                                "edad" to "0",
                                "nombre" to ""
                            )
                        )
                        for(d in docs){
                            db.collection("Usuarios").document(correo).collection("Juegos").document(d).set(
                                hashMapOf(
                                    "estrellas" to "0",
                                    "vecComp" to "0"
                                )
                            )
                            db.collection("Usuarios").document(correo).collection("Logros").document(d).set(
                                hashMapOf(
                                    archv[i] to "0"
                                )
                            )
                            i += 1
                        }
                        succesRegister()
                    } else {
                        showAlert()
                    }
                }
        }
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Datos invalidos")
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog = builder.create()
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(
            R.anim.no_animation,
            R.anim.top_to_bot
        )
    }
}