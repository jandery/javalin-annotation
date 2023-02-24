package se.refur.javalin

import io.javalin.Javalin
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder
import se.refur.javalin.methods.ApiCookieMethod
import se.refur.javalin.methods.ApiMethod
import se.refur.javalin.methods.PageMethod
import java.lang.reflect.Method
import kotlin.reflect.KClass

/**
 * The purpose of this extension function is to add endpoint handlers for web page requests
 * and api requests
 * @param packageName package where to look for annotated methods
 * @return extended object for chaining
 */
@Suppress("unused")
fun Javalin.exposePackageEndpoints(packageName: String): Javalin {
    // Not really working as I thought
    // For package refers to /target/classes and not to the actual package /target/classes/sample/first/etc
    val configurationBuilder: ConfigurationBuilder = ConfigurationBuilder()
        .forPackages(packageName)
        .setScanners(Scanners.MethodsAnnotated)

    getAnnotatedMethods(configurationBuilder, Api::class)
        .filter { it.isInPackage(packageName) }
        .map { ApiMethod(it) }
        .forEach { it.addHandler(this) }

    getAnnotatedMethods(configurationBuilder, ApiCookie::class)
        .filter { it.isInPackage(packageName) }
        .map { ApiCookieMethod(it) }
        .forEach { it.addHandler(this) }

    getAnnotatedMethods(configurationBuilder, Page::class)
        .filter { it.isInPackage(packageName) }
        .map { PageMethod(it) }
        .forEach { it.addHandler(this) }

    // Return this for chaining
    return this
}

/**
 * The purpose of this function is getting all annotated methods in a specified package
 * @param configurationBuilder where to look for annotated methods
 * @param annotationClass type of annotation to search for
 * @return set of method with argument annotation in argument package
 */
private fun <T : Annotation> getAnnotatedMethods(
    configurationBuilder: ConfigurationBuilder, annotationClass: KClass<T>
): Set<Method> = Reflections(configurationBuilder).getMethodsAnnotatedWith(annotationClass.java)


/**
 * The purpose of this extension function is to make sure a method's declaring class is in the argument package
 */
private fun Method.isInPackage(packageName: String?): Boolean =
    packageName == null || this.declaringClass.packageName == packageName