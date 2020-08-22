package dev.syafii.chatapps.controller.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dev.syafii.chatapps.Status

class LoginViewModel : ViewModel(){

    private val firebaseAuth by lazy {FirebaseAuth.getInstance()}
    private val _loginResult by lazy { MutableLiveData<Status>() }
    val loginResult: LiveData<Status> get() = _loginResult

    fun isLogin(): Boolean{
        val session = firebaseAuth.currentUser
        return session != null
    }

    fun doLogin(email: String, password: String){
        _loginResult.value = Status.LOADING
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{result ->
                if (result.isSuccessful){
                    _loginResult.value = Status.SUCCESS
                }else{
                    _loginResult.value = Status.ERROR
                }
            }
    }
}
