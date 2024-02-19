package com.example.conectaconmigo.Cuentas

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.conectaconmigo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditarCuenta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_cuenta)

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val email = currentUser?.email.toString()
        Start(email)
    }



    private fun Start(email:String){
        val buttonSave = findViewById<AppCompatButton>(R.id.buttonSave)
        val db = FirebaseFirestore.getInstance()
        val name = findViewById<EditText>(R.id.editTNom)
        val age = findViewById<EditText>(R.id.editTAge)
        val lastN = findViewById<EditText>(R.id.editTApe)

        db.collection("Usuarios").document(email).get().addOnSuccessListener {
            name.setText(it.get("nombre") as String?)
            lastN.setText(it.get("apellido") as String?)
            age.setText((it.get("edad") as String?))
        }
        buttonSave.setOnClickListener { SaveProfile(email) }
    }

    private fun SaveProfile(email:String){
        val name = findViewById<EditText>(R.id.editTNom)
        val age = findViewById<EditText>(R.id.editTAge)
        val lastN = findViewById<EditText>(R.id.editTApe)
        val db = FirebaseFirestore.getInstance()
        val updates = hashMapOf(
            "nombre" to name.text.toString(),
            "apellido" to lastN.text.toString(),
            "edad" to age.text.toString()
            // Puedes agregar más campos según sea necesario
        )
        db.collection("Usuarios").document(email)
            .update(updates as Map<String, Any>)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this,Cuenta::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Error al actualizar
                    Log.w(TAG, "Error updating document", task.exception)
                }
            }
    }
}