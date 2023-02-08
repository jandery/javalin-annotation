package se.refur.javalin.methods

import io.javalin.http.Context
import io.javalin.http.HandlerType
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import se.refur.javalin.Page
import se.refur.javalin.Param
import se.refur.javalin.ParameterType
import java.lang.reflect.Method
import java.time.LocalDate
import kotlin.reflect.jvm.javaMethod

class PageMethodTest {

    private val emptyParamMethod: PageMethod
    private val paramMethod: PageMethod

    init {
        val emptyParamJavaMethod: Method = MyPageHandlerCls::emptyArgumentMethod.javaMethod
            ?: throw Exception("An error occurred")
        emptyParamMethod = PageMethod(emptyParamJavaMethod)

        val paramJavaMethod: Method = MyPageHandlerCls::argumentsMethod.javaMethod
            ?: throw Exception("An error occurred")
        paramMethod = PageMethod(paramJavaMethod)
    }

    private val emptyArgumentContext = mockk<Context>()
    private val argmentedContext = mockk<Context>().also {
        every { it.pathParam("routeParam") } returns "2020-10-20"
        every { it.queryParamAsClass("queryParam", Int::class.java).getOrDefault(-1) } returns 5
        every { it.formParamAsClass("formParam", Boolean::class.java).get() } returns false
    }

    @Test
    fun getWebServerRoute_emptyArgument_testEmptyArgument() {
        assertThat(emptyParamMethod.getWebServerRoute()).isEqualTo("/test/empty/argument")
    }

    @Test
    fun getWebServerHandlerType_emptyArgument_GET() {
        assertThat(emptyParamMethod.getWebServerHandlerType()).isEqualTo(HandlerType.GET)
    }

    @Test
    fun mapParametersToTypeArguments_emptyArgument_emptyList() {
        assertThat(emptyParamMethod.mapParametersToTypeArguments(emptyArgumentContext))
            .isEmpty()
    }

    @Test
    fun emptyArgumentMethod_emptyArgument_nothingToSee() {
        val response = MyPageHandlerCls().emptyArgumentMethod()
        assertThat(response).isEqualTo("Nothing to see")
    }

    @Test
    fun getWebServerRoute_argumentMethod_testEmptyArgument() {
        assertThat(paramMethod.getWebServerRoute()).isEqualTo("/test/non-empty/argument")
    }

    @Test
    fun getWebServerHandlerType_argumentMethod_GET() {
        assertThat(paramMethod.getWebServerHandlerType()).isEqualTo(HandlerType.POST)
    }

    @Test
    fun mapParametersToTypeArguments_argumentMethod_mapWithSize3() {
        assertThat(paramMethod.mapParametersToTypeArguments(argmentedContext))
            .hasSize(3)
    }

    @Test
    fun emptyArgumentMethod_argumentMethod_nothingToSee() {
        val response = MyPageHandlerCls()
            .argumentsMethod(LocalDate.parse("2020-10-20"), 5, false)
        assertThat(response).isEqualTo("2020-10-20 5 false")
    }
}

private class MyPageHandlerCls {

    @Page(
        type = HandlerType.GET,
        path = "/test/empty/argument",
        templatePath = "/ftl/empty/argument",
        accessRole = "PUBLIC"
    )
    fun emptyArgumentMethod(): String {
        return "Nothing to see"
    }

    @Page(
        type = HandlerType.POST,
        path = "/test/non-empty/argument",
        templatePath = "/ftl/non-empty/argument",
        accessRole = "ADMIN"
    )
    fun argumentsMethod(
        @Param("routeParam", ParameterType.ROUTE) routeParam: LocalDate,
        @Param("queryParam", ParameterType.QUERY) queryParam: Int,
        @Param("formParam", ParameterType.FORM) formParam: Boolean
    ): String {
        return "$routeParam $queryParam $formParam"
    }
}