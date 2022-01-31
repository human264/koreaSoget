package fastcampus.aop.part3.sogetting.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fastcampus.aop.part3.sogetting.MainActivity
import fastcampus.aop.part3.sogetting.R

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val loginBtn = findViewById<Button>(R.id.loginBtnLogin)

        loginBtn.setOnClickListener {
            val email = findViewById<TextInputEditText>(R.id.emailAreaLogin)
            val pwd = findViewById<TextInputEditText>(R.id.pwdAreaLogin)

            auth.signInWithEmailAndPassword(email.text.toString(), pwd.text.toString())
                .addOnCompleteListener(this) {task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "실패", Toast.LENGTH_LONG).show()
                    }
                }
        }






    }
}