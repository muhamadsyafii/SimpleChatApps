package dev.syafii.chatapps.controller.register

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import dev.syafii.chatapps.R
import dev.syafii.chatapps.controller.home.MainActivity
import dev.syafii.chatapps.Status
import dev.syafii.chatapps.controller.login.LoginActivity
import dev.syafii.chatapps.databinding.ActivityRegisterBinding
import dev.syafii.chatapps.util.toast

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel : RegisterViewModel


    //dialog loading
    private val dialogBuilder by lazy { AlertDialog.Builder(this) }
    private lateinit var dialogLoading: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        subscribeVM()
        setupProgressBar()
        viewClicked()
    }

    private fun viewClicked(){
        binding.btnLRegister.setOnClickListener {
            validation()
        }
        binding.tvLogin.setOnClickListener{
            Intent(this, LoginActivity::class.java).also { intent ->
                startActivity(intent)
                finish()
            }
        }
    }


    //this is method for validation field login
    private fun validation() {
        if (binding.etEmail.text.toString().isEmpty()) {
            binding.etEmail.error = "Invalid Email"
        } else {
            if (binding.etPassword.text.toString().isEmpty()) {
                binding.etPassword.error = "Invalid Password"
            } else {
                viewModel.doRegister(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            }
        }
    }

    private fun subscribeVM(){
        viewModel.registerResult.observe(this) { status ->
            status?.let {stats ->
                when(stats){
                    Status.LOADING -> {
                        showLoading()
                    }
                    Status.ERROR -> {
                        hideLoading()
                        toast("Wrong Password and Email")
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        Intent(this, MainActivity::class.java).also { intent ->
                            startActivity(intent)
                            toast("Welcome")
                            finish()
                        }
                    }
                }
            }
        }
    }

    //method for setup progress bar or loading
    private fun setupProgressBar() {
        dialogBuilder.setView(R.layout.custom_dialog_loading)
        dialogLoading = dialogBuilder.create()
        dialogBuilder.setCancelable(false)
    }

    //method for show loading
    private fun showLoading() {
        dialogLoading.show()
    }

    //method for hide loading
    private fun hideLoading() {
        dialogLoading.dismiss()
    }

}