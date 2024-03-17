package se.refur.javalin.methods

import io.javalin.http.ContentType
import io.javalin.http.Handler
import io.javalin.http.HandlerType
import se.refur.javalin.Js
import java.lang.reflect.Method

/**
 * The purpose of this class is to generate web server handler for a JavaScript request
 */
internal class JsMethod(method: Method) : AnnotatedMethod(method) {
    /**
     * Annotation for method
     */
    private val annotation: Js = method.getAnnotation(Js::class.java)

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
        ctx.status(200).contentType(ContentType.TEXT_JS).result(response)
    }
}