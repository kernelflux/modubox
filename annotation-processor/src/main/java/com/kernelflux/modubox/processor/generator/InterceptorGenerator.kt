package com.kernelflux.modubox.processor.generator

import com.kernelflux.modubox.processor.model.RouteInterceptorInfo
import com.squareup.kotlinpoet.*

/**
 * 拦截器代码生成器
 * 专门处理路由拦截器相关的代码生成
 */
class InterceptorGenerator : RegistryGenerator<RouteInterceptorInfo>(
    packageName = "com.kernelflux.modubox.registry",
    className = "InterceptorRegistry"
) {
    
    override fun createRegisterMethod(items: List<RouteInterceptorInfo>): FunSpec {
        val body = CodeBlock.builder()
            .addStatement("val routerManager = moduBox.getRouterManager()")
        
        items.forEach { interceptor ->
            body.addStatement("routerManager.registerInterceptor(\"${interceptor.patterns.firstOrNull() ?: ""}\", ${interceptor.className}())")
        }
        
        return createStandardRegisterMethod("registerInterceptors", body)
    }
    
    override fun createInfoMethod(items: List<RouteInterceptorInfo>): FunSpec {
        val body = CodeBlock.builder()
            .addStatement("val interceptors = mutableMapOf<String, String>()")
        
        items.forEach { interceptor ->
            body.addStatement("interceptors[\"${interceptor.patterns.firstOrNull() ?: ""}\"] = \"${interceptor.description}\"")
        }
        
        body.addStatement("return interceptors")
        
        return createStandardInfoMethod("getInterceptorInfo", body)
    }
}
