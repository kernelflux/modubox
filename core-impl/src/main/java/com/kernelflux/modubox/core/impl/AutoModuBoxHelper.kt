package com.kernelflux.modubox.core.impl

import android.content.Context
import android.util.Log
import com.kernelflux.modubox.core.api.ModuBox
import com.kernelflux.modubox.core.api.ModuBoxConfig

/**
 * 支持自动注册的ModuBox工具类
 * 通过注解处理器生成的代码实现自动注册
 */
object AutoModuBoxHelper {
    private const val TAG = "AutoModuBoxHelper"
    private var moduBoxInstance: ModuBox? = null

    /**
     * 初始化ModuBox并自动注册所有组件
     * @param context 应用上下文
     * @return ModuBox实例
     */
    fun init(context: Context): ModuBox {
        return init(context, ModuBoxConfig.default())
    }
    
    /**
     * 初始化ModuBox并自动注册所有组件（带配置）
     * @param context 应用上下文
     * @param config 配置参数
     * @return ModuBox实例
     */
    fun init(context: Context, config: ModuBoxConfig): ModuBox {
        if (moduBoxInstance == null) {
            moduBoxInstance = ModuBoxImpl()
            moduBoxInstance!!.init(context, config)
            
            // 自动注册所有组件
            autoRegisterAll()
            
            Log.d(TAG, "ModuBox initialized with auto-registration")
        }
        return moduBoxInstance!!
    }
    
    /**
     * 自动注册所有组件
     */
    private fun autoRegisterAll() {
        try {
            // 使用反射调用生成的注册类
            val registryClass = Class.forName("com.kernelflux.modubox.registry.ModuBoxRegistry")
            val registerAllMethod = registryClass.getMethod("registerAll", ModuBox::class.java)
            registerAllMethod.invoke(null, moduBoxInstance)
            
            Log.d(TAG, "Auto registration completed successfully")
        } catch (e: ClassNotFoundException) {
            Log.w(TAG, "ModuBoxRegistry not found, skipping auto-registration. Make sure annotation processing is enabled.")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to auto-register components", e)
        }
    }
    
    /**
     * 手动注册插件（可选）
     */
    fun registerPlugins() {
        try {
            val registryClass = Class.forName("com.kernelflux.modubox.registry.PluginRegistry")
            val registerPluginsMethod = registryClass.getMethod("registerPlugins", ModuBox::class.java)
            registerPluginsMethod.invoke(null, moduBoxInstance)
            
            Log.d(TAG, "Plugins registered successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register plugins", e)
        }
    }
    
    /**
     * 手动注册路由（可选）
     */
    fun registerRoutes() {
        try {
            val registryClass = Class.forName("com.kernelflux.modubox.registry.RouteRegistry")
            val registerRoutesMethod = registryClass.getMethod("registerRoutes", ModuBox::class.java)
            registerRoutesMethod.invoke(null, moduBoxInstance)
            
            Log.d(TAG, "Routes registered successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register routes", e)
        }
    }
    
    /**
     * 手动注册服务（可选）
     */
    fun registerServices() {
        try {
            val registryClass = Class.forName("com.kernelflux.modubox.registry.ServiceRegistry")
            val registerServicesMethod = registryClass.getMethod("registerServices", ModuBox::class.java)
            registerServicesMethod.invoke(null, moduBoxInstance)
            
            Log.d(TAG, "Services registered successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register services", e)
        }
    }
    
    /**
     * 手动注册模块（可选）
     */
    fun registerModules() {
        try {
            val registryClass = Class.forName("com.kernelflux.modubox.registry.ModuleRegistry")
            val registerModulesMethod = registryClass.getMethod("registerModules", ModuBox::class.java)
            registerModulesMethod.invoke(null, moduBoxInstance)
            
            Log.d(TAG, "Modules registered successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register modules", e)
        }
    }
    
    /**
     * 手动注册组件（可选）
     */
    fun registerComponents() {
        try {
            val registryClass = Class.forName("com.kernelflux.modubox.registry.ComponentRegistry")
            val registerComponentsMethod = registryClass.getMethod("registerComponents", ModuBox::class.java)
            registerComponentsMethod.invoke(null, moduBoxInstance)
            
            Log.d(TAG, "Components registered successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register components", e)
        }
    }
    
    /**
     * 获取注册信息
     */
    fun getRegistrationInfo(): Map<String, Any> {
        val info = mutableMapOf<String, Any>()
        
        try {
            val registryClass = Class.forName("com.kernelflux.modubox.registry.ModuBoxRegistry")
            val getRegistrationInfoMethod = registryClass.getMethod("getRegistrationInfo")
            val registrationInfo = getRegistrationInfoMethod.invoke(null) as? Map<*, *>
            
            if (registrationInfo != null) {
                registrationInfo.forEach { (key, value) ->
                    info[key.toString()] = value ?: emptyMap<String, String>()
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get registration info", e)
        }
        
        return info
    }
    
    /**
     * 获取ModuBox实例
     */
    fun getInstance(): ModuBox? {
        return moduBoxInstance
    }
    
    /**
     * 获取ModuBox实例（非空）
     */
    fun getInstanceOrThrow(): ModuBox {
        return moduBoxInstance ?: throw IllegalStateException("ModuBox not initialized. Call AutoModuBoxHelper.init() first.")
    }
    
    /**
     * 清理实例
     */
    fun clear() {
        moduBoxInstance = null
    }
}
