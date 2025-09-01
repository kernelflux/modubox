package com.kernelflux.modubox.core.api

import android.content.Context

/**
 * 插件管理器接口
 * 负责插件的生命周期管理和状态控制
 */
interface PluginManager {
    
    /**
     * 初始化插件管理器
     * @param context 应用上下文
     */
    fun init(context: Context)
    
    /**
     * 注册插件
     * @param plugin 插件实例
     * @return 是否注册成功
     */
    fun registerPlugin(plugin: Plugin): Boolean
    
    /**
     * 注销插件
     * @param pluginId 插件ID
     * @return 是否注销成功
     */
    fun unregisterPlugin(pluginId: String): Boolean
    
    /**
     * 获取插件
     * @param pluginId 插件ID
     * @return 插件实例
     */
    fun getPlugin(pluginId: String): Plugin?
    
    /**
     * 获取所有插件
     * @return 插件列表
     */
    fun getAllPlugins(): List<Plugin>
    
    /**
     * 启动插件
     * @param pluginId 插件ID
     * @param params 启动参数
     * @return 是否启动成功
     */
    fun startPlugin(pluginId: String, params: Map<String, Any>? = null): Boolean
    
    /**
     * 停止插件
     * @param pluginId 插件ID
     * @return 是否停止成功
     */
    fun stopPlugin(pluginId: String): Boolean
    
    /**
     * 重启插件
     * @param pluginId 插件ID
     * @return 是否重启成功
     */
    fun restartPlugin(pluginId: String): Boolean
    
    /**
     * 销毁插件
     * @param pluginId 插件ID
     * @return 是否销毁成功
     */
    fun destroyPlugin(pluginId: String): Boolean
    
    /**
     * 检查插件是否已加载
     * @param pluginId 插件ID
     * @return 是否已加载
     */
    fun isPluginLoaded(pluginId: String): Boolean
    
    /**
     * 检查插件是否正在运行
     * @param pluginId 插件ID
     * @return 是否正在运行
     */
    fun isPluginRunning(pluginId: String): Boolean
    
    /**
     * 获取插件状态
     * @param pluginId 插件ID
     * @return 插件状态
     */
    fun getPluginState(pluginId: String): PluginState?
    
    /**
     * 获取运行中的插件列表
     * @return 运行中的插件列表
     */
    fun getRunningPlugins(): List<Plugin>
    
    /**
     * 停止所有插件
     */
    fun stopAllPlugins()
    
    /**
     * 销毁所有插件
     */
    fun destroyAllPlugins()
}
