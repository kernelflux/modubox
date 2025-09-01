package com.kernelflux.modubox.feature.login

import android.content.Context
import com.kernelflux.modubox.annotation.Plugin
import com.kernelflux.modubox.plugin.api.BasePlugin

/**
 * 登录功能插件
 */
@Plugin(
    id = "login",
    name = "登录插件",
    version = "1.0.0",
    description = "提供用户登录功能",
    autoStart = false
)
class LoginPlugin : BasePlugin() {
    
    override val pluginId: String = "login"
    override val pluginName: String = "登录插件"
    override val version: String = "1.0.0"
    override val description: String = "提供用户登录功能"
    
    override fun onPluginInit(context: Context) {
        super.onPluginInit(context)
        
        // 插件初始化
        // 注意：路由和服务注册现在由注解处理器自动处理
        // 不再需要手动注册
    }
    
    override fun onPluginStart(params: Map<String, Any>?) {
        super.onPluginStart(params)
        
        // 启动时可以进行一些初始化操作
    }
    
    override fun onPluginStop() {
        super.onPluginStop()
        
        // 停止时可以进行一些清理操作
    }
    
    override fun onPluginDestroy() {
        super.onPluginDestroy()
        
        // 销毁时进行最终清理
    }
}
