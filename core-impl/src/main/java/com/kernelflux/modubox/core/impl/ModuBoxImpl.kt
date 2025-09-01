package com.kernelflux.modubox.core.impl

import android.content.Context
import com.kernelflux.modubox.core.api.*

/**
 * ModuBox核心实现类
 */
class ModuBoxImpl : ModuBox {
    
    private lateinit var context: Context
    private lateinit var config: ModuBoxConfig
    private lateinit var pluginManager: PluginManager
    private lateinit var routerManager: RouterManager
    private lateinit var dependencyManager: DependencyManager
    
    override fun init(context: Context) {
        init(context, ModuBoxConfig.default())
    }
    
    override fun init(context: Context, config: ModuBoxConfig) {
        this.context = context.applicationContext
        this.config = config
        
        // 初始化各个管理器
        pluginManager = PluginManagerImpl()
        routerManager = RouterManagerImpl()
        dependencyManager = DependencyManagerImpl()
        
        pluginManager.init(context)
        routerManager.init(context)
        dependencyManager.init(context)
        
        // 注册核心服务
        dependencyManager.registerService(ModuBox::class.java, this)
        dependencyManager.registerService(PluginManager::class.java, pluginManager)
        dependencyManager.registerService(RouterManager::class.java, routerManager)
        dependencyManager.registerService(DependencyManager::class.java, dependencyManager)
    }
    
    override fun getConfig(): ModuBoxConfig {
        return config
    }
    
    override fun registerPlugin(plugin: Plugin) {
        pluginManager.registerPlugin(plugin)
    }
    
    override fun getPlugin(pluginId: String): Plugin? {
        return pluginManager.getPlugin(pluginId)
    }
    
    override fun getAllPlugins(): List<Plugin> {
        return pluginManager.getAllPlugins()
    }
    
    override fun startPlugin(pluginId: String, params: Map<String, Any>?) {
        pluginManager.startPlugin(pluginId, params)
    }
    
    override fun stopPlugin(pluginId: String) {
        pluginManager.stopPlugin(pluginId)
    }
    
    override fun isPluginLoaded(pluginId: String): Boolean {
        return pluginManager.isPluginLoaded(pluginId)
    }
    
    override fun getPluginManager(): PluginManager {
        return pluginManager
    }
    
    override fun getRouterManager(): RouterManager {
        return routerManager
    }
    
    override fun getDependencyManager(): DependencyManager {
        return dependencyManager
    }
}
