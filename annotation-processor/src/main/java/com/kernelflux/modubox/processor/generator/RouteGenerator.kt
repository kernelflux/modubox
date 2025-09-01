package com.kernelflux.modubox.processor.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.kernelflux.modubox.processor.model.RouteInfo

/**
 * 路由代码生成器
 * 专门处理路由相关的代码生成
 */
class RouteGenerator : BaseGenerator() {
    
    private val packageName = "com.kernelflux.modubox.registry"
    private val className = "RouteRegistry"
    
    fun generate(codeGenerator: CodeGenerator, routeInfos: List<RouteInfo>) {
        if (routeInfos.isEmpty()) return
        
        val imports = listOf(
            "com.kernelflux.modubox.core.api.ModuBox",
            "com.kernelflux.modubox.core.api.RouterManager",
            "java.util.Map",
            "java.util.HashMap"
        )
        
        val classContent = generateRouteRegistryClass(routeInfos)
        
        generateJavaClass(codeGenerator, packageName, className, imports, classContent)
    }
    
    private fun generateRouteRegistryClass(routeInfos: List<RouteInfo>): String {
        return buildString {
            appendLine("/**")
            appendLine(" * 路由注册器")
            appendLine(" * 由KSP注解处理器自动生成，请勿手动修改")
            appendLine(" */")
            appendLine("public class $className {")
            appendLine()
            
            // 生成注册方法
            append(generateRegisterMethod(routeInfos))
            appendLine()
            
            // 生成信息获取方法
            append(generateInfoMethod(routeInfos))
            appendLine()
            
            appendLine("}")
        }
    }
    
    private fun generateRegisterMethod(routeInfos: List<RouteInfo>): String {
        val body = buildString {
            appendLine("        RouterManager routerManager = moduBox.getRouterManager();")
            routeInfos.forEach { route ->
                appendLine("        routerManager.registerRoute(\"${route.path}\", ${route.className}.class);")
            }
        }
        
        return generateMethod(
            modifiers = "public static",
            returnType = "void",
            methodName = "registerRoutes",
            parameters = "ModuBox moduBox",
            body = body
        )
    }
    
    private fun generateInfoMethod(routeInfos: List<RouteInfo>): String {
        val body = buildString {
            appendLine("        Map<String, String> routes = new HashMap<>();")
            routeInfos.forEach { route ->
                appendLine("        routes.put(\"${route.path}\", \"${route.description}\");")
            }
            appendLine("        return routes;")
        }
        
        return generateMethod(
            modifiers = "public static",
            returnType = "Map<String, String>",
            methodName = "getRouteInfo",
            body = body
        )
    }
}
