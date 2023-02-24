package se.refur.javalin

import io.javalin.Javalin
import se.refur.javalin.methods.ApiCookieMethod
import se.refur.javalin.methods.ApiMethod
import se.refur.javalin.methods.PageMethod
import java.lang.reflect.Method
import kotlin.reflect.KClass

/**
 * The purpose of this extension function is to add endpoint handlers for web page requests
 * and api requests
 * @param clazz Kotlin KClass where annotated methods exists
 * @return extended object for chaining
 */
@Suppress("unused")
fun <T : Any> Javalin.exposeClassEndpoints(clazz: KClass<T>): Javalin =
    exposeClassEndpoints(clazz.java)

/**
 * The purpose of this extension function is to add endpoint handlers for web page requests
 * and api requests
 * @param clazz Java Class where annotated methods exists
 * @return extended object for chaining
 */
fun <T : Any> Javalin.exposeClassEndpoints(clazz: Class<T>): Javalin {
    val allClassMethods: Array<Method> = clazz.declaredMethods

    allClassMethods.filter { it.isAnnotationPresent(Api::class.java) }
        .map { ApiMethod(it) }
        .forEach { it.addHandler(this) }

    allClassMethods.filter { it.isAnnotationPresent(ApiCookie::class.java) }
        .map { ApiCookieMethod(it) }
        .forEach { it.addHandler(this) }

    allClassMethods.filter { it.isAnnotationPresent(Page::class.java) }
        .map { PageMethod(it) }
        .forEach { it.addHandler(this) }

    // Return this for chaining
    return this
}