package se.refur.javalin

import io.javalin.http.HandlerType
import java.time.LocalDate

/**
 * The purpose of this class is annotations for testing
 */
class AnnotatedClass {
    @Api(type = HandlerType.GET, path = "/api/empty", accessRole = "PUBLIC")
    fun apiEmptyArgumentMethod(): String {
        return "Nothing to see"
    }

    @Api(type = HandlerType.POST, path = "/api/non-empty", accessRole = "ADMIN")
    fun apiArgumentsMethod(
        @Param("routeParam", ParameterType.ROUTE) routeParam: LocalDate,
        @Param("queryParam", ParameterType.QUERY) queryParam: Int,
        @Param("formParam", ParameterType.FORM) formParam: Boolean
    ): String {
        return "$routeParam $queryParam $formParam"
    }

    @Page(type = HandlerType.GET, path = "/page/empty", templatePath = "/ftl/empty", accessRole = "PUBLIC")
    fun pageEmptyArgumentMethod(): String {
        return "Nothing to see"
    }

    @Page(type = HandlerType.POST, path = "/page/non-empty", templatePath = "/ftl/non-empty", accessRole = "ADMIN")
    fun pageArgumentsMethod(
        @Param("routeParam", ParameterType.ROUTE) routeParam: LocalDate,
        @Param("queryParam", ParameterType.QUERY) queryParam: Int,
        @Param("formParam", ParameterType.FORM) formParam: Boolean
    ): String {
        return "$routeParam $queryParam $formParam"
    }
}