package se.refur.javalin.methods

import io.javalin.Javalin
import io.javalin.core.security.RouteRole
import io.javalin.http.Context
import io.javalin.http.Handler
import io.javalin.http.HandlerType
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
     */
    open fun getWebServerRoute(): String = throw Exception("Override me")

    /**
     * Get the type of handler for web server, f.x. POST, GET, PUT etc
     */
    open fun getWebServerHandlerType(): HandlerType = throw Exception("Override me")

    /**
     * Generate the web server route handler
     */
    open fun generateWebServerHandler(): Handler = throw Exception("Override me")

    /**
     * Get roles for access
     */
    open fun getAccessRole(): RouteRole = throw Exception("Override me")

    /**
     * Map all annotated parameters to its typed value
     */
    fun mapParametersToTypeArguments(ctx: Context): List<Any> =
        annotatedParameters.map { it.getTypedValue(ctx) }

    /**
     * Add route handler to web server
     */
    fun addHandler(javalin: Javalin) {
        javalin.addHandler(
            getWebServerHandlerType(),
            getWebServerRoute(),
            generateWebServerHandler(),
            getAccessRole())
    }
}
