package se.refur.javalin.sample.third

import io.javalin.http.HandlerType
import se.refur.javalin.Api
import se.refur.javalin.Page


class ThirdExposedClass {

    @Page(type = HandlerType.GET, path = "/third", templatePath = "")
    fun pageHandler(): Map<String, Any> = emptyMap()

    @Api(type = HandlerType.GET, path = "/api/third")
    fun apiHandler(): String = "API response for ThirdExposedClass"

}