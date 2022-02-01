package fastcampus.aop.part3.sogetting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import fastcampus.aop.part3.sogetting.auth.IntroActivity
import fastcampus.aop.part3.sogetting.auth.UserDataModel
import fastcampus.aop.part3.sogetting.setting.SettingActivity
import fastcampus.aop.part3.sogetting.slider.CardStackAdapter
import fastcampus.aop.part3.sogetting.utils.FirebaseAuthUtils
import fastcampus.aop.part3.sogetting.utils.FirebaseRef
import java.util.Date.from

class MainActivity : AppCompatActivity() {


    private val TAG = "MainActivity"
    lateinit var cardStackAdapter: CardStackAdapter
    lateinit var manager : CardStackLayoutManager

    private val usersDataList = mutableListOf<UserDataModel>()

    private var userCount = 0

    private lateinit var currentUserGender: String

    private val uid = FirebaseAuthUtils.getUid()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 나와 다른 성별의 유저를 받아와야 하는데

        val setting = findViewById<ImageView>(R.id.settingIcon)
        setting.setOnClickListener{

//            val auth = Firebase.auth
//            auth.signOut();
//
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)

        }

        val cardStackView = findViewById<CardStackView>(R.id.cardStackView)

        manager = CardStackLayoutManager(baseContext, object: CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }
            override fun onCardSwiped(direction: Direction?) {
                if(direction == Direction.Right){
                    Toast.makeText(this@MainActivity, "rihgt", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, usersDataList[userCount].uid.toString())

                    userLikeOtherUser(uid, usersDataList[userCount].uid.toString())
                }
                if(direction == Direction.Left) {
                    Toast.makeText(this@MainActivity, "left", Toast.LENGTH_SHORT).show()
                }

                userCount += 1

                if(userCount == usersDataList.count()) {
                    getUserDataList(currentUserGender)
                    Toast.makeText(this@MainActivity, "유저 새롭게 받아옵니다.", Toast.LENGTH_LONG).show()
                }

            }
            override fun onCardRewound() {
            }
            override fun onCardCanceled() {
            }
            override fun onCardAppeared(view: View?, position: Int) {
            }
            override fun onCardDisappeared(view: View?, position: Int) {
            }
        })


        cardStackAdapter = CardStackAdapter(baseContext, usersDataList)
        cardStackView.layoutManager = manager
        cardStackView.adapter = cardStackAdapter

//        getUserDataList()
        getMyUserData()
    }

    private fun getMyUserData() {


        val postListener = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, dataSnapshot.toString())
                val data = dataSnapshot.getValue(UserDataModel::class.java)

                Log.d(TAG, data?.gender.toString())

                currentUserGender = data?.gender.toString()
                getUserDataList(data?.gender.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)
    }


    private fun getUserDataList(currentUserGender: String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    Log.d(TAG, dataModel.toString())

                    val user = dataModel.getValue(UserDataModel::class.java)

                    if(user!!.gender.toString().equals(currentUserGender)) {

                    } else {
                        usersDataList.add(user!!)
                    }



                }

                cardStackAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        FirebaseRef.userInfoRef.addValueEventListener(postListener)
    }

    // 유저의 좋아요를 표시하는 부분
    // 데이터에서 값을 저장해야 하는데, 어떤 값을 저장할까?
    // 나의 UID. 내가 좋아하는 사람의 UID
    private fun userLikeOtherUser(myUid: String, otherUid: String) {
            FirebaseRef.userLikeRef.child(myUid).child(otherUid).setValue("true")
        getOtherUserLikeList(otherUid)
    }


    private fun getOtherUserLikeList(otherUid: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    Log.e(TAG, dataModel.key.toString())
                    val likeUserKey = dataModel.key.toString()
                    if(likeUserKey.equals(uid)) {
                        Toast.makeText(this@MainActivity,"매칭 완료", Toast.LENGTH_SHORT).show()
                        createNotificationChannel()
                        sendNotification()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userLikeRef.child(otherUid).addValueEventListener(postListener)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Test_Channel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(){
        var builder = NotificationCompat.Builder(this, "Test_Channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("매칭완료")
            .setContentText("매칭이 완료되었습니다 저사람도 나를 좋아해요")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            notify(123, builder.build())
        }
    }








}