package com.kernelflux.modubox.feature.login

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kernelflux.modubox.annotation.Route
import com.kernelflux.modubox.annotation.Inject
import com.kernelflux.modubox.core.api.ModuBox

/**
 * 登录页面
 */
@Route(
    path = "/login",
    description = "用户登录页面",
    requireLogin = false
)
class LoginActivity : AppCompatActivity() {
    
    @Inject(name = "LoginService")
    private lateinit var loginService: LoginService
    
    @Inject(name = "UserManager")
    private lateinit var userManager: UserManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        // 依赖注入
        injectDependencies()
        
        setupViews()
    }
    
    private fun injectDependencies() {
        // 通过ModuBox进行依赖注入
        ModuBox.getDependencyManager().inject(this)
    }
    
    private fun setupViews() {
        // 设置登录按钮点击事件
        findViewById<android.widget.Button>(R.id.btn_login)?.setOnClickListener {
            performLogin()
        }
        
        // 设置注册按钮点击事件
        findViewById<android.widget.Button>(R.id.btn_register)?.setOnClickListener {
            navigateToRegister()
        }
    }
    
    private fun performLogin() {
        val username = findViewById<android.widget.EditText>(R.id.et_username)?.text.toString()
        val password = findViewById<android.widget.EditText>(R.id.et_password)?.text.toString()
        
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show()
            return
        }
        
        val result = loginService.login(username, password)
        if (result.success) {
            // 保存用户信息
            result.userInfo?.let { userInfo ->
                userManager.setCurrentUser(userInfo.userId)
            }
            
            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
            // 登录成功后跳转到主页
            finish()
        } else {
            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun navigateToRegister() {
        // 使用路由管理器跳转到注册页面
        ModuBox.getRouterManager().navigate(this, "/register")
    }
}
