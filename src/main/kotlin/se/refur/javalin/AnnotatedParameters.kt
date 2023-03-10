package se.refur.javalin

/**
 * The purpose of this annotation is to handle properties for annotated methods
 * @property paramName name of parameter in type
 * @property parameterType type of parameter
 * @property description optional, description of parameter.
 * @see ParameterType
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Param(
    val paramName: String,
    val parameterType: ParameterType,
    val description: String = "")



/**
 * The purpose of this enum is hold types of parameters
 * @property ROUTE route parameter, f.x. /api/{PARAM}
 * @property QUERY query parameter, f.x. /api?{PARAM}=value
 * @property FORM form parameter, f.x. jquery.ajax({data:{PARAM:"value"}})
 * @property COOKIE stored cookie
 * @property FILE uploaded file
 */
enum class ParameterType {
    ROUTE, QUERY, FORM, COOKIE, FILE
}