package se.refur.javalin.methods

import io.javalin.core.security.RouteRole
import io.javalin.http.ContentType
import io.javalin.http.Handler
import io.javalin.http.HandlerType
import se.refur.javalin.Api
import se.refur.javalin.JavalinAnnotation
import java.lang.reflect.Method

/**
 * The purpose of this class is generate web server handler for an API route
 */
internal class ApiMethod(method: Method) : AnnotatedMethod(method) {
    /**
     * Annotation for method
     */
    private val annotation: Api = method.getAnnotation(Api::class.java)

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
    override fun generateWebServerHandler(): Handler = Handler { ctx ->
        // Get arguments from annotated parameters
        val args = mapParametersToTypeArguments(ctx)
        // Call method with typed arguments
        val response: String = annotationMethod.invoke(obj, *args.toTypedArray()) as String
        // Render result
        ctx.status(200)
            .contentType(ContentType.TEXT_HTML)
            .result(response)
    }
}