package com.kernelflux.modubox.core.api

import android.content.Context

/**
 * 插件接口
 * 定义插件的基本功能和生命周期
 */
interface Plugin {
    
    /**
     * 插件ID，必须唯一
     */
    val pluginId: String
    
    /**
     * 插件名称
     */
    val pluginName: String
    
    /**
     * 插件版本
     */
    val version: String
    
    /**
     * 插件描述
     */
    val description: String
    
    /**
     * 插件初始化
     * @param context 应用上下文
     */
    fun onInit(context: Context)
    
    /**
     * 插件启动
     * @param params 启动参数
     */
    fun onStart(params: Map<String, Any>? = null)
    
    /**
     * 插件停止
     */
    fun onStop()
    
    /**
     * 插件销毁
     */
    fun onDestroy()
    
    /**
     * 获取插件状态
     * @return 插件状态
     */
    fun getState(): PluginState
    
    /**
     * 检查插件是否可用
     * @return 是否可用
     */
    fun isAvailable(): Boolean
}

/**
 * 插件状态枚举
 */
enum class PluginState {
    UNINITIALIZED,  // 未初始化
    INITIALIZED,    // 已初始化
    STARTED,        // 已启动
    STOPPED,        // 已停止
    DESTROYED       // 已销毁
}
