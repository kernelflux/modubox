package com.kernelflux.modubox.plugin.api

import android.content.Context
import com.kernelflux.modubox.core.api.*

/**
 * 插件基类
 * 提供插件的基础实现，简化插件开发
 */
abstract class BasePlugin : Plugin {
    
    private var pluginContext: PluginContext? = null
    private var currentState: PluginState = PluginState.UNINITIALIZED
    private var isAvailableFlag: Boolean = true
    
    override fun onInit(context: Context) {
        try {
            currentState = PluginState.INITIALIZED
            onPluginInit(context)
        } catch (e: Exception) {
            currentState = PluginState.UNINITIALIZED
            throw e
        }
    }
    
    override fun onStart(params: Map<String, Any>?) {
        try {
            currentState = PluginState.STARTED
            onPluginStart(params)
        } catch (e: Exception) {
            currentState = PluginState.STOPPED
            throw e
        }
    }
    
    override fun onStop() {
        try {
            currentState = PluginState.STOPPED
            onPluginStop()
        } catch (e: Exception) {
            throw e
        }
    }
    
    override fun onDestroy() {
        try {
            currentState = PluginState.DESTROYED
            onPluginDestroy()
        } catch (e: Exception) {
            throw e
        }
    }
    
    override fun getState(): PluginState {
        return currentState
    }
    
    override fun isAvailable(): Boolean {
        return isAvailableFlag
    }
    
    /**
     * 设置插件上下文
     */
    fun setPluginContext(context: PluginContext) {
        this.pluginContext = context
    }
    
    /**
     * 获取插件上下文
     */
    protected fun getPluginContext(): PluginContext? {
        return pluginContext
    }
    
    /**
     * 设置可用状态
     */
    protected fun setAvailable(available: Boolean) {
        isAvailableFlag = available
    }
    
    /**
     * 插件初始化回调
     */
    protected open fun onPluginInit(context: Context) {
        // 子类可以重写此方法
    }
    
    /**
     * 插件启动回调
     */
    protected open fun onPluginStart(params: Map<String, Any>?) {
        // 子类可以重写此方法
    }
    
    /**
     * 插件停止回调
     */
    protected open fun onPluginStop() {
        // 子类可以重写此方法
    }
    
    /**
     * 插件销毁回调
     */
    protected open fun onPluginDestroy() {
        // 子类可以重写此方法
    }
    
    /**
     * 获取路由管理器
     */
    protected fun getRouterManager(): RouterManager? {
        return pluginContext?.getRouterManager()
    }
    
    /**
     * 获取依赖注入管理器
     */
    protected fun getDependencyManager(): DependencyManager? {
        return pluginContext?.getDependencyManager()
    }
    
    /**
     * 获取插件管理器
     */
    protected fun getPluginManager(): PluginManager? {
        return pluginContext?.getPluginManager()
    }
    
    /**
     * 导航到指定路径
     */
    protected fun navigate(path: String, params: Map<String, Any>? = null): Boolean {
        return getRouterManager()?.navigate(path, params) ?: false
    }
    
    /**
     * 获取服务
     */
    protected fun <T> getService(serviceClass: Class<T>): T? {
        return getDependencyManager()?.getService(serviceClass)
    }
    
    /**
     * 注册服务
     */
    protected fun <T> registerService(serviceClass: Class<T>, instance: T) {
        getDependencyManager()?.registerService(serviceClass, instance)
    }
}
