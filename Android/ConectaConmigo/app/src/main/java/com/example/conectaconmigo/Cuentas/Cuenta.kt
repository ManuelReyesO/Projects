package com.example.conectaconmigo.Cuentas

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.conectaconmigo.InitialActivity
import com.example.conectaconmigo.Menu
import com.example.conectaconmigo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Cuenta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuenta)
        val buttonGames = findViewById<AppCompatButton>(R.id.juegos)
        buttonGames.setOnClickListener { Games() }
        val buttonEdit = findViewById<AppCompatButton>(R.id.editAccount)
        buttonEdit.setOnClickListener { EditAccount() }
        val buttonArch = findViewById<AppCompatButton>(R.id.buttonArch)
        buttonArch.setOnClickListener { Archivements() }
        val buttonlogout = findViewById<AppCompatButton>(R.id.buttonLogout)

        val auth = FirebaseAuth.getInstance()

        buttonlogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, InitialActivity::class.java)
            startActivity(intent)
            finish()
        }

        val currentUser = auth.currentUser
        val email = currentUser?.email.toString()
        Start(email)
    }

    private fun Start(email:String){
        val db = FirebaseFirestore.getInstance()
        val Mail = findViewById<TextView>(R.id.UserMail)
        val name = findViewById<TextView>(R.id.UserName)
        val age = findViewById<TextView>(R.id.UserAge)
        Mail.text = "Correo: " + email

        db.collection("Usuarios").document(email).get().addOnSuccessListener {
            name.text = "Nombre: " + (it.get("nombre").toString() + " " + it.get("apellido").toString())
            age.text = "Edad: " + (it.get("edad").toString())
        }
    }

    private fun Games(){
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
        finish()
    }

    private fun EditAccount(){
        val intent = Intent(this, EditarCuenta::class.java)
        startActivity(intent)
    }

    private fun Archivements(){
        val intent = Intent(this, Logros::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
        finish() // Cierra la actividad actual
        super.onBackPressed()
    }
}