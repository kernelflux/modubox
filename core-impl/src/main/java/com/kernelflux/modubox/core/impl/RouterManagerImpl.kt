package com.kernelflux.modubox.core.impl

import android.content.Context
import android.content.Intent
import android.util.Log
import com.kernelflux.modubox.core.api.*

/**
 * 路由管理器实现类
 */
class RouterManagerImpl : RouterManager {
    
    private lateinit var context: Context
    private val routes = mutableMapOf<String, RouteInfo>()
    private val interceptors = mutableListOf<RouterManager.Interceptor>()
    
    companion object {
        private const val TAG = "RouterManager"
    }
    
    override fun init(context: Context) {
        this.context = context.applicationContext
        Log.d(TAG, "RouterManager initialized")
    }
    
    override fun registerRoute(path: String, target: Class<*>) {
        routes[path] = RouteInfo(path, target)
        Log.d(TAG, "Route registered: $path -> ${target.simpleName}")
    }
    
    override fun unregisterRoute(path: String) {
        routes.remove(path)
        Log.d(TAG, "Route unregistered: $path")
    }
    
    override fun navigate(path: String, params: Map<String, Any>?): Boolean {
        return try {
            // 检查拦截器
            for (interceptor in interceptors) {
                if (interceptor.intercept(path, params)) {
                    Log.d(TAG, "Route intercepted: $path")
                    return false
                }
            }
            
            val routeInfo = routes[path] ?: return false
            val intent = buildIntent(path, params) ?: return false
            
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            Log.d(TAG, "Navigation successful: $path")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Navigation failed: $path", e)
            false
        }
    }
    
    override fun navigateForResult(path: String, params: Map<String, Any>?, requestCode: Int): Boolean {
        // 这里需要Activity上下文，暂时返回false
        Log.w(TAG, "navigateForResult not implemented yet")
        return false
    }
    
    override fun buildIntent(path: String, params: Map<String, Any>?): Intent? {
        val routeInfo = routes[path] ?: return null
        
        return Intent(context, routeInfo.target).apply {
            params?.forEach { (key, value) ->
                when (value) {
                    is String -> putExtra(key, value)
                    is Int -> putExtra(key, value)
                    is Long -> putExtra(key, value)
                    is Float -> putExtra(key, value)
                    is Double -> putExtra(key, value)
                    is Boolean -> putExtra(key, value)
                    is Char -> putExtra(key, value)
                    is Byte -> putExtra(key, value)
                    is Short -> putExtra(key, value)
                    is CharSequence -> putExtra(key, value)
                    is IntArray -> putExtra(key, value)
                    is LongArray -> putExtra(key, value)
                    is FloatArray -> putExtra(key, value)
                    is DoubleArray -> putExtra(key, value)
                    is BooleanArray -> putExtra(key, value)
                    is CharArray -> putExtra(key, value)
                    is ByteArray -> putExtra(key, value)
                    is ShortArray -> putExtra(key, value)
                    is CharSequenceArray -> putExtra(key, value)
                    is ArrayList<*> -> putExtra(key, value)
                    else -> putExtra(key, value.toString())
                }
            }
        }
    }
    
    override fun hasRoute(path: String): Boolean {
        return routes.containsKey(path)
    }
    
    override fun getRouteInfo(path: String): RouteInfo? {
        return routes[path]
    }
    
    override fun getAllRoutes(): List<RouteInfo> {
        return routes.values.toList()
    }
    
    override fun addInterceptor(interceptor: RouterManager.Interceptor) {
        interceptors.add(interceptor)
    }
    
    override fun removeInterceptor(interceptor: RouterManager.Interceptor) {
        interceptors.remove(interceptor)
    }
}
