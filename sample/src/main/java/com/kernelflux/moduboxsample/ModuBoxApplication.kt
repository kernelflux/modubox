package com.kernelflux.moduboxsample

import android.app.Application
import android.util.Log
import com.kernelflux.modubox.core.api.ModuBoxConfig
import com.kernelflux.modubox.core.impl.AutoModuBoxHelper

/**
 * ModuBox应用类
 * 负责在应用启动时初始化ModuBox框架
 */
class ModuBoxApplication : Application() {
    
    companion object {
        private const val TAG = "ModuBoxApplication"
    }
    
    override fun onCreate() {
        super.onCreate()
        
        try {
            // 根据构建类型选择配置
            val config = if (BuildConfig.DEBUG) {
                ModuBoxConfig.debug()
            } else {
                ModuBoxConfig.production()
            }
            
            // 使用自动注册初始化ModuBox框架
            val moduBox = AutoModuBoxHelper.init(this, config)
            
            // 获取注册信息并打印日志
            val registrationInfo = AutoModuBoxHelper.getRegistrationInfo()
            Log.d(TAG, "Registration info: $registrationInfo")
            
            Log.d(TAG, "ModuBox framework initialized successfully with auto-registration")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize ModuBox framework", e)
        }
    }
    
    override fun onTerminate() {
        super.onTerminate()
        
        // 清理ModuBox资源
        AutoModuBoxHelper.clear()
        Log.d(TAG, "ModuBox framework terminated")
    }
}
