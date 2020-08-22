package dev.syafii.chatapps.controller.login

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import dev.syafii.chatapps.controller.home.MainActivity
import dev.syafii.chatapps.R
import dev.syafii.chatapps.Status
import dev.syafii.chatapps.controller.register.RegisterActivity
import dev.syafii.chatapps.controller.register.RegisterViewModel
import dev.syafii.chatapps.databinding.ActivityLoginBinding
import dev.syafii.chatapps.util.toast

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel


    //dialog loading
    private val dialogBuilder by lazy { AlertDialog.Builder(this)}
    private lateinit var dialogLoading : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inisialisasi view binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        setupProgressBar()
        viewClicked()
        subscribeVM()
    }

    override fun onStart() {
        super.onStart()
        checkSession()
    }

    private fun viewClicked(){
        binding.btnLogin.setOnClickListener {
            validation()
        }
        binding.tvRegister.setOnClickListener{
            Intent(this, RegisterActivity::class.java).also { intent ->
                startActivity(intent)
                finish()
            }
        }
    }

    //this is method for validation field login
    private fun validation(){
        if (binding.etEmail.text.toString().isEmpty()){
            binding.etEmail.error = "Invalid Email"
        }else{
            if (binding.etPassword.text.toString().isEmpty()){
                binding.etPassword.error = "Invalid Password"
            }else{
                viewModel.doLogin(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            }
        }
    }

    private fun subscribeVM(){
        viewModel.loginResult.observe(this) { status ->
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

    //this is for check session login or not
    private fun checkSession(){
        if (viewModel.isLogin()){
            Intent(this, MainActivity::class.java).also { intent ->
                startActivity(intent)
                toast("You already login")
                finish()
            }
        }
    }

    //method for setup progress bar or loading
    private fun setupProgressBar(){
        dialogBuilder.setView(R.layout.custom_dialog_loading)
        dialogLoading = dialogBuilder.create()
        dialogBuilder.setCancelable(false)
    }

    //method for show loading
    private fun showLoading(){
        dialogLoading.show()
    }

    //method for hide loading
    private fun hideLoading(){
        dialogLoading.dismiss()
    }
}