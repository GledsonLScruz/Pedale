package com.example.pedal

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.pedal.viewmodel.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var mUserViewModel : UserViewModel
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient

    private lateinit var navController : NavController

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


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        checkAuth()
    }

    override fun onStart() {
        super.onStart()
        checkAuth()
    }

    //Function that check if the user is alredy authenticated
    private fun checkAuth() {
        val GoogleUser = GoogleSignIn.getLastSignedInAccount(this)

        when {
            GoogleUser != null -> {
                UserConfig.id = GoogleUser.id.toString()
                UserConfig.email = GoogleUser.email.toString()
                navController.navigate(R.id.listFragment)
            }
        }
    }

    fun continueWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)
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

                    navController.navigate(R.id.listFragment)

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

    fun logoutGoogle(){
        googleSignInClient.signOut().addOnCompleteListener {
            FirebaseAuth.getInstance().signOut()
            navController.navigate(R.id.loginFragment)


            //TODO: Implement a redefine methot that delete the data from previous user
            //UserConfig.redefine()
        }
    }

    fun viewmodel() :UserViewModel{
        return mUserViewModel
    }

}