package com.kernelflux.modubox.processor.generator

import com.kernelflux.modubox.processor.model.PluginInfo
import com.squareup.kotlinpoet.*

/**
 * 插件代码生成器
 * 专门处理插件相关的代码生成
 */
class PluginGenerator : RegistryGenerator<PluginInfo>(
    packageName = "com.kernelflux.modubox.registry",
    className = "PluginRegistry"
) {
    
    override fun createRegisterMethod(items: List<PluginInfo>): FunSpec {
        val body = CodeBlock.builder()
        items.forEach { plugin ->
            body.addStatement("moduBox.registerPlugin(${plugin.className}())")
        }
        
        return createStandardRegisterMethod("registerPlugins", body)
    }
    
    override fun createInfoMethod(items: List<PluginInfo>): FunSpec {
        val body = CodeBlock.builder()
            .addStatement("val plugins = mutableMapOf<String, String>()")
        
        items.forEach { plugin ->
            body.addStatement("plugins[\"${plugin.id}\"] = \"${plugin.name} - ${plugin.version}\"")
        }
        
        body.addStatement("return plugins")
        
        return createStandardInfoMethod("getPluginInfo", body)
    }
}
