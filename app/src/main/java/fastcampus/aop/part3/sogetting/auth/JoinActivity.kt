package fastcampus.aop.part3.sogetting.auth

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fastcampus.aop.part3.sogetting.R
import fastcampus.aop.part3.sogetting.utils.FirebaseRef
import kotlinx.android.synthetic.main.item_card.*
import java.io.ByteArrayOutputStream

class JoinActivity : AppCompatActivity() {

    private val TAG = "JoinActivity"
    private lateinit var auth: FirebaseAuth

    private var nickname = ""
    private var gender = ""
    private var city = ""
    private var age = ""
    private var uid = ""
    lateinit var profileImage : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth = Firebase.auth

        profileImage = findViewById<ImageView>(R.id.imageArea)

        //image upload
        val getAction = registerForActivityResult(
            ActivityResultContracts.GetContent()) { uri -> profileImage.setImageURI(uri) }

        profileImage.setOnClickListener {
            getAction.launch("image/*")
        }


        val joinBtn = findViewById<Button>(R.id.joinBtn)
        joinBtn.setOnClickListener {

            val email = findViewById<TextInputEditText>(R.id.emailArea)
            val pwd = findViewById<TextInputEditText>(R.id.pwdArea)


            gender = findViewById<TextInputEditText>(R.id.genderArea).text.toString()
            city = findViewById<TextInputEditText>(R.id.cityArea).text.toString()
            age = findViewById<TextInputEditText>(R.id.ageArea).text.toString()
            nickname = findViewById<TextInputEditText>(R.id.nicknameArea).text.toString()

//            Log.d(TAG, email.text.toString())
//            Log.d(TAG, pwd.text.toString())

            auth.createUserWithEmailAndPassword(email.text.toString(), pwd.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        var user = auth.currentUser
                        uid = user?.uid.toString()


                        var userModel = UserDataModel(uid, nickname, age, gender, city)

                        FirebaseRef.userInfoRef.child(uid).setValue(
                            userModel
                        )

                        uploadImage(uid)

//
//
//                        val intent = Intent(this, MainActivity::class.java)
//                        startActivity(intent)


                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)

                    }
                }


        }
    }

    private fun uploadImage(uid : String) {

        val storage = Firebase.storage
        val storageRef = storage.reference.child(uid + ".png")

        // Get the data from an ImageView as bytes
        profileImage.isDrawingCacheEnabled = true
        profileImage.buildDrawingCache()
        val bitmap = (profileImage.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }







    }





}