package se.refur.javalin

import io.javalin.http.HandlerType


/**
 * The purpose of this annotation is to register a web page endpoint
 * Function annotated with this is expected to return the rendering data map for template,
 *      Map<String,Any>
 * @property type Type of endpoint
 * @property path Route path for endpoint
 * @property templatePath path for Freemarker template
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Page(val type: HandlerType, val path: String, val templatePath: String, val accessRole: String)

/**
 * The purpose of this annotation is to register an API endpoint
 * Return for functions annotated with this is expected to return String
 * @property type Type of endpoint
 * @property path Route path for endpoint
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Api(val type: HandlerType, val path: String, val accessRole: String)

/**
 * The purpose of this annotation is to register an API endpoint that should set one or more cookies
 * Functions annotated with this is expected to return a map of cookie name to cookie value
 * @property type Type of endpoint
 * @property path Route path for endpoint
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiCookie(val type: HandlerType, val path: String, val accessRole: String)

/**
 * The purpose of this annotation is to handle properties for above annotated functions
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Param(val paramName: String, val parameterType: ParameterType)



/**
 * The purpose of this enum is hold types of parameters
 * @property ROUTE route parameter, f.x. /api/{PARAM}
 * @property QUERY query parameter, f.x. /api?{PARAM}=value
 * @property FORM form parameter, f.x. in JS ServerCaller("/api", "POST").addArg("{PARAM}", "value")
 * @property COOKIE stored cookie
 */
enum class ParameterType {
    ROUTE, QUERY, FORM, COOKIE
}