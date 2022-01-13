package com.example.scrolly.repositories

import android.content.Context
import android.net.Uri
import com.example.scrolly.models.Post
import com.example.scrolly.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage




private const val TAG = "FirebaseRepo"
class FirebaseRepo {


    val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val imageReferences = Firebase.storage.reference
    private val itemInfo = db.collection(Constants.ITEMS)


    // Register with new member
    fun register(email: String, password: String) =
        firebaseAuth.createUserWithEmailAndPassword(email, password)


    // Login with current user (signIn)
    fun signIn(email: String, password: String) =
        firebaseAuth.signInWithEmailAndPassword(email, password)



    // this function is to upload images to FireStore
    fun uploadImage(imageUri: Uri, filename: String) =
        imageReferences.child("images/$filename").putFile(imageUri)


    // upload Item Info to fireStore
    fun uploadItemInfo(posts: Post) = itemInfo.document().set(posts)




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
