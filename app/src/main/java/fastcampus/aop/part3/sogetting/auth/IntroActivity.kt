package fastcampus.aop.part3.sogetting.auth


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import fastcampus.aop.part3.sogetting.R


class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val loginBtn: Button = findViewById(R.id.loginBtn)
        loginBtn.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }


        val joinBtn : Button = findViewById(R.id.joinBtnIntro)
        joinBtn.setOnClickListener {

            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)

        }

    }
}