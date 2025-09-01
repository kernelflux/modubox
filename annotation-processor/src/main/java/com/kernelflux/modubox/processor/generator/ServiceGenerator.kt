package com.kernelflux.modubox.processor.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.kernelflux.modubox.processor.model.ServiceInfo

/**
 * 服务代码生成器
 * 专门处理服务相关的代码生成
 */
class ServiceGenerator : BaseGenerator() {
    
    private val packageName = "com.kernelflux.modubox.registry"
    private val className = "ServiceRegistry"
    
    fun generate(codeGenerator: CodeGenerator, serviceInfos: List<ServiceInfo>) {
        if (serviceInfos.isEmpty()) return
        
        val imports = listOf(
            "com.kernelflux.modubox.core.api.ModuBox",
            "com.kernelflux.modubox.core.api.DependencyManager",
            "java.util.Map",
            "java.util.HashMap"
        )
        
        val classContent = generateServiceRegistryClass(serviceInfos)
        
        generateJavaClass(codeGenerator, packageName, className, imports, classContent)
    }
    
    private fun generateServiceRegistryClass(serviceInfos: List<ServiceInfo>): String {
        return buildString {
            appendLine("/**")
            appendLine(" * 服务注册器")
            appendLine(" * 由KSP注解处理器自动生成，请勿手动修改")
            appendLine(" */")
            appendLine("public class $className {")
            appendLine()
            
            // 生成注册方法
            append(generateRegisterMethod(serviceInfos))
            appendLine()
            
            // 生成信息获取方法
            append(generateInfoMethod(serviceInfos))
            appendLine()
            
            appendLine("}")
        }
    }
    
    private fun generateRegisterMethod(serviceInfos: List<ServiceInfo>): String {
        val body = buildString {
            appendLine("        DependencyManager dependencyManager = moduBox.getDependencyManager();")
            serviceInfos.forEach { service ->
                appendLine("        dependencyManager.registerService(${service.className}.class, new ${service.className}());")
            }
        }
        
        return generateMethod(
            modifiers = "public static",
            returnType = "void",
            methodName = "registerServices",
            parameters = "ModuBox moduBox",
            body = body
        )
    }
    
    private fun generateInfoMethod(serviceInfos: List<ServiceInfo>): String {
        val body = buildString {
            appendLine("        Map<String, String> services = new HashMap<>();")
            serviceInfos.forEach { service ->
                appendLine("        services.put(\"${service.name}\", \"${service.className}\");")
            }
            appendLine("        return services;")
        }
        
        return generateMethod(
            modifiers = "public static",
            returnType = "Map<String, String>",
            methodName = "getServiceInfo",
            body = body
        )
    }
}
