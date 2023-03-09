package se.refur.javalin.methods

import io.javalin.http.Handler
import io.javalin.http.HandlerType
import io.javalin.http.contextResolver
import se.refur.javalin.Api
import java.lang.reflect.Method
import java.util.concurrent.CompletableFuture

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
        val response: Any = annotationMethod.invoke(obj, *args.toTypedArray())
        //ctx.status(200).json(response).contextResolver()
        ctx.status(200)
            .future(CompletableFuture.completedFuture(response))
            .contextResolver()
    }
}