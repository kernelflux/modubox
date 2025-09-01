package com.kernelflux.modubox.processor.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.kernelflux.modubox.processor.model.ComponentInfo

/**
 * 组件代码生成器
 * 专门处理组件相关的代码生成
 */
class ComponentGenerator : BaseGenerator() {
    
    private val packageName = "com.kernelflux.modubox.registry"
    private val className = "ComponentRegistry"
    
    fun generate(codeGenerator: CodeGenerator, componentInfos: List<ComponentInfo>) {
        if (componentInfos.isEmpty()) return
        
        val imports = listOf(
            "com.kernelflux.modubox.core.api.ModuBox",
            "com.kernelflux.modubox.core.api.DependencyManager",
            "java.util.Map",
            "java.util.HashMap"
        )
        
        val classContent = generateComponentRegistryClass(componentInfos)
        
        generateJavaClass(codeGenerator, packageName, className, imports, classContent)
    }
    
    private fun generateComponentRegistryClass(componentInfos: List<ComponentInfo>): String {
        return buildString {
            appendLine("/**")
            appendLine(" * 组件注册器")
            appendLine(" * 由KSP注解处理器自动生成，请勿手动修改")
            appendLine(" */")
            appendLine("public class $className {")
            appendLine()
            
            // 生成注册方法
            append(generateRegisterMethod(componentInfos))
            appendLine()
            
            // 生成信息获取方法
            append(generateInfoMethod(componentInfos))
            appendLine()
            
            appendLine("}")
        }
    }
    
    private fun generateRegisterMethod(componentInfos: List<ComponentInfo>): String {
        val body = buildString {
            appendLine("        DependencyManager dependencyManager = moduBox.getDependencyManager();")
            componentInfos.forEach { component ->
                appendLine("        dependencyManager.registerComponent(new ${component.className}());")
            }
        }
        
        return generateMethod(
            modifiers = "public static",
            returnType = "void",
            methodName = "registerComponents",
            parameters = "ModuBox moduBox",
            body = body
        )
    }
    
    private fun generateInfoMethod(componentInfos: List<ComponentInfo>): String {
        val body = buildString {
            appendLine("        Map<String, String> components = new HashMap<>();")
            componentInfos.forEach { component ->
                appendLine("        components.put(\"${component.name}\", \"${component.description}\");")
            }
            appendLine("        return components;")
        }
        
        return generateMethod(
            modifiers = "public static",
            returnType = "Map<String, String>",
            methodName = "getComponentInfo",
            body = body
        )
    }
}
