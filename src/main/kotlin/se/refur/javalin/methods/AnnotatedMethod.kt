package se.refur.javalin.methods

import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.http.Handler
import io.javalin.http.HandlerType
import io.javalin.security.RouteRole
import se.refur.javalin.JavalinAnnotation
import se.refur.javalin.params.AnnotatedParameter
import java.lang.reflect.Method

/**
 * The purpose of this open class is holding parameters and functions common
 * for holding ApiMethod and WebPageMethod
 */
internal open class AnnotatedMethod(val annotationMethod: Method) {
    /**
     * New instance of the calling class
     */
    protected val obj: Any = annotationMethod.declaringClass.getDeclaredConstructor().newInstance()

    /**
     * List of all annotated method
     */
    private val annotatedParameters: List<AnnotatedParameter> =
        AnnotatedParameter.filterAnnotatedParams(annotationMethod.parameters)

    /**
     * Get the web server route path
     * Example "/some/route"
     */
    open fun getWebServerRoute(): String = throw Exception("Override me")

    /**
     * Get the type of handler for web server
     * Example POST, GET, PUT, DELETE etc
     */
    open fun getWebServerHandlerType(): HandlerType = throw Exception("Override me")

    /**
     * Generate the web server endpoint handler
     */
    open fun generateWebServerHandler(): Handler = throw Exception("Override me")

    /**
     * Get roles for user access
     */
    open fun getAccess(): List<String> = throw Exception("Override me")

    /**
     * Map all annotated parameters to its typed value
     */
    fun mapParametersToTypeArguments(ctx: Context): List<Any> =
        annotatedParameters.map { it.getTypedValue(ctx) }

    /**
     * Add route handler to web server
     */
    fun addHandler(javalin: Javalin) {
        javalin.addHttpHandler(
            getWebServerHandlerType(),
            getWebServerRoute(),
            generateWebServerHandler(),
            *getAccessRoles())
    }

    private fun getAccessRoles(): Array<RouteRole> =
        getAccess()
            .filter {it.isNotEmpty() }
            .map { JavalinAnnotation.getRole(it) }
            .toTypedArray()
}
