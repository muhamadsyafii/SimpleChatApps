package dev.syafii.chatapps.controller.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dev.syafii.chatapps.Status

class RegisterViewModel : ViewModel() {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val _registerResult by lazy { MutableLiveData<Status>() }
    val registerResult: LiveData<Status> get() = _registerResult

    fun doRegister(email: String, password: String) {
        _registerResult.value = Status.LOADING
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _registerResult.value = Status.SUCCESS
                } else {
                    _registerResult.value = Status.ERROR
                }
            }
    }

}