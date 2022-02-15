package com.example.scrolly.repositories

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.scrolly.models.Like
import com.example.scrolly.models.Post
import com.example.scrolly.models.User
import com.example.scrolly.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*


private const val TAG = "FirebaseRepo"

class FirebaseRepo {


    val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storageRef = Firebase.storage.reference
    private val postsCollectionRef = db.collection(Constants.POSTS)
    private val usersCollectionRef = db.collection(Constants.USERS)

    // Register with new member
    fun register(email: String, password: String) =
        firebaseAuth.createUserWithEmailAndPassword(email, password)

    // Create User Profile
    fun createProfile(user: User) {
        user.id = firebaseAuth.currentUser!!.uid
        usersCollectionRef.document(user.id).set(user)
    }

    suspend fun getUserInfo(id: String?): User? {
        var user: User? = null
        if (id != null) {
            usersCollectionRef.document(id).get().addOnSuccessListener {
                user = it.toObject(User::class.java)
            }.await()
        }
        return user
    }

    fun getPosts(): LiveData<List<Post>> {
        var posts: MutableLiveData<List<Post>> = MutableLiveData<List<Post>>()
        postsCollectionRef.get().addOnSuccessListener { snapshot ->
            posts.value = snapshot.toObjects(Post::class.java)
        }
        return posts
    }

    fun isUserLoggedIn(): Boolean {
        return !firebaseAuth.currentUser?.uid.isNullOrBlank()
    }

    suspend fun uploadProfileImage(uri: Uri): Boolean {
        var isSuccess: Boolean? = null
        val date = Date()
        val dateFormat = "dd-MMM-yyyy-hh:mm:ss"
        val dateString = android.text.format.DateFormat.format(dateFormat, date)
        val filename = "img_$dateString.jpg"
        val ref = storageRef.child("images/$filename")
        val uploadTask = ref.putFile(uri)

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                isSuccess = true
                val downloadUri = task.result
                usersCollectionRef.document(firebaseAuth.currentUser!!.uid)
                    .update("profileImgUrl", downloadUri.toString())
            } else {
                isSuccess = false
                throw IllegalStateException("uploadImage(): Failed getting download url")
            }
        }.await()
        return isSuccess!!
    }

    // Login with current user (signIn)
    fun signIn(email: String, password: String) =
        firebaseAuth.signInWithEmailAndPassword(email, password)


    // this function is to upload images to FireStore
    private fun uploadImage(postId: String, imageUri: Uri) {
//        var isSuccess: Boolean? = null
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

                postsCollectionRef.document(postId)
                    .update("postImageUrl", downloadUri.toString())
            } else {
                throw IllegalStateException("uploadImage(): Failed getting download url")
            }
        }
    }


    // upload post Info to fireStore
    suspend fun uploadPost(post: Post, postImgUri: Uri?): Boolean {
        var isSuccessful: Boolean? = null
        post.userId = firebaseAuth.currentUser!!.uid
        postsCollectionRef.document(post.id).set(post).addOnSuccessListener {
            isSuccessful = true
            postImgUri?.let {
                uploadImage(post.id, it)
            }
        }.addOnFailureListener {
            isSuccessful = false
        }.await()

        return isSuccessful!!
    }



    fun addLike(postId: String) {
        postsCollectionRef.document(postId)
            .update("likes", FieldValue.arrayUnion(Like(firebaseAuth.currentUser!!.uid)))
    }

    suspend fun deleteLike(postId: String) {
        var post = postsCollectionRef.document(postId).get().await().toObject(Post::class.java)
        val newLikes = post?.likes?.filter {
            it.userId != firebaseAuth.currentUser!!.uid
        }
        postsCollectionRef.document(postId)
            .update("likes", newLikes)
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
