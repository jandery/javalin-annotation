package se.refur.javalin

import io.javalin.Javalin
import org.reflections.Reflections
import org.reflections.scanners.Scanners
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
    getAnnotatedMethods(packageName, Api::class)
        .map { ApiMethod(it) }
        .forEach { it.addHandler(this) }

    getAnnotatedMethods(packageName, ApiCookie::class)
        .map { ApiCookieMethod(it) }
        .forEach { it.addHandler(this) }

    getAnnotatedMethods(packageName, Page::class)
        .map { PageMethod(it) }
        .forEach { it.addHandler(this) }

    // Return this for chaining
    return this
}

/**
 * The purpose of this function is getting all annotated methods in a specified package
 * @param packageName where to look for annotated methods
 * @param annotationClass type of annotation to search for
 * @return set of method with argument annotation in argument package
 */
private fun <T : Annotation> getAnnotatedMethods(packageName: String, annotationClass: KClass<T>): Set<Method> =
    Reflections(packageName, Scanners.MethodsAnnotated).getMethodsAnnotatedWith(annotationClass.java)
