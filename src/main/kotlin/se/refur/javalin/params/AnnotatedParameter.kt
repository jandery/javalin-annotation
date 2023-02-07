package se.refur.javalin.params

import io.javalin.http.Context
import se.refur.javalin.Param
import se.refur.javalin.ParameterType
import java.lang.reflect.Parameter
import java.time.LocalDate

/**
 * The purpose of this class is to encapsulate a parameter for kotlin function
 */
class AnnotatedParameter(parameter: Parameter) {
    // Annotation for parameter
    private val annotation: Param = parameter.getAnnotation(Param::class.java)

    // Type of value, f.x. String, Int, Boolean, etc.
    private val parameterValueTypeName: String = parameter.type.simpleName

    /**
     * Get parameter as its actual value
     */
    fun getTypedValue(ctx: Context): Any {
        try {
            // Get parameter parser from type of parameter and type of parameter value
            val parser = parameterParsers[Pair(annotation.parameterType, parameterValueTypeName)]
            // If we found parser, get typed value
            return parser?.getTypedValue(ctx, annotation.paramName)
            // No parser found
                ?: throw Exception("Could not find parser for ${annotation.parameterType} and $parameterValueTypeName")
        } catch (e: Exception) {
            throwIllegalArgument(e)
        }
        return ""
    }

    /**
     * Not able to parse parameter
     */
    private fun throwIllegalArgument(e: Exception) {
        throw IllegalArgumentException("Could not parse '${annotation.paramName}' as '$parameterValueTypeName'.", e)
    }

    companion object {
        /**
         * Function to filter parameters with annotation "RouteParam"
         */
        fun filterAnnotatedParams(params: Array<Parameter>): List<AnnotatedParameter> =
            params
                .filter { it.isAnnotationPresent(Param::class.java) }
                .map { AnnotatedParameter(it) }
    }
}

/**
 * Available parsers based on parameter type and type of response
 */
private val parameterParsers: Map<Pair<ParameterType, String>, IParameterParser> = mapOf(
    Pair(ParameterType.ROUTE, String::class.java.simpleName) to RouteParamAsString,
    Pair(ParameterType.ROUTE, Int::class.java.simpleName) to RouteParamAsInt,
    Pair(ParameterType.ROUTE, LocalDate::class.java.simpleName) to RouteParamAsLocalDate,
    Pair(ParameterType.QUERY, String::class.java.simpleName) to QueryParamAsString,
    Pair(ParameterType.QUERY, Int::class.java.simpleName) to QueryParamAsInt,
    Pair(ParameterType.FORM, String::class.java.simpleName) to FormParamAsString,
    Pair(ParameterType.FORM, Int::class.java.simpleName) to FormParamAsInt,
    Pair(ParameterType.FORM, Boolean::class.java.simpleName) to FormParamAsBoolean,
    Pair(ParameterType.COOKIE, String::class.java.simpleName) to CookieValueAsString
)