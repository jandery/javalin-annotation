package se.refur.javalin.methods

import io.javalin.http.Context
import io.javalin.http.HandlerType
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import se.refur.javalin.AnnotatedClass
import java.lang.reflect.Method
import java.time.LocalDate
import kotlin.reflect.jvm.javaMethod

class ApiMethodTest {
    private val emptyParamMethod: ApiMethod
    private val paramMethod: ApiMethod

    init {
        val emptyParamJavaMethod: Method = AnnotatedClass::apiEmptyArgumentMethod.javaMethod
            ?: throw Exception("An error occurred")
        emptyParamMethod = ApiMethod(emptyParamJavaMethod)

        val paramJavaMethod: Method = AnnotatedClass::apiArgumentsMethod.javaMethod
            ?: throw Exception("An error occurred")
        paramMethod = ApiMethod(paramJavaMethod)
    }

    private val emptyArgumentContext = mockk<Context>()
    private val argContext = mockk<Context>().also {
        every { it.pathParam("routeParam") } returns "2020-10-20"
        every { it.queryParamAsClass("queryParam", Int::class.java).getOrDefault(-1) } returns 5
        every { it.formParamAsClass("formParam", Boolean::class.java).get() } returns false
    }

    @Test
    fun getWebServerRoute_emptyParam_testEmptyArgument() {
        assertThat(emptyParamMethod.getWebServerRoute()).isEqualTo("/api/empty")
    }

    @Test
    fun getWebserverHandler_emptyParam_GET() {
        assertThat(emptyParamMethod.getWebServerHandlerType()).isEqualTo(HandlerType.GET)
    }

    @Test
    fun mapParametersToTypeArguments_emptyParam_emptyList() {
        assertThat(emptyParamMethod.mapParametersToTypeArguments(emptyArgumentContext))
            .isEmpty()
    }

    @Test
    fun emptyArgumentMethod_emptyArgument_nothingToSee() {
        val response = AnnotatedClass().apiEmptyArgumentMethod()
        assertThat(response).isEqualTo("Nothing to see")
    }

    @Test
    fun getWebServerRoute_argumentMethod_testEmptyArgument() {
        assertThat(paramMethod.getWebServerRoute()).isEqualTo("/api/non-empty")
    }

    @Test
    fun getWebServerHandlerType_argumentMethod_GET() {
        assertThat(paramMethod.getWebServerHandlerType()).isEqualTo(HandlerType.POST)
    }

    @Test
    fun mapParametersToTypeArgument_argumentMethod_mapWithSize3() {
        assertThat(paramMethod.mapParametersToTypeArguments(argContext))
            .hasSize(3)
    }

    @Test
    fun emptyArgumentMethod_argumentMethod_nothingToSee() {
        val response = AnnotatedClass()
            .apiArgumentsMethod(LocalDate.parse("2020-10-20"), 5, false)
        assertThat(response).isEqualTo("2020-10-20 5 false")
    }
}