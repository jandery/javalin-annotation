package se.refur.javalin

import io.javalin.Javalin
import se.refur.javalin.methods.*
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

    allClassMethods.filter { it.isAnnotationPresent(Download::class.java) }
        .map { DownloadMethod(it) }
        .forEach { it.addHandler(this) }

    allClassMethods.filter { it.isAnnotationPresent(Upload::class.java) }
        .map { UploadMethod(it) }
        .forEach { it.addHandler(this) }

    allClassMethods.filter { it.isAnnotationPresent(Css::class.java) }
        .map { CssMethod(it) }
        .forEach { it.addHandler(this) }

    allClassMethods.filter { it.isAnnotationPresent(Js::class.java) }
        .map { JsMethod(it) }
        .forEach { it.addHandler(this) }

    // Return this for chaining
    return this
}