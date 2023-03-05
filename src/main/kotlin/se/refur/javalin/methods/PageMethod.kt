package se.refur.javalin.methods

import io.javalin.core.security.RouteRole
import io.javalin.http.Handler
import io.javalin.http.HandlerType
import se.refur.javalin.JavalinAnnotation
import se.refur.javalin.Page
import java.lang.reflect.Method

/**
 * The purpose of this class is generate web server handler for an Web page route, request for HTML
 */
internal class PageMethod(method: Method) : AnnotatedMethod(method) {
    /**
     * Annotation for method
     */
    private val annotation: Page = method.getAnnotation(Page::class.java)

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

    /**
     * Generate handler for the Page renderer
     * @see AnnotatedMethod.generateWebServerHandler
     */
    @Suppress("UNCHECKED_CAST")
    override fun generateWebServerHandler(): Handler = Handler { ctx ->
        // Get arguments from annotated parameters
        val args = mapParametersToTypeArguments(ctx)
        // Call method with typed arguments
        val templateDataMap: Map<String, Any> = annotationMethod
            .invoke(obj, *args.toTypedArray()) as Map<String, Any>
        // Render web page with template path and template data map
        ctx.status(200)
            .render(filePath = annotation.templatePath, model = templateDataMap)
    }
}
