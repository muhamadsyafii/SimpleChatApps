package dev.syafii.chatapps.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.syafii.chatapps.model.ChatModel

class MainViewModel : ViewModel() {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val databasePreference by lazy { FirebaseDatabase.getInstance().reference }
    private lateinit var valueEventListener: ValueEventListener

    private val _chatList by lazy { MutableLiveData<List<ChatModel>>() }
    val chatList: LiveData<List<ChatModel>>
        get() = _chatList

    private val MESSAGE = "message"
    private val TAG = "MainViewModel"

    fun doLogout() {
        firebaseAuth.signOut()
    }

    fun getUsername(): String {
        val email = firebaseAuth.currentUser?.email.orEmpty()
        return email.split("@").first()
    }

    fun postMessage(message: String) {
        val chat = ChatModel(getUsername(), message)
        Log.e(TAG, "$chat")
        databasePreference.child(MESSAGE).push().setValue(chat)
    }

    fun readMessageFirebase() {
        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataChat = snapshot.children.toMutableList().map { child ->
                    val chat = child.getValue(ChatModel::class.java)
                    chat?.firebaseKey = child.key
                    chat ?: ChatModel()

                }
                _chatList.value = dataChat
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, error.message)
            }

        }
        databasePreference.child(MESSAGE).addValueEventListener(valueEventListener)
    }


}