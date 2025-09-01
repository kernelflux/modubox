package com.kernelflux.modubox.core.impl.interceptors

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.kernelflux.modubox.annotation.RouteInterceptor
import com.kernelflux.modubox.core.api.RouterManager

/**
 * 权限拦截器
 * 检查应用权限，无权限则请求权限
 */
@RouteInterceptor(
    name = "PermissionInterceptor",
    priority = 90,
    global = false
)
class PermissionInterceptor(
    private val context: Context
) : RouterManager.Interceptor {
    
    companion object {
        private const val TAG = "PermissionInterceptor"
    }
    
    override suspend fun intercept(
        path: String,
        params: Map<String, Any>?,
        chain: RouterManager.InterceptorChain
    ): Boolean {
        Log.d(TAG, "PermissionInterceptor checking path: $path")
        
        // 根据路径检查所需权限
        val requiredPermissions = getRequiredPermissions(path)
        if (requiredPermissions.isNotEmpty()) {
            val missingPermissions = requiredPermissions.filter { permission ->
                ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
            }
            
            if (missingPermissions.isNotEmpty()) {
                Log.w(TAG, "Missing permissions: $missingPermissions")
                return false
            }
        }
        
        Log.d(TAG, "All permissions granted, proceeding with navigation")
        return chain.proceed(path, params)
    }
    
    override fun getPriority(): Int = 90
    
    override fun getName(): String = "PermissionInterceptor"
    
    override fun matches(path: String): Boolean {
        return path.startsWith("/camera/") || 
               path.startsWith("/location/") || 
               path.startsWith("/storage/")
    }
    
    private fun getRequiredPermissions(path: String): List<String> {
        return when {
            path.startsWith("/camera/") -> listOf(Manifest.permission.CAMERA)
            path.startsWith("/location/") -> listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            path.startsWith("/storage/") -> listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            else -> emptyList()
        }
    }
}
