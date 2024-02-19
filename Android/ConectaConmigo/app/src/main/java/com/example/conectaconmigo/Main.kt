package com.example.conectaconmigo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.conectaconmigo.Inicio.InicioSesion
import com.example.conectaconmigo.Inicio.Registro


class Main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnInicioSesion = findViewById<AppCompatButton>(R.id.btnIS)
        btnInicioSesion.setOnClickListener {navigateLogin()}
        val btnRegistro = findViewById<AppCompatButton>(R.id.btnREG)
        btnRegistro.setOnClickListener { navigateRegis() }
    }

    fun navigateLogin(){
        val intent = Intent(this, InicioSesion::class.java)
        startActivity(intent)
    }
    fun navigateRegis(){
        val intent = Intent(this, Registro::class.java)
        startActivity(intent)
    }
}


