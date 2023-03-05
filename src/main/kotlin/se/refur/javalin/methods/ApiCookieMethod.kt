package se.refur.javalin.methods

import io.javalin.core.security.RouteRole
import io.javalin.http.Handler
import io.javalin.http.HandlerType
import se.refur.javalin.ApiCookie
import se.refur.javalin.JavalinAnnotation
import java.lang.reflect.Method

/**
 * The purpose of this class is generate web server handler for an API route with purpose of
 * setting cookies
 */
internal class ApiCookieMethod(method: Method) : AnnotatedMethod(method) {
    /**
     * Annotation for method
     */
    private val annotation: ApiCookie = method.getAnnotation(ApiCookie::class.java)

    /**
     * Api route
     * @see AnnotatedMethod.getWebServerRoute
     */
    override fun getWebServerRoute(): String = annotation.path

    /**
     * Api route type
     * @see AnnotatedMethod.getWebServerHandlerType
     */
    override fun getWebServerHandlerType(): HandlerType = annotation.type

    /**
     * Route access
     * @see AnnotatedMethod.getAccess
     */
    override fun getAccess(): List<String> = listOf(annotation.accessRole)

    /**
     * Generate handler for the api call
     * @see AnnotatedMethod.generateWebServerHandler
     */
    @Suppress("UNCHECKED_CAST")
    override fun generateWebServerHandler(): Handler = Handler { ctx ->
        // Get arguments from annotated parameters
        val args = mapParametersToTypeArguments(ctx)
        // Call method with typed arguments
        val cookies: Map<String, String> = annotationMethod
            .invoke(obj, *args.toTypedArray()) as Map<String, String>
        // Set cookies
        cookies.forEach { (cookieName: String, cookieValue: String) ->
            ctx.cookie(cookieName, cookieValue)
        }
        ctx.status(200)
    }
}