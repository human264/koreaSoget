package fastcampus.aop.part3.sogetting.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fastcampus.aop.part3.sogetting.R
import fastcampus.aop.part3.sogetting.auth.IntroActivity
import fastcampus.aop.part3.sogetting.message.MyLikeListActivity
import fastcampus.aop.part3.sogetting.message.MyMsgActivity

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)


        val mybtn = findViewById<Button>(R.id.myPageBtn)

        mybtn.setOnClickListener {

            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        val myLikeBtn = findViewById<Button>(R.id.myLikeList)
        myLikeBtn.setOnClickListener {
            val intent = Intent(this, MyLikeListActivity::class.java)
            startActivity(intent)
        }


        val logoutBtn = findViewById<Button>(R.id.logOutBtn)
        logoutBtn.setOnClickListener {
            val auth = Firebase.auth
            auth.signOut()
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }

        val myMsg = findViewById<Button>(R.id.myMsg)
        logoutBtn.setOnClickListener {
            val intent = Intent(this, MyMsgActivity::class.java)
            startActivity(intent)
        }




    }
}