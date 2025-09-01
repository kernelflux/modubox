package com.kernelflux.modubox.processor.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.kernelflux.modubox.processor.model.RouteInterceptorInfo

/**
 * 拦截器代码生成器
 * 专门处理拦截器相关的代码生成
 */
class InterceptorGenerator : BaseGenerator() {
    
    private val packageName = "com.kernelflux.modubox.registry"
    private val className = "InterceptorRegistry"
    
    fun generate(codeGenerator: CodeGenerator, interceptorInfos: List<RouteInterceptorInfo>) {
        if (interceptorInfos.isEmpty()) return
        
        val imports = listOf(
            "com.kernelflux.modubox.core.api.ModuBox",
            "com.kernelflux.modubox.core.api.RouterManager",
            "java.util.Map",
            "java.util.HashMap"
        )
        
        val classContent = generateInterceptorRegistryClass(interceptorInfos)
        
        generateJavaClass(codeGenerator, packageName, className, imports, classContent)
    }
    
    private fun generateInterceptorRegistryClass(interceptorInfos: List<RouteInterceptorInfo>): String {
        return buildString {
            appendLine("/**")
            appendLine(" * 拦截器注册器")
            appendLine(" * 由KSP注解处理器自动生成，请勿手动修改")
            appendLine(" */")
            appendLine("public class $className {")
            appendLine()
            
            // 生成注册方法
            append(generateRegisterMethod(interceptorInfos))
            appendLine()
            
            // 生成信息获取方法
            append(generateInfoMethod(interceptorInfos))
            appendLine()
            
            appendLine("}")
        }
    }
    
    private fun generateRegisterMethod(interceptorInfos: List<RouteInterceptorInfo>): String {
        val body = buildString {
            appendLine("        RouterManager routerManager = moduBox.getRouterManager();")
            interceptorInfos.forEach { interceptor ->
                appendLine("        routerManager.addInterceptor(new ${interceptor.className}());")
            }
        }
        
        return generateMethod(
            modifiers = "public static",
            returnType = "void",
            methodName = "registerInterceptors",
            parameters = "ModuBox moduBox",
            body = body
        )
    }
    
    private fun generateInfoMethod(interceptorInfos: List<RouteInterceptorInfo>): String {
        val body = buildString {
            appendLine("        Map<String, String> interceptors = new HashMap<>();")
            interceptorInfos.forEach { interceptor ->
                appendLine("        interceptors.put(\"${interceptor.name}\", \"${interceptor.className}\");")
            }
            appendLine("        return interceptors;")
        }
        
        return generateMethod(
            modifiers = "public static",
            returnType = "Map<String, String>",
            methodName = "getInterceptorInfo",
            body = body
        )
    }
}
