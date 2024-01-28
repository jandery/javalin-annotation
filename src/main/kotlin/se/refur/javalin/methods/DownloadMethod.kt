package se.refur.javalin.methods

import io.javalin.http.Handler
import io.javalin.http.HandlerType
import se.refur.javalin.Download
import java.lang.reflect.Method

/**
 * The purpose of this class is to generate web server handler for file download
 */
internal class DownloadMethod(method: Method) : AnnotatedMethod(method) {
    /**
     * Annotation for method
     */
    private val annotation: Download = method.getAnnotation(Download::class.java)

    /**
     * Page route
     * @see AnnotatedMethod.getWebServerRoute
     */
    override fun getWebServerRoute(): String = annotation.path

    /**
     * Page route type
     * @see AnnotatedMethod.getWebServerHandlerType
     */
    override fun getWebServerHandlerType(): HandlerType = annotation.type

    /**
     * Route access
     * @see AnnotatedMethod.getAccess
     */
    override fun getAccess(): List<String> = listOf(annotation.accessRole)

    override fun generateWebServerHandler(): Handler = Handler { ctx ->
        // Get arguments from annotated parameters
        val args = mapParametersToTypeArguments(ctx)
        // Call method with typed arguments
        val fileContent: ByteArray = annotationMethod
            .invoke(obj, *args.toTypedArray()) as ByteArray

        ctx.status(200)
            .contentType(annotation.contentType)
            .header("Content-Disposition", "attachment;filename=${annotation.downloadAs}")
            .result(fileContent)
    }
}