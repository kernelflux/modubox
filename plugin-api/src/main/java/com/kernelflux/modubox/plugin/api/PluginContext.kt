package com.kernelflux.modubox.plugin.api

import android.content.Context
import com.kernelflux.modubox.core.api.*

/**
 * 插件上下文
 * 为插件提供运行环境和核心服务访问
 */
interface PluginContext {
    
    /**
     * 获取应用上下文
     */
    fun getApplicationContext(): Context
    
    /**
     * 获取插件管理器
     */
    fun getPluginManager(): PluginManager
    
    /**
     * 获取路由管理器
     */
    fun getRouterManager(): RouterManager
    
    /**
     * 获取依赖注入管理器
     */
    fun getDependencyManager(): DependencyManager
    
    /**
     * 获取ModuBox核心实例
     */
    fun getModuBox(): ModuBox
    
    /**
     * 获取插件ID
     */
    fun getPluginId(): String
    
    /**
     * 获取插件名称
     */
    fun getPluginName(): String
    
    /**
     * 获取插件版本
     */
    fun getVersion(): String
    
    /**
     * 获取插件描述
     */
    fun getDescription(): String
    
    /**
     * 获取插件配置
     */
    fun getConfig(): PluginConfig
    
    /**
     * 获取插件资源管理器
     */
    fun getResourceManager(): PluginResourceManager
}

/**
 * 插件配置
 */
data class PluginConfig(
    val autoStart: Boolean = false,
    val dependencies: List<String> = emptyList(),
    val permissions: List<String> = emptyList(),
    val features: List<String> = emptyList()
)

/**
 * 插件资源管理器
 */
interface PluginResourceManager {
    
    /**
     * 获取字符串资源
     */
    fun getString(resId: Int): String
    
    /**
     * 获取字符串资源（带参数）
     */
    fun getString(resId: Int, vararg formatArgs: Any): String
    
    /**
     * 获取颜色资源
     */
    fun getColor(resId: Int): Int
    
    /**
     * 获取尺寸资源
     */
    fun getDimension(resId: Int): Float
    
    /**
     * 获取布尔资源
     */
    fun getBoolean(resId: Int): Boolean
    
    /**
     * 获取整数资源
     */
    fun getInteger(resId: Int): Int
}
