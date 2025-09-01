package com.kernelflux.moduboxsample

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kernelflux.modubox.core.impl.AutoModuBoxHelper

class MainActivity : AppCompatActivity() {
    
    private lateinit var tvStatus: TextView
    private lateinit var btnStartLogin: Button
    private lateinit var btnStopLogin: Button
    private lateinit var btnNavigate: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initViews()
        setupListeners()
        updateStatus()
    }
    
    private fun initViews() {
        tvStatus = findViewById(R.id.tv_status)
        btnStartLogin = findViewById(R.id.btn_start_login)
        btnStopLogin = findViewById(R.id.btn_stop_login)
        btnNavigate = findViewById(R.id.btn_navigate)
    }
    
    private fun setupListeners() {
        btnStartLogin.setOnClickListener {
            AutoModuBoxHelper.getInstanceOrThrow().getPluginManager().startPlugin("login")
            updateStatus()
        }
        
        btnStopLogin.setOnClickListener {
            AutoModuBoxHelper.getInstanceOrThrow().getPluginManager().stopPlugin("login")
            updateStatus()
        }
        
        btnNavigate.setOnClickListener {
            // 使用自动注册的路由导航到登录页面
            AutoModuBoxHelper.getInstanceOrThrow().getRouterManager().navigate(this, "/login")
        }
    }
    
    private fun updateStatus() {
        val moduBox = AutoModuBoxHelper.getInstanceOrThrow()
        val pluginManager = moduBox.getPluginManager()
        val loginPlugin = pluginManager.getPlugin("login")
        val isLoaded = pluginManager.isPluginLoaded("login")
        val isRunning = pluginManager.isPluginRunning("login")
        val state = loginPlugin?.getState()
        
        // 获取注册信息
        val registrationInfo = AutoModuBoxHelper.getRegistrationInfo()
        val pluginCount = (registrationInfo["plugins"] as? Map<*, *>)?.size ?: 0
        val routeCount = (registrationInfo["routes"] as? Map<*, *>)?.size ?: 0
        val serviceCount = (registrationInfo["services"] as? Map<*, *>)?.size ?: 0
        val moduleCount = (registrationInfo["modules"] as? Map<*, *>)?.size ?: 0
        val componentCount = (registrationInfo["components"] as? Map<*, *>)?.size ?: 0
        
        val status = """
            ModuBox 状态:
            登录插件已加载: $isLoaded
            登录插件运行中: $isRunning
            登录插件状态: $state
            已注册插件数: $pluginCount
            已注册路由数: $routeCount
            已注册服务数: $serviceCount
            已注册模块数: $moduleCount
            已注册组件数: $componentCount
        """.trimIndent()
        
        tvStatus.text = status
    }
}