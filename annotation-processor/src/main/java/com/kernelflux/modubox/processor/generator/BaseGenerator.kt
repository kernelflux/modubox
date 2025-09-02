package com.kernelflux.modubox.processor.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.squareup.kotlinpoet.*

/**
 * 基础代码生成器
 * 提供通用的代码生成功能
 */
abstract class BaseGenerator {
    
    /**
     * 生成Kotlin类文件
     */
    protected fun generateKotlinClass(
        codeGenerator: CodeGenerator,
        packageName: String,
        className: String,
        classBuilder: TypeSpec.Builder
    ) {
        val fileSpec = FileSpec.builder(packageName, className)
            .addType(classBuilder.build())
            .build()
        
        val kspFile = codeGenerator.createNewFile(
            dependencies = Dependencies.ALL_FILES,
            packageName = packageName,
            fileName = "$className.kt"
        )
        
        kspFile.writer().use { writer ->
            fileSpec.writeTo(writer)
        }
    }
    
    /**
     * 创建方法构建器
     */
    protected fun createMethodBuilder(
        name: String,
        returnType: TypeName
    ): FunSpec.Builder {
        return FunSpec.builder(name)
            .returns(returnType)
            .addModifiers(KModifier.PUBLIC)
    }
    
    /**
     * 创建对象构建器
     */
    protected fun createObjectBuilder(name: String): TypeSpec.Builder {
        return TypeSpec.objectBuilder(name)
            .addModifiers(KModifier.PUBLIC)
    }
}
