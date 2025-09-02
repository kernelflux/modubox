package com.kernelflux.modubox.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.*
import com.kernelflux.modubox.annotation.*
import com.kernelflux.modubox.processor.generator.*
import com.kernelflux.modubox.processor.model.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

/**
 * ModuBox KSP注解处理器
 * 负责处理所有ModuBox相关的注解并生成相应的代码
 */
class ModuBoxProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {
    
    companion object {
        private val MAP_STRING_ANY = Map::class.asTypeName().parameterizedBy(
            String::class.asTypeName(),
            Any::class.asTypeName()
        )
    }
    
    private val pluginInfos = mutableListOf<PluginInfo>()
    private val routeInfos = mutableListOf<RouteInfo>()
    private val routeInterceptorInfos = mutableListOf<RouteInterceptorInfo>()
    private val serviceInfos = mutableListOf<ServiceInfo>()
    private val moduleInfos = mutableListOf<ModuleInfo>()
    private val componentInfos = mutableListOf<ComponentInfo>()

    
    override fun process(resolver: Resolver): List<KSAnnotated> {
        // 处理所有注解
        processAnnotations(resolver)
        
        // 生成代码
        generateCode()
        
        return emptyList()
    }
    
    private fun processAnnotations(resolver: Resolver) {
        // 处理@Plugin注解
        resolver.getSymbolsWithAnnotation(Plugin::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { processPlugin(it) }
        
        // 处理@Route注解
        resolver.getSymbolsWithAnnotation(Route::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { processRoute(it) }
        
        // 处理@RouteInterceptor注解
        resolver.getSymbolsWithAnnotation(RouteInterceptor::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { processRouteInterceptor(it) }
        
        // 处理@Service注解
        resolver.getSymbolsWithAnnotation(Service::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { processService(it) }
        
        // 处理@Module注解
        resolver.getSymbolsWithAnnotation(Module::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { processModule(it) }
        
        // 处理@Component注解
        resolver.getSymbolsWithAnnotation(Component::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { processComponent(it) }
        

    }
    
    private fun processPlugin(symbol: KSClassDeclaration) {
        val annotation = symbol.annotations.first { it.shortName.asString() == "Plugin" }
        val arguments = annotation.arguments.associate { it.name?.asString() to it.value }
        
        val pluginInfo = PluginInfo(
            className = symbol.qualifiedName?.asString() ?: "",
            simpleName = symbol.simpleName.asString(),
            id = arguments["id"] as? String ?: "",
            name = (arguments["name"] as? String)?.ifEmpty { symbol.simpleName.asString() } ?: symbol.simpleName.asString(),
            version = arguments["version"] as? String ?: "1.0.0",
            description = arguments["description"] as? String ?: "",
            autoStart = arguments["autoStart"] as? Boolean ?: false,
            dependencies = (arguments["dependencies"] as? Array<*>)?.mapNotNull { it as? String } ?: emptyList()
        )
        
        pluginInfos.add(pluginInfo)
        logger.info("Found plugin: ${pluginInfo.id}")
    }
    
    private fun processRoute(symbol: KSClassDeclaration) {
        val annotation = symbol.annotations.first { it.shortName.asString() == "Route" }
        val arguments = annotation.arguments.associate { it.name?.asString() to it.value }
        
        val routeInfo = RouteInfo(
            className = symbol.qualifiedName?.asString() ?: "",
            simpleName = symbol.simpleName.asString(),
            path = arguments["path"] as? String ?: "",
            group = arguments["group"] as? String ?: "",
            description = arguments["description"] as? String ?: "",
            requireLogin = arguments["requireLogin"] as? Boolean ?: false,
            permissions = (arguments["permissions"] as? Array<*>)?.mapNotNull { it as? String } ?: emptyList(),
            priority = arguments["priority"] as? Int ?: 0,
            regex = arguments["regex"] as? Boolean ?: false,
            fallback = arguments["fallback"] as? Boolean ?: false,
            fallbackPath = arguments["fallbackPath"] as? String ?: "",
            extras = (arguments["extras"] as? Array<*>)?.mapNotNull { it as? String } ?: emptyList(),
            webView = arguments["webView"] as? Boolean ?: false,
            external = arguments["external"] as? Boolean ?: false,
            externalPackage = arguments["externalPackage"] as? String ?: "",
            deepLink = arguments["deepLink"] as? Boolean ?: false,
            scheme = arguments["scheme"] as? String ?: "",
            multiProcess = arguments["multiProcess"] as? Boolean ?: false,
            process = arguments["process"] as? String ?: "",
            abTest = arguments["abTest"] as? Boolean ?: false,
            abTestConfig = arguments["abTestConfig"] as? String ?: "",
            tags = (arguments["tags"] as? Array<*>)?.mapNotNull { it as? String } ?: emptyList(),
            enabled = arguments["enabled"] as? Boolean ?: true
        )
        
        routeInfos.add(routeInfo)
        logger.info("Found route: ${routeInfo.path}")
    }
    
    private fun processRouteInterceptor(symbol: KSClassDeclaration) {
        val annotation = symbol.annotations.first { it.shortName.asString() == "RouteInterceptor" }
        val arguments = annotation.arguments.associate { it.name?.asString() to it.value }
        
        val interceptorInfo = RouteInterceptorInfo(
            className = symbol.qualifiedName?.asString() ?: "",
            simpleName = symbol.simpleName.asString(),
            name = arguments["name"] as? String ?: symbol.simpleName.asString(),
            description = arguments["description"] as? String ?: "",
            priority = arguments["priority"] as? Int ?: 0,
            group = arguments["group"] as? String ?: "",
            global = arguments["global"] as? Boolean ?: false,
            patterns = (arguments["patterns"] as? Array<*>)?.mapNotNull { it as? String } ?: emptyList(),
            excludes = (arguments["excludes"] as? Array<*>)?.mapNotNull { it as? String } ?: emptyList(),
            async = arguments["async"] as? Boolean ?: false,
            timeout = arguments["timeout"] as? Long ?: 0L,
            enabled = arguments["enabled"] as? Boolean ?: true,
            tags = (arguments["tags"] as? Array<*>)?.mapNotNull { it as? String } ?: emptyList(),
            hotReload = arguments["hotReload"] as? Boolean ?: false
        )
        
        routeInterceptorInfos.add(interceptorInfo)
        logger.info("Found route interceptor: ${interceptorInfo.patterns}")
    }
    
    private fun processService(symbol: KSClassDeclaration) {
        val annotation = symbol.annotations.first { it.shortName.asString() == "Service" }
        val arguments = annotation.arguments.associate { it.name?.asString() to it.value }
        
        val serviceInfo = ServiceInfo(
            className = symbol.qualifiedName?.asString() ?: "",
            simpleName = symbol.simpleName.asString(),
            name = arguments["name"] as? String ?: symbol.simpleName.asString(),
            singleton = arguments["singleton"] as? Boolean ?: false
        )
        
        serviceInfos.add(serviceInfo)
        logger.info("Found service: ${serviceInfo.name}")
    }
    
    private fun processModule(symbol: KSClassDeclaration) {
        val annotation = symbol.annotations.first { it.shortName.asString() == "Module" }
        val arguments = annotation.arguments.associate { it.name?.asString() to it.value }
        
        val moduleInfo = ModuleInfo(
            className = symbol.qualifiedName?.asString() ?: "",
            simpleName = symbol.simpleName.asString(),
            name = arguments["name"] as? String ?: symbol.simpleName.asString(),
            description = arguments["description"] as? String ?: "",
            priority = arguments["priority"] as? Int ?: 0,
            singleton = arguments["singleton"] as? Boolean ?: false,
            dependencies = (arguments["dependencies"] as? Array<*>)?.mapNotNull { it as? String } ?: emptyList(),
            scope = arguments["scope"] as? String ?: "singleton",
            lazy = arguments["lazy"] as? Boolean ?: false,
            hotReload = arguments["hotReload"] as? Boolean ?: false
        )
        
        moduleInfos.add(moduleInfo)
        logger.info("Found module: ${moduleInfo.name}")
    }
    
    private fun processComponent(symbol: KSClassDeclaration) {
        val annotation = symbol.annotations.first { it.shortName.asString() == "Component" }
        val arguments = annotation.arguments.associate { it.name?.asString() to it.value }
        
        val componentInfo = ComponentInfo(
            className = symbol.qualifiedName?.asString() ?: "",
            simpleName = symbol.simpleName.asString(),
            name = arguments["name"] as? String ?: symbol.simpleName.asString(),
            description = arguments["description"] as? String ?: "",
            scope = arguments["scope"] as? String ?: "singleton",
            modules = (arguments["modules"] as? Array<*>)?.mapNotNull { it as? String } ?: emptyList(),
            dependencies = (arguments["dependencies"] as? Array<*>)?.mapNotNull { it as? String } ?: emptyList(),
            singleton = arguments["singleton"] as? Boolean ?: false,
            lazy = arguments["lazy"] as? Boolean ?: false,
            hotReload = arguments["hotReload"] as? Boolean ?: false,
            priority = arguments["priority"] as? Int ?: 0
        )
        
        componentInfos.add(componentInfo)
        logger.info("Found component: ${componentInfo.name}")
    }
    

    
    private fun generateCode() {
        if (!hasAnyAnnotations()) {
            logger.info("No ModuBox annotations found, skipping code generation")
            return
        }
        
        // 生成各个注册器
        PluginGenerator().generate(codeGenerator, pluginInfos)
        RouteGenerator().generate(codeGenerator, routeInfos)
        InterceptorGenerator().generate(codeGenerator, routeInterceptorInfos)
        ServiceGenerator().generate(codeGenerator, serviceInfos)
        ModuleGenerator().generate(codeGenerator, moduleInfos)
        ComponentGenerator().generate(codeGenerator, componentInfos)
        
        // 生成主注册器
        generateMainRegistry()
    }
    
    private fun generateMainRegistry() {
        val registryClass = TypeSpec.classBuilder("ModuBoxRegistry")
            .addModifiers(KModifier.PUBLIC)
            .addFunction(generateUnifiedRegisterMethod())
            .addFunction(generateUnifiedInfoMethod())
            .build()
        
        val fileSpec = FileSpec.builder("com.kernelflux.modubox.registry", "ModuBoxRegistry")
            .addType(registryClass)
            .build()
        
        val kspFile = codeGenerator.createNewFile(
            dependencies = Dependencies.ALL_FILES,
            packageName = "com.kernelflux.modubox.registry",
            fileName = "ModuBoxRegistry"
        )
        
        kspFile.writer().use { writer ->
            fileSpec.writeTo(writer)
        }
    }
    
    private fun generateUnifiedRegisterMethod(): FunSpec {
        val body = CodeBlock.builder()
        
        if (pluginInfos.isNotEmpty()) {
            body.addStatement("// 注册插件")
            body.addStatement("PluginRegistry.registerPlugins(moduBox)")
        }
        
        if (routeInfos.isNotEmpty()) {
            body.addStatement("// 注册路由")
            body.addStatement("RouteRegistry.registerRoutes(moduBox)")
        }
        
        if (routeInterceptorInfos.isNotEmpty()) {
            body.addStatement("// 注册拦截器")
            body.addStatement("InterceptorRegistry.registerInterceptors(moduBox)")
        }
        
        if (serviceInfos.isNotEmpty()) {
            body.addStatement("// 注册服务")
            body.addStatement("ServiceRegistry.registerServices(moduBox)")
        }
        
        if (moduleInfos.isNotEmpty()) {
            body.addStatement("// 注册模块")
            body.addStatement("ModuleRegistry.registerModules(moduBox)")
        }
        
        if (componentInfos.isNotEmpty()) {
            body.addStatement("// 注册组件")
            body.addStatement("ComponentRegistry.registerComponents(moduBox)")
        }
        
        return FunSpec.builder("registerAll")
            .addModifiers(KModifier.PUBLIC)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("moduBox", ClassName("com.kernelflux.modubox.core.api", "ModuBox"))
            .addCode(body.build())
            .build()
    }
    
    private fun generateUnifiedInfoMethod(): FunSpec {
        val body = CodeBlock.builder()
            .addStatement("val info = mutableMapOf<String, Any>()")
            .addStatement("")
        
        if (pluginInfos.isNotEmpty()) {
            body.addStatement("// 插件信息")
            body.addStatement("info[\"plugins\"] = PluginRegistry.getPluginInfo()")
        }
        
        if (routeInfos.isNotEmpty()) {
            body.addStatement("// 路由信息")
            body.addStatement("info[\"routes\"] = RouteRegistry.getRouteInfo()")
        }
        
        if (routeInterceptorInfos.isNotEmpty()) {
            body.addStatement("// 拦截器信息")
            body.addStatement("info[\"interceptors\"] = InterceptorRegistry.getInterceptorInfo()")
        }
        
        if (serviceInfos.isNotEmpty()) {
            body.addStatement("// 服务信息")
            body.addStatement("info[\"services\"] = ServiceRegistry.getServiceInfo()")
        }
        
        if (moduleInfos.isNotEmpty()) {
            body.addStatement("// 模块信息")
            body.addStatement("info[\"modules\"] = ModuleRegistry.getModuleInfo()")
        }
        
        if (componentInfos.isNotEmpty()) {
            body.addStatement("// 组件信息")
            body.addStatement("info[\"components\"] = ComponentRegistry.getComponentInfo()")
        }
        
        body.addStatement("")
        body.addStatement("return info")
        
        return FunSpec.builder("getRegistrationInfo")
            .addModifiers(KModifier.PUBLIC)
            .addModifiers(KModifier.OVERRIDE)
            .returns(MAP_STRING_ANY)
            .addCode(body.build())
            .build()
    }
    
    private fun hasAnyAnnotations(): Boolean {
        return pluginInfos.isNotEmpty() || routeInfos.isNotEmpty() || routeInterceptorInfos.isNotEmpty() || 
               serviceInfos.isNotEmpty() || moduleInfos.isNotEmpty() || componentInfos.isNotEmpty()
    }

}

/**
 * ModuBox KSP处理器提供者
 */
class ModuBoxProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ModuBoxProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger
        )
    }
}
