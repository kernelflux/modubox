package com.kernelflux.modubox.feature.login

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kernelflux.modubox.annotation.Route
import com.kernelflux.modubox.annotation.Inject
import com.kernelflux.modubox.core.api.ModuBox

/**
 * 注册页面
 */
@Route(
    path = "/register",
    description = "用户注册页面",
    requireLogin = false
)
class RegisterActivity : AppCompatActivity() {
    
    @Inject(name = "LoginService")
    private lateinit var loginService: LoginService
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        
        // 依赖注入
        injectDependencies()
        
        setupViews()
    }
    
    private fun injectDependencies() {
        // 通过ModuBox进行依赖注入
        ModuBox.getDependencyManager().inject(this)
    }
    
    private fun setupViews() {
        // 设置注册按钮点击事件
        findViewById<android.widget.Button>(R.id.btn_register)?.setOnClickListener {
            performRegister()
        }
        
        // 设置返回登录按钮点击事件
        findViewById<android.widget.Button>(R.id.btn_back_to_login)?.setOnClickListener {
            finish()
        }
    }
    
    private fun performRegister() {
        val username = findViewById<android.widget.EditText>(R.id.et_username)?.text.toString()
        val email = findViewById<android.widget.EditText>(R.id.et_email)?.text.toString()
        val password = findViewById<android.widget.EditText>(R.id.et_password)?.text.toString()
        val confirmPassword = findViewById<android.widget.EditText>(R.id.et_confirm_password)?.text.toString()
        
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (password != confirmPassword) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show()
            return
        }
        
        // 使用登录服务进行注册
        val result = loginService.register(username, password)
        if (result.success) {
            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
        }
    }
}
