package se.refur.javalin

import io.javalin.Javalin
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder

/**
 * The purpose of this extension function is to add endpoint handlers for web page requests
 * and api requests
 * @param packageName package where to look for annotated methods
 * @return extended object for chaining
 */
fun Javalin.exposeHandlers(packageName: String): Javalin {
    val configurationBuilder: ConfigurationBuilder = ConfigurationBuilder()
        // set where to search
        .setUrls(ClasspathHelper.forPackage(packageName))
        // set what to search for
        .setScanners(Scanners.MethodsAnnotated)

    exposeMethods(configurationBuilder, this)
    // Return this for chaining
    return this
}

/**
 * The purpose of this function is to expose methods
 */
private fun exposeMethods(configurationBuilder: ConfigurationBuilder, javalin: Javalin) {
    getApiMethods(configurationBuilder)
        .forEach { it.addHandler(javalin) }

    getApiCookieMethods(configurationBuilder)
        .forEach { it.addHandler(javalin) }

    getPageMethods(configurationBuilder)
        .forEach { it.addHandler(javalin) }
}


