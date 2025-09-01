package com.kernelflux.modubox.core.impl

import android.content.Context
import android.util.Log
import com.kernelflux.modubox.core.api.*
import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * 依赖注入管理器实现类
 * 参考Hilt设计，支持大型组件化App
 */
class DependencyManagerImpl : DependencyManager {
    
    private lateinit var context: Context
    private val services = mutableMapOf<String, Any>()
    private val serviceTypes = mutableMapOf<Class<*>, Any>()
    private val modules = mutableMapOf<Class<*>, Any>()
    private val components = mutableMapOf<Class<*>, Any>()
    private val plugins = mutableListOf<Plugin>()
    
    companion object {
        private const val TAG = "DependencyManager"
    }
    
    override fun init(context: Context) {
        this.context = context.applicationContext
        Log.d(TAG, "DependencyManager initialized")
    }
    
    override fun registerPlugin(plugin: Plugin) {
        plugins.add(plugin)
        Log.d(TAG, "Plugin registered: ${plugin.javaClass.simpleName}")
    }
    
    override fun registerService(serviceName: String, service: Any) {
        services[serviceName] = service
        serviceTypes[service.javaClass] = service
        Log.d(TAG, "Service registered: $serviceName")
    }
    
    override fun registerModule(module: Any) {
        modules[module.javaClass] = module
        Log.d(TAG, "Module registered: ${module.javaClass.simpleName}")
    }
    
    override fun registerComponent(component: Any) {
        components[component.javaClass] = component
        Log.d(TAG, "Component registered: ${component.javaClass.simpleName}")
    }
    
    override fun <T> getService(serviceName: String): T? {
        @Suppress("UNCHECKED_CAST")
        return services[serviceName] as? T
    }
    
    override fun <T> getService(type: Class<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return serviceTypes[type] as? T
    }
    
    override fun <T> getModule(type: Class<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return modules[type] as? T
    }
    
    override fun <T> getComponent(type: Class<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return components[type] as? T
    }
    
    override fun inject(target: Any) {
        try {
            val clazz = target.javaClass
            val fields = clazz.declaredFields
            
            for (field in fields) {
                if (field.isAnnotationPresent(com.kernelflux.modubox.annotation.Inject::class.java)) {
                    injectField(target, field)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to inject dependencies into ${target.javaClass.simpleName}", e)
        }
    }
    
    private fun injectField(target: Any, field: Field) {
        try {
            field.isAccessible = true
            
            // 检查字段是否已经设置
            if (!Modifier.isFinal(field.modifiers) && field.get(target) != null) {
                return
            }
            
            val injectAnnotation = field.getAnnotation(com.kernelflux.modubox.annotation.Inject::class.java)
            val serviceName = injectAnnotation?.name
            
            val service = if (serviceName.isNullOrEmpty()) {
                // 按类型注入
                getService(field.type)
            } else {
                // 按名称注入
                getService<Any>(serviceName)
            }
            
            if (service != null) {
                field.set(target, service)
                Log.d(TAG, "Injected ${field.type.simpleName} into ${target.javaClass.simpleName}.${field.name}")
            } else {
                Log.w(TAG, "Service not found: ${serviceName ?: field.type.simpleName}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to inject field ${field.name} in ${target.javaClass.simpleName}", e)
        }
    }
    
    override fun <T> createInstance(clazz: Class<T>): T? {
        return try {
            val instance = clazz.getDeclaredConstructor().newInstance()
            inject(instance)
            instance
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create instance of ${clazz.simpleName}", e)
            null
        }
    }
    
    override fun hasService(serviceName: String): Boolean {
        return services.containsKey(serviceName)
    }
    
    override fun hasService(type: Class<*>): Boolean {
        return serviceTypes.containsKey(type)
    }
    
    override fun clear() {
        services.clear()
        serviceTypes.clear()
        modules.clear()
        components.clear()
        plugins.clear()
        Log.d(TAG, "All dependencies cleared")
    }
    
    override fun getAllServiceNames(): Set<String> {
        return services.keys.toSet()
    }
    
    override fun getAllModuleTypes(): Set<Class<*>> {
        return modules.keys.toSet()
    }
    
    override fun getAllComponentTypes(): Set<Class<*>> {
        return components.keys.toSet()
    }
}
