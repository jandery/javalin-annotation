package se.refur.javalin

import io.javalin.Javalin
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import java.lang.reflect.Method
import kotlin.reflect.KClass
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import se.refur.javalin.methods.ApiCookieMethod
import se.refur.javalin.methods.ApiMethod
import se.refur.javalin.methods.PageMethod

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
 * The purpose of this extension function is to add endpoint handlers for web page requests
 * and api requests
 * @param clazz class where to look for annotated methods
 * @return extended object for chaining
 */
fun <T : Any>Javalin.exposeHandler(clazz: KClass<T>): Javalin {
    val configurationBuilder: ConfigurationBuilder = ConfigurationBuilder()
        // set where to search
        .setUrls(ClasspathHelper.forClass(clazz.java))
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
    // Get all methods annotated for API handler
    getAnnotatedMethods(configurationBuilder, Api::class)
        // Map to instance
        .map { ApiMethod(it) }
        // Create handler to expose
        .forEach { it.addHandler(javalin) }

    // Get all methods annotated for setting Cookies
    getAnnotatedMethods(configurationBuilder, ApiCookie::class)
        // Map to instance
        .map { ApiCookieMethod(it) }
        // Create handler to expose
        .forEach { it.addHandler(javalin) }

    // Get all methods annotated for Web Page handler
    getAnnotatedMethods(configurationBuilder, Page::class)
        // Map to instance
        .map { PageMethod(it) }
        // Create handler to expose for generating HTML
        .forEach { it.addHandler(javalin) }
}

/**
 * The purpose of this function is getting all annotated methods in a specified package
 * @param configurationBuilder where to look for annotated methods
 * @param annotationClass type of annotation to search for
 * @return set of method with argument annotation in argument package
 */
private fun <T : Annotation>getAnnotatedMethods(configurationBuilder: ConfigurationBuilder, annotationClass: KClass<T>): Set<Method> =
    Reflections(configurationBuilder).getMethodsAnnotatedWith(annotationClass.java)

