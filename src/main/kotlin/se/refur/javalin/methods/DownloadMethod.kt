package se.refur.javalin.methods

import io.javalin.core.security.RouteRole
import io.javalin.http.Handler
import io.javalin.http.HandlerType
import se.refur.javalin.Download
import se.refur.javalin.JavalinAnnotation
import java.lang.reflect.Method

/**
 * The purpose of this
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
     * @see AnnotatedMethod.getAccessRole
     */
    override fun getAccessRole(): RouteRole = JavalinAnnotation.getRole(annotation.accessRole)


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