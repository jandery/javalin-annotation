package se.refur.javalin.methods

import io.javalin.http.ContentType
import io.javalin.http.Handler
import io.javalin.http.HandlerType
import se.refur.javalin.Css
import java.lang.reflect.Method

/**
 * The purpose of this class is to generate web server handler for a CSS request
 */
internal class CssMethod(method: Method) : AnnotatedMethod(method) {
    /**
     * Annotation for method
     */
    private val annotation: Css = method.getAnnotation(Css::class.java)

    /**
     * Api route
     * @see AnnotatedMethod.getWebServerRoute
     */
    override fun getWebServerRoute(): String = annotation.path

    /**
     * Api route type
     * @see AnnotatedMethod.getWebServerHandlerType
     */
    override fun getWebServerHandlerType(): HandlerType = HandlerType.GET

    /**
     * Route access
     * @see AnnotatedMethod.getAccess
     */
    override fun getAccess(): List<String> = emptyList()

    /**
     * Generate handler for the api call
     * @see AnnotatedMethod.generateWebServerHandler
     */
    override fun generateWebServerHandler(): Handler = Handler { ctx ->
        val response: String = annotationMethod.invoke(obj) as String
        ctx.status(200).contentType(ContentType.TEXT_CSS).result(response)
    }
}