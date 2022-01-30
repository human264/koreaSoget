package fastcampus.aop.part3.sogetting.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import fastcampus.aop.part3.sogetting.R
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        joinBtn.setOnClickListener {
            var intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }
    }
}