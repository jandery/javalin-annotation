package se.refur.javalin.params

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import se.refur.javalin.AnnotatedClass
import kotlin.reflect.jvm.javaMethod


class AnnotatedParameterTest {

    @Test
    fun filterAnnotatedParams_methodWithParams_3() {
        val parameters: List<AnnotatedParameter> = AnnotatedClass::apiArgumentsMethod
            .javaMethod
            ?.let { AnnotatedParameter.filterAnnotatedParams(it.parameters) }
            ?: emptyList()
        assertThat(parameters).hasSize(3)
    }

    @Test
    fun filterAnnotatedParams_methodWithoutParams_0() {
        val parameters: List<AnnotatedParameter> = AnnotatedClass::apiEmptyArgumentMethod
            .javaMethod
            ?.let { AnnotatedParameter.filterAnnotatedParams(it.parameters) }
            ?: emptyList()
        assertThat(parameters).hasSize(0)
    }
}