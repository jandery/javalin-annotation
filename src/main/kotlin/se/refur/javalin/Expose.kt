package se.refur.javalin

import org.reflections.Reflections
import org.reflections.util.ConfigurationBuilder
import se.refur.javalin.methods.ApiCookieMethod
import se.refur.javalin.methods.ApiMethod
import se.refur.javalin.methods.PageMethod
import java.lang.reflect.Method
import kotlin.reflect.KClass

/**
 * Get all methods annotated with Api
 */
internal fun getApiMethods(configurationBuilder: ConfigurationBuilder): List<ApiMethod> =
    getAnnotatedMethods(configurationBuilder, Api::class)
        .map { ApiMethod(it) }

/**
 * Get all methods annotated with ApiCookie
 */
internal fun getApiCookieMethods(configurationBuilder: ConfigurationBuilder): List<ApiCookieMethod> =
    getAnnotatedMethods(configurationBuilder, ApiCookie::class)
        .map { ApiCookieMethod(it) }

/**
 * Get all methods annotated with Page
 */
internal fun getPageMethods(configurationBuilder: ConfigurationBuilder): List<PageMethod> =
    getAnnotatedMethods(configurationBuilder, Page::class)
        .map { PageMethod(it) }

/**
 * The purpose of this function is getting all annotated methods in a specified package
 * @param configurationBuilder where to look for annotated methods
 * @param annotationClass type of annotation to search for
 * @return set of method with argument annotation in argument package
 */
private fun <T : Annotation>getAnnotatedMethods(configurationBuilder: ConfigurationBuilder, annotationClass: KClass<T>): Set<Method> =
    Reflections(configurationBuilder).getMethodsAnnotatedWith(annotationClass.java)