package fastcampus.aop.part3.sogetting.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fastcampus.aop.part3.sogetting.R
import fastcampus.aop.part3.sogetting.auth.UserDataModel
import fastcampus.aop.part3.sogetting.message.fcm.NotiModel
import fastcampus.aop.part3.sogetting.message.fcm.PushNotification
import fastcampus.aop.part3.sogetting.message.fcm.RetrofitInstance
import fastcampus.aop.part3.sogetting.utils.FirebaseAuthUtils
import fastcampus.aop.part3.sogetting.utils.FirebaseRef
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MyLikeListActivity : AppCompatActivity() {

    private val TAG = "MyLikeListActivity"
    private val uid = FirebaseAuthUtils.getUid()

    private val likeUserListUid = mutableListOf<String>()
    private val likeUserList = mutableListOf<UserDataModel>()

    lateinit var listViewAdapter : ListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_like_list)

        val userListView = findViewById<ListView>(R.id.userListView)

        listViewAdapter = ListViewAdapter(this, likeUserList)
        userListView.adapter = listViewAdapter

        getUserDataList()

        getMyLikeList()

        userListView.setOnItemClickListener { parent, view, position, id ->
//
//            Log.d(TAG, likeUserList[position].uid.toString())
            checkMatching(likeUserList[position].uid.toString())

            val notiModel = NotiModel("a", "b")

            val pushModel = PushNotification(notiModel,
                likeUserList[position].token.toString())

            testPush(pushModel)
        }
    }

    private fun checkMatching(otherUid: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                Log.d(TAG, otherUid)
                Log.e(TAG, dataSnapshot.toString())

                if(dataSnapshot.children.count() == 0) {
                    Toast.makeText(this@MyLikeListActivity,"매칭이 되지 않았습니다..", Toast.LENGTH_LONG).show()
                } else {
                    for (dataModel in dataSnapshot.children) {

                        val likeUserKey = dataModel.key.toString()
                        if (likeUserKey.equals(uid)) {
                            Toast.makeText(this@MyLikeListActivity, "매칭이 되었습니다.", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            Toast.makeText(
                                this@MyLikeListActivity,
                                "매칭이 되지 않았습니다..",
                                Toast.LENGTH_LONG
                            ).show()
                        }

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




    private fun getMyLikeList() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
//                    Log.d(TAG, dataModel.key.toString())
                    //내가 좋아요 한 사람들의 UID가 LIKE 에 들어 있음.
                    likeUserListUid.add(dataModel.key.toString())
                }
                getUserDataList()

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userLikeRef.child(uid).addValueEventListener(postListener)
    }

    private fun getUserDataList() {
        val postListener = object:ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    val user = dataModel.getValue(UserDataModel::class.java)
                    if(likeUserListUid.contains(user?.uid)) {
                        // 내가 좋아요 한 사람들의 정보만 뽑아 올 수 있음.


                        likeUserList.add(user!!)
                    }
//                    Log.d(TAG, user.toString())
                }

                listViewAdapter.notifyDataSetChanged()
                Log.d(TAG, likeUserList.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        FirebaseRef.userInfoRef.addValueEventListener(postListener)
    }

    //PUSH

    private fun testPush(notification : PushNotification)
        = CoroutineScope(Dispatchers.IO).launch {

            RetrofitInstance.api.postNotification(notification)

    }



}