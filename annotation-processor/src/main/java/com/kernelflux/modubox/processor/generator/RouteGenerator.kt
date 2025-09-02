package com.kernelflux.modubox.processor.generator

import com.kernelflux.modubox.processor.model.RouteInfo
import com.squareup.kotlinpoet.*

/**
 * 路由代码生成器
 * 专门处理路由相关的代码生成
 */
class RouteGenerator : RegistryGenerator<RouteInfo>(
    packageName = "com.kernelflux.modubox.registry",
    className = "RouteRegistry"
) {
    
    override fun createRegisterMethod(items: List<RouteInfo>): FunSpec {
        val body = CodeBlock.builder()
            .addStatement("val routerManager = moduBox.getRouterManager()")
        
        items.forEach { route ->
            body.addStatement("routerManager.registerRoute(\"${route.path}\", ${route.className}::class)")
        }
        
        return createStandardRegisterMethod("registerRoutes", body)
    }
    
    override fun createInfoMethod(items: List<RouteInfo>): FunSpec {
        val body = CodeBlock.builder()
            .addStatement("val routes = mutableMapOf<String, String>()")
        
        items.forEach { route ->
            body.addStatement("routes[\"${route.path}\"] = \"${route.description}\"")
        }
        
        body.addStatement("return routes")
        
        return createStandardInfoMethod("getRouteInfo", body)
    }
}
