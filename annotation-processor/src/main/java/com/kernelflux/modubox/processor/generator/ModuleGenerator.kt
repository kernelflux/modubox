package com.kernelflux.modubox.processor.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.kernelflux.modubox.processor.model.ModuleInfo

/**
 * 模块代码生成器
 * 专门处理模块相关的代码生成
 */
class ModuleGenerator : BaseGenerator() {
    
    private val packageName = "com.kernelflux.modubox.registry"
    private val className = "ModuleRegistry"
    
    fun generate(codeGenerator: CodeGenerator, moduleInfos: List<ModuleInfo>) {
        if (moduleInfos.isEmpty()) return
        
        val imports = listOf(
            "com.kernelflux.modubox.core.api.ModuBox",
            "com.kernelflux.modubox.core.api.DependencyManager",
            "java.util.Map",
            "java.util.HashMap"
        )
        
        val classContent = generateModuleRegistryClass(moduleInfos)
        
        generateJavaClass(codeGenerator, packageName, className, imports, classContent)
    }
    
    private fun generateModuleRegistryClass(moduleInfos: List<ModuleInfo>): String {
        return buildString {
            appendLine("/**")
            appendLine(" * 模块注册器")
            appendLine(" * 由KSP注解处理器自动生成，请勿手动修改")
            appendLine(" */")
            appendLine("public class $className {")
            appendLine()
            
            // 生成注册方法
            append(generateRegisterMethod(moduleInfos))
            appendLine()
            
            // 生成信息获取方法
            append(generateInfoMethod(moduleInfos))
            appendLine()
            
            appendLine("}")
        }
    }
    
    private fun generateRegisterMethod(moduleInfos: List<ModuleInfo>): String {
        val body = buildString {
            appendLine("        DependencyManager dependencyManager = moduBox.getDependencyManager();")
            moduleInfos.forEach { module ->
                appendLine("        dependencyManager.registerModule(new ${module.className}());")
            }
        }
        
        return generateMethod(
            modifiers = "public static",
            returnType = "void",
            methodName = "registerModules",
            parameters = "ModuBox moduBox",
            body = body
        )
    }
    
    private fun generateInfoMethod(moduleInfos: List<ModuleInfo>): String {
        val body = buildString {
            appendLine("        Map<String, String> modules = new HashMap<>();")
            moduleInfos.forEach { module ->
                appendLine("        modules.put(\"${module.name}\", \"${module.description}\");")
            }
            appendLine("        return modules;")
        }
        
        return generateMethod(
            modifiers = "public static",
            returnType = "Map<String, String>",
            methodName = "getModuleInfo",
            body = body
        )
    }
}
