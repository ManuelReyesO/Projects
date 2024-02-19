package com.example.conectaconmigo.Inicio

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.example.conectaconmigo.Menu
import com.example.conectaconmigo.R
import com.google.firebase.auth.FirebaseAuth

class InicioSesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)
        val btnInicioSesion2 = findViewById<AppCompatButton>(R.id.btnIS2)
        btnInicioSesion2.setOnClickListener { login() }
        overridePendingTransition(R.anim.bot_to_top, R.anim.no_animation)
    }

    fun succesLogin(email:String) {
        val intent = Intent(this, Menu::class.java).apply {
            putExtra("email", email)
        }
        startActivity(intent)
        finish()
    }

    fun login() {
        val email = findViewById<AppCompatEditText>(R.id.edTC2).text.toString()
        val password = findViewById<AppCompatEditText>(R.id.edTP2).text.toString()
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
        } else {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(
                    email,
                    password
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        succesLogin(email)
                    } else {
                        showAlert()
                    }
                }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Registro no efectuado")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
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