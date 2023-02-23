package se.refur.javalin

import io.javalin.Javalin
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import kotlin.reflect.KClass

/**
 * The purpose of this extension function is to add endpoint handlers for web page requests
 * and api requests
 * @param packageName package where to look for annotated methods
 * @return extended object for chaining
 */
@Suppress("unused")
fun Javalin.exposePackageEndpoints(packageName: String): Javalin {
    val configurationBuilder: ConfigurationBuilder = ConfigurationBuilder()
        // set where to search
        .setUrls(ClasspathHelper.forPackage(packageName))
        // set what to search for
        .setScanners(Scanners.MethodsAnnotated)

    // expose annotated methods
    exposeMethods(configurationBuilder, this)
    // Return this for chaining
    return this
}

@Suppress("unused")
fun <T : Any>Javalin.exposeClassEndpoints(clazz: KClass<T>): Javalin {
    val configurationBuilder: ConfigurationBuilder = ConfigurationBuilder()
        .addClassLoaders(clazz.java.classLoader)
        .setScanners(Scanners.MethodsAnnotated)

    // expose annotated methods
    exposeMethods(configurationBuilder, this)
    // Return this for chaining
    return this
}

/**
 * The purpose of this function is to expose methods
 */
private fun exposeMethods(configurationBuilder: ConfigurationBuilder, javalin: Javalin) {
    // Add handlers to webserver for Api annotations
    getApiMethods(configurationBuilder).forEach { it.addHandler(javalin) }
    // Add handlers to webserver for ApiCookie annotations
    getApiCookieMethods(configurationBuilder).forEach { it.addHandler(javalin) }
    // Add handlers to webserver for Page annotations
    getPageMethods(configurationBuilder).forEach { it.addHandler(javalin) }
}



