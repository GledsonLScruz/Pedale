package com.gledsonluan.pedale

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.gledsonluan.pedale.data.UserConfig
import com.gledsonluan.pedale.model.Tour
import com.gledsonluan.pedale.viewmodel.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var mUserViewModel: UserViewModel
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var navController: NavController

    private val db = Firebase.firestore

    private lateinit var alldatafromdblocal : List<Tour>

    companion object {
        private const val RC_SIGN_IN = 120
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1500)

        setTheme(R.style.Theme_Pedal)

        setContentView(R.layout.activity_main)


        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // getting the value of gso inside the GoogleSigninClient
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        mUserViewModel.readAllTours.observe(this, Observer { allData ->
            alldatafromdblocal = allData
        })
    }

    override fun onStart() {
        super.onStart()
        checkAuth()
    }

    //Function that check if the user is alredy authenticated
    fun checkAuth() {
        val CurrentUser = FirebaseAuth.getInstance().currentUser

        if(CurrentUser == null) {
            navController.navigate(R.id.loginFragment)
        } else {
            UserConfig.id = CurrentUser.uid.toString()
            UserConfig.email = CurrentUser.email.toString()
            UserConfig.name = CurrentUser.displayName.toString()
            UserConfig.pic = CurrentUser.photoUrl.toString()
        }
    }

        fun continueWithGoogle() {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(ContentValues.TAG, "firebaseAuthWithGoogle:" + account.id)
                    authWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(ContentValues.TAG, "Google sign in failed", e)
                }
            }
        }

        private fun authWithGoogle(idToken: String) {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(ContentValues.TAG, "signInWithCredential:success")
                        UserConfig.email = FirebaseAuth.getInstance().currentUser!!.email.toString()
                        UserConfig.id = FirebaseAuth.getInstance().currentUser!!.uid
                        UserConfig.name = FirebaseAuth.getInstance().currentUser!!.displayName.toString()
                        UserConfig.pic = FirebaseAuth.getInstance().currentUser!!.photoUrl.toString()

                        val user = hashMapOf(
                            "name" to UserConfig.name,
                            "email" to UserConfig.email,
                            "pic" to UserConfig.pic,
                            "data" to emptyMap<String,String>()
                        )

                        db.collection("users").document(UserConfig.id)
                            .set(user)
                            .addOnSuccessListener {
                                //db.collection("users").document(UserConfig.id).collection("data").add(tour("0"))
                                Log.d("Firebase", "DocumentSnapshot successfully written!") }
                            .addOnFailureListener { e -> Log.w("Firebase", "Error writing document", e) }

                        navController.navigateUp()

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("ERROR", "signInWithCredential:failure", task.exception)
                        Toast.makeText(
                            this,
                            "Erro ao continuar com Google",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

    fun logoutGoogle() {
            googleSignInClient.signOut().addOnCompleteListener {
                FirebaseAuth.getInstance().signOut()
                navController.navigate(R.id.action_profileFragment_to_loginFragment2)
                UserConfig.email = "EMAIL"
                UserConfig.id = "ID"
                UserConfig.name = "NAME"
                UserConfig.pic = "PIC"}
    }

    fun viewmodel(): UserViewModel {
            return mUserViewModel
        }

    fun db(): FirebaseFirestore {
        return db
    }
}