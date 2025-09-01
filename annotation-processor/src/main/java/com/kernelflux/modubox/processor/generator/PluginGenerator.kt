package com.kernelflux.modubox.processor.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.kernelflux.modubox.processor.model.PluginInfo

/**
 * 插件代码生成器
 * 专门处理插件相关的代码生成
 */
class PluginGenerator : BaseGenerator() {
    
    private val packageName = "com.kernelflux.modubox.registry"
    private val className = "PluginRegistry"
    
    fun generate(codeGenerator: CodeGenerator, pluginInfos: List<PluginInfo>) {
        if (pluginInfos.isEmpty()) return
        
        val imports = listOf(
            "com.kernelflux.modubox.core.api.ModuBox",
            "java.util.Map",
            "java.util.HashMap"
        )
        
        val classContent = generatePluginRegistryClass(pluginInfos)
        
        generateJavaClass(codeGenerator, packageName, className, imports, classContent)
    }
    
    private fun generatePluginRegistryClass(pluginInfos: List<PluginInfo>): String {
        return buildString {
            appendLine("/**")
            appendLine(" * 插件注册器")
            appendLine(" * 由KSP注解处理器自动生成，请勿手动修改")
            appendLine(" */")
            appendLine("public class $className {")
            appendLine()
            
            // 生成注册方法
            append(generateRegisterMethod(pluginInfos))
            appendLine()
            
            // 生成信息获取方法
            append(generateInfoMethod(pluginInfos))
            appendLine()
            
            appendLine("}")
        }
    }
    
    private fun generateRegisterMethod(pluginInfos: List<PluginInfo>): String {
        val body = buildString {
            pluginInfos.forEach { plugin ->
                appendLine("        moduBox.registerPlugin(new ${plugin.className}());")
            }
        }
        
        return generateMethod(
            modifiers = "public static",
            returnType = "void",
            methodName = "registerPlugins",
            parameters = "ModuBox moduBox",
            body = body
        )
    }
    
    private fun generateInfoMethod(pluginInfos: List<PluginInfo>): String {
        val body = buildString {
            appendLine("        Map<String, String> plugins = new HashMap<>();")
            pluginInfos.forEach { plugin ->
                appendLine("        plugins.put(\"${plugin.id}\", \"${plugin.name} - ${plugin.version}\");")
            }
            appendLine("        return plugins;")
        }
        
        return generateMethod(
            modifiers = "public static",
            returnType = "Map<String, String>",
            methodName = "getPluginInfo",
            body = body
        )
    }
}
