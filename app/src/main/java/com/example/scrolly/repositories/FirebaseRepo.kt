package com.example.scrolly.repositories

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.scrolly.models.Post
import com.example.scrolly.models.User
import com.example.scrolly.utils.Constants
import com.example.scrolly.utils.showSnackBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*


private const val TAG = "FirebaseRepo"

class FirebaseRepo {


    val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storageRef = Firebase.storage.reference
    private val postsCollectionRef = db.collection(Constants.POST)
    private val usersCollectionRef = db.collection(Constants.USER)


    // Register with new member
    fun register(email: String, password: String) = firebaseAuth.createUserWithEmailAndPassword(email, password)

    // Create User Profile
    fun createProfile(user: User) = usersCollectionRef.document(user.id).set(user)

    // Login with current user (signIn)
    fun signIn(email: String, password: String) =
        firebaseAuth.signInWithEmailAndPassword(email, password)


    // this function is to upload images to FireStore
    private suspend fun uploadImage(postId: String, imageUri: Uri) {
        val date = Date()
        val dateFormat = "dd-MMM-yyyy-hh:mm:ss"
        val dateString = android.text.format.DateFormat.format(dateFormat, date)
        val filename = "img_$dateString.jpg"
        val ref = storageRef.child("images/$filename")
        val uploadTask = ref.putFile(imageUri)

        val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
            ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    postsCollectionRef.document(postId).update("postImageUrl", downloadUri.toString())
                } else {
                    throw IllegalStateException("uploadImage(): Failed getting download url")
                }
            }.await()
    }


    // upload post Info to fireStore
    suspend fun uploadPost(post: Post, postImgUri: Uri?): Boolean {
        var isSuccessful: Boolean? = null

        postsCollectionRef.document(post.id).set(post).addOnSuccessListener {
            isSuccessful = true
            postImgUri?.let { uri ->
                CoroutineScope(IO).launch {
                     uploadImage(post.id, uri)
                }
            }
        }.addOnFailureListener {
            isSuccessful = false
        }.await()

        return isSuccessful!!
    }

    fun getPosts(): LiveData<List<Post>> {
        var posts: MutableLiveData<List<Post>> = MutableLiveData<List<Post>>()
        postsCollectionRef.get().addOnSuccessListener { snapshot ->
            posts.value = snapshot.toObjects(Post::class.java)
        }
        Log.d(TAG, "getPosts: $posts")
        return posts
    }

    companion object {
        private var instance: FirebaseRepo? = null

        fun init(context: Context) {
            if (instance == null) {
                instance = FirebaseRepo()
            }
        }

        fun get(): FirebaseRepo {
            return instance ?: throw Exception("Firebase service repo must be initialized")
        }
    }


}
