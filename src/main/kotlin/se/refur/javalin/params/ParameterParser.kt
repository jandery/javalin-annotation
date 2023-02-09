package se.refur.javalin.params

import io.javalin.http.Context
import java.time.LocalDate

/**
 * The purpose of this interface is funtion for parsing a parameter to a specific type
 */
internal interface IParameterParser {
    fun getTypedValue(ctx: Context, paramName: String): Any
}

/**
 * Parse route parameter as String
 * f.x. /api/{type} is /api/myType when paramName is "type"
 */
internal object RouteParamAsString : IParameterParser {
    override fun getTypedValue(ctx: Context, paramName: String): Any =
        ctx.pathParamAsClass(paramName, String::class.java).get()
}

/**
 * Parse route parameter as Integer
 * f.x. /api/{age} is /api/55 when paramName is "age"
 */
internal object RouteParamAsInt : IParameterParser {
    override fun getTypedValue(ctx: Context, paramName: String): Any =
        ctx.pathParamAsClass(paramName, Int::class.java).get()
}

/**
 * Parse route parameter as LocalDate
 * f.x. /api/{date} is /api/2022-10-20 when paramName is "date"
 * Javalin Validator only supports primitives. Other types must be handled separately.
 * @see [io.javalin.core.validation.JavalinValidation]
 */
internal object RouteParamAsLocalDate : IParameterParser {
    private val parser: (Context, String) -> Any = { ctx: Context, paramName: String ->
        val stringValue = ctx.pathParam(paramName)
        LocalDate.parse(stringValue)
    }

    override fun getTypedValue(ctx: Context, paramName: String): Any =
        parseWithException(parser, ctx, paramName, LocalDate::class.simpleName)
}

/**
 * Parse query parameter as String
 * f.x. /api?type=myType when paramName is "type" returns myType
 */
internal object QueryParamAsString : IParameterParser {
    override fun getTypedValue(ctx: Context, paramName: String): Any =
        ctx.queryParamAsClass(paramName, String::class.java).getOrDefault("")
}

/**
 * Parse query parameter as Int
 * f.x. /api?age=55 when paramName is "age" returns 55
 */
internal object QueryParamAsInt : IParameterParser {
    override fun getTypedValue(ctx: Context, paramName: String): Any =
        ctx.queryParamAsClass(paramName, Int::class.java).getOrDefault(-1)
}

/**
 * Parse query parameter as LocalDate
 * f.x. /api?date=2022-10-20
 * Javalin Validator only supports primitives. Other types must be handled separately.
 * @see [io.javalin.core.validation.JavalinValidation]
 */
internal object QueryParamAsLocalDate : IParameterParser {
    private val parser: (Context, String) -> Any = { ctx: Context, paramName: String ->
        val stringValue = ctx.queryParam(paramName)
        LocalDate.parse(stringValue)
    }

    override fun getTypedValue(ctx: Context, paramName: String): Any =
        parseWithException(parser, ctx, paramName, LocalDate::class.simpleName)
}

/**
 * Parse a form parameter as String
 * f.x. jQuery.ajax({data:{strValue:"aValue",intValue:42,dateValue:"2022-10-20",boolValue:true}})
 */
internal object FormParamAsString : IParameterParser {
    override fun getTypedValue(ctx: Context, paramName: String): Any =
        ctx.formParamAsClass(paramName, String::class.java).get()
}

/**
 * Parse a form parameter as Int
 * f.x. jQuery.ajax({data:{strValue:"aValue",intValue:42,dateValue:"2022-10-20",boolValue:true}})
 */
internal object FormParamAsInt : IParameterParser {
    override fun getTypedValue(ctx: Context, paramName: String): Any =
        ctx.formParamAsClass(paramName, Int::class.java).get()
}

/**
 * Parse a form parameter as LocalDate
 * f.x. jQuery.ajax({data:{strValue:"aValue",intValue:42,dateValue:"2022-10-20",boolValue:true}})
 */
internal object FormParamAsLocalDate : IParameterParser {
    private val parser: (Context, String) -> Any = { ctx: Context, paramName: String ->
        val stringValue = ctx.formParam(paramName)
        LocalDate.parse(stringValue)
    }

    override fun getTypedValue(ctx: Context, paramName: String): Any =
        parseWithException(parser, ctx, paramName, LocalDate::class.simpleName)
}

/**
 * Parse a form parameter as Boolean
 * f.x. jQuery.ajax({data:{strValue:"aValue",intValue:42,dateValue:"2022-10-20",boolValue:true}})
 */
internal object FormParamAsBoolean : IParameterParser {
    override fun getTypedValue(ctx: Context, paramName: String): Any =
        ctx.formParamAsClass(paramName, Boolean::class.java).get()
}

/**
 * Read a cookie
 */
internal object CookieValueAsString : IParameterParser {
    override fun getTypedValue(ctx: Context, paramName: String): Any =
        ctx.cookie(paramName) ?: ""
}


/**
 * Parse parameter and leave error message understandable for UI
 */
private fun parseWithException(
    parse: (Context, String) -> Any,
    ctx: Context,
    paramName: String,
    expectedType: String?
): Any {
    try {
        return parse(ctx, paramName)
    } catch (e: Exception) {
        throw Exception("Could not parse '$paramName' to '$expectedType'")
    }
}
