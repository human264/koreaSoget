package fastcampus.aop.part3.sogetting.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fastcampus.aop.part3.sogetting.R
import fastcampus.aop.part3.sogetting.utils.FirebaseAuthUtils
import fastcampus.aop.part3.sogetting.utils.FirebaseRef

class MyLikeListActivity : AppCompatActivity() {

    private val TAG = "MyLikeListActivity"
    private val uid = FirebaseAuthUtils.getUid()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_like_list)


        getOtherUserLikeList()
    }

    private fun getOtherUserLikeList() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    Log.d(TAG, dataModel.key.toString())



                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userLikeRef.child(uid).addValueEventListener(postListener)
    }
}