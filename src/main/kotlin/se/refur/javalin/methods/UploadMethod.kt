package se.refur.javalin.methods

import io.javalin.http.Handler
import io.javalin.http.HandlerType
import se.refur.javalin.Upload
import java.lang.reflect.Method

/**
 * The purpose of this class is generate web server handler for file upload
 */
internal class UploadMethod(method: Method) : AnnotatedMethod(method) {
    /**
     * Annotation for method
     */
    private val annotation: Upload = method.getAnnotation(Upload::class.java)

    /**
     * Page route
     * @see AnnotatedMethod.getWebServerRoute
     */
    override fun getWebServerRoute(): String = annotation.path

    /**
     * Page route type
     * @see AnnotatedMethod.getWebServerHandlerType
     */
    override fun getWebServerHandlerType(): HandlerType = HandlerType.POST

    /**
     * Route access
     * @see AnnotatedMethod.getAccess
     */
    override fun getAccess(): List<String> = listOf(annotation.accessRole)

    /**
     * Generate handler for uploading files
     */
    override fun generateWebServerHandler(): Handler = Handler { ctx ->
        // Get arguments from annotated parameters
        val args = mapParametersToTypeArguments(ctx)
        // Call method with typed arguments
        val response: Any = annotationMethod.invoke(obj, *args.toTypedArray())
        // Render result
        ctx.status(200).json(response)
    }
}