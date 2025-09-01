package com.kernelflux.modubox.core.impl

import android.content.Context
import android.util.Log
import com.kernelflux.modubox.core.api.*

/**
 * 插件管理器实现类
 */
class PluginManagerImpl : PluginManager {
    
    private lateinit var context: Context
    private val plugins = mutableMapOf<String, Plugin>()
    private val runningPlugins = mutableSetOf<String>()
    
    companion object {
        private const val TAG = "PluginManager"
    }
    
    override fun init(context: Context) {
        this.context = context.applicationContext
        Log.d(TAG, "PluginManager initialized")
    }
    
    override fun registerPlugin(plugin: Plugin): Boolean {
        return try {
            if (plugins.containsKey(plugin.pluginId)) {
                Log.w(TAG, "Plugin ${plugin.pluginId} already registered")
                return false
            }
            
            plugins[plugin.pluginId] = plugin
            plugin.onInit(context)
            Log.d(TAG, "Plugin ${plugin.pluginId} registered successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register plugin ${plugin.pluginId}", e)
            false
        }
    }
    
    override fun unregisterPlugin(pluginId: String): Boolean {
        return try {
            val plugin = plugins[pluginId] ?: return false
            stopPlugin(pluginId)
            plugin.onDestroy()
            plugins.remove(pluginId)
            Log.d(TAG, "Plugin $pluginId unregistered successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to unregister plugin $pluginId", e)
            false
        }
    }
    
    override fun getPlugin(pluginId: String): Plugin? {
        return plugins[pluginId]
    }
    
    override fun getAllPlugins(): List<Plugin> {
        return plugins.values.toList()
    }
    
    override fun startPlugin(pluginId: String, params: Map<String, Any>?): Boolean {
        return try {
            val plugin = plugins[pluginId] ?: return false
            
            if (!plugin.isAvailable()) {
                Log.w(TAG, "Plugin $pluginId is not available")
                return false
            }
            
            plugin.onStart(params)
            runningPlugins.add(pluginId)
            Log.d(TAG, "Plugin $pluginId started successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start plugin $pluginId", e)
            false
        }
    }
    
    override fun stopPlugin(pluginId: String): Boolean {
        return try {
            val plugin = plugins[pluginId] ?: return false
            
            plugin.onStop()
            runningPlugins.remove(pluginId)
            Log.d(TAG, "Plugin $pluginId stopped successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to stop plugin $pluginId", e)
            false
        }
    }
    
    override fun restartPlugin(pluginId: String): Boolean {
        return try {
            stopPlugin(pluginId)
            startPlugin(pluginId)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to restart plugin $pluginId", e)
            false
        }
    }
    
    override fun destroyPlugin(pluginId: String): Boolean {
        return try {
            val plugin = plugins[pluginId] ?: return false
            
            stopPlugin(pluginId)
            plugin.onDestroy()
            plugins.remove(pluginId)
            Log.d(TAG, "Plugin $pluginId destroyed successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to destroy plugin $pluginId", e)
            false
        }
    }
    
    override fun isPluginLoaded(pluginId: String): Boolean {
        return plugins.containsKey(pluginId)
    }
    
    override fun isPluginRunning(pluginId: String): Boolean {
        return runningPlugins.contains(pluginId)
    }
    
    override fun getPluginState(pluginId: String): PluginState? {
        val plugin = plugins[pluginId] ?: return null
        return plugin.getState()
    }
    
    override fun getRunningPlugins(): List<Plugin> {
        return runningPlugins.mapNotNull { plugins[it] }
    }
    
    override fun stopAllPlugins() {
        runningPlugins.toList().forEach { stopPlugin(it) }
    }
    
    override fun destroyAllPlugins() {
        plugins.keys.toList().forEach { destroyPlugin(it) }
    }
}
