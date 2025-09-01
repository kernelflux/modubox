package com.kernelflux.modubox.core.api

import android.content.Context

/**
 * ModuBox 核心API接口
 * 提供插件化框架的主要功能
 */
interface ModuBox {
    
    /**
     * 初始化ModuBox框架
     * @param context 应用上下文
     */
    fun init(context: Context)
    
    /**
     * 初始化ModuBox框架（带配置）
     * @param context 应用上下文
     * @param config 配置参数
     */
    fun init(context: Context, config: ModuBoxConfig)
    
    /**
     * 获取配置
     * @return 当前配置
     */
    fun getConfig(): ModuBoxConfig
    
    /**
     * 注册插件
     * @param plugin 插件实例
     */
    fun registerPlugin(plugin: Plugin)
    
    /**
     * 获取插件
     * @param pluginId 插件ID
     * @return 插件实例，如果不存在返回null
     */
    fun getPlugin(pluginId: String): Plugin?
    
    /**
     * 获取所有已注册的插件
     * @return 插件列表
     */
    fun getAllPlugins(): List<Plugin>
    
    /**
     * 启动插件
     * @param pluginId 插件ID
     * @param params 启动参数
     */
    fun startPlugin(pluginId: String, params: Map<String, Any>? = null)
    
    /**
     * 停止插件
     * @param pluginId 插件ID
     */
    fun stopPlugin(pluginId: String)
    
    /**
     * 检查插件是否已加载
     * @param pluginId 插件ID
     * @return 是否已加载
     */
    fun isPluginLoaded(pluginId: String): Boolean
    
    /**
     * 获取插件管理器
     * @return 插件管理器实例
     */
    fun getPluginManager(): PluginManager
    
    /**
     * 获取路由管理器
     * @return 路由管理器实例
     */
    fun getRouterManager(): RouterManager
    
    /**
     * 获取依赖注入管理器
     * @return 依赖注入管理器实例
     */
    fun getDependencyManager(): DependencyManager
}
