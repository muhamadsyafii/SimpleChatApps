package dev.syafii.chatapps.controller.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import dev.syafii.chatapps.R
import dev.syafii.chatapps.databinding.ActivityMainBinding
import dev.syafii.chatapps.controller.login.LoginActivity
import dev.syafii.chatapps.util.toast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var chatAdapter: MainAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewClicked()

        viewModel.readMessageFirebase()
        subscribeVM()
        setupRecycleView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                viewModel.doLogout()
                Intent(this, LoginActivity::class.java).also { intent ->
                    startActivity(intent)
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun viewClicked() {
        binding.btnSend.setOnClickListener {
            if (binding.etChat.text.toString().isNotEmpty()) {
                viewModel.postMessage(binding.etChat.text.toString())
                binding.etChat.setText("")
            } else {
                toast("Message text can't be empty")
            }
        }
    }

    private fun setupRecycleView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        chatAdapter = MainAdapter(viewModel, viewModel.getUsername())
        binding.rvChat.layoutManager = layoutManager
        binding.rvChat.adapter = chatAdapter
    }

    fun subscribeVM() {
        viewModel.chatList.observe(this) { chats ->
            chats?.let { chatAdapter.setChatDataList(chats) }
        }
    }
}