package com.kernelflux.modubox.processor.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

/**
 * 通用注册器生成器基类
 * 减少重复代码，提供统一的生成模式
 */
abstract class RegistryGenerator<T>(
    private val packageName: String,
    private val className: String
) : BaseGenerator() {
    
    fun generate(codeGenerator: CodeGenerator, infos: List<T>) {
        if (infos.isEmpty()) return
        
        val registryClass = createRegistryClass(infos)
        generateKotlinClass(codeGenerator, packageName, className, registryClass)
    }
    
    private fun createRegistryClass(infos: List<T>): TypeSpec.Builder {
        return createObjectBuilder(className)
            .addFunction(createRegisterMethod(infos))
            .addFunction(createInfoMethod(infos))
    }
    
    protected abstract fun createRegisterMethod(items: List<T>): FunSpec
    protected abstract fun createInfoMethod(items: List<T>): FunSpec
    
    /**
     * 创建标准的注册方法
     */
    protected fun createStandardRegisterMethod(
        name: String,
        body: CodeBlock.Builder
    ): FunSpec {
        return createMethodBuilder(name, Unit::class.asTypeName())
            .addParameter("moduBox", ClassName("com.kernelflux.modubox.core.api", "ModuBox"))
            .addCode(body.build())
            .build()
    }
    
    /**
     * 创建标准的信息方法
     */
    protected fun createStandardInfoMethod(
        name: String,
        body: CodeBlock.Builder
    ): FunSpec {
        return createMethodBuilder(
            name,
            Map::class.asTypeName().parameterizedBy(
                String::class.asTypeName(),
                String::class.asTypeName()
            )
        )
            .addCode(body.build())
            .build()
    }
}
