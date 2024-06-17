package se.refur.javalin.params

import io.javalin.http.Context
import se.refur.javalin.AnnotationParserException
import java.time.LocalDate


/**
 * The purpose of this interface is function for parsing a parameter to a specific type
 */
internal interface IParameterParser {
    fun getTypedValue(ctx: Context, paramName: String): Any
}

/**
 * Parse route parameter as String
 * f.x. /api/{type} is /api/myType when paramName is "type"
 */
internal object RouteParamAsString : IParameterParser {
    private val parser: (Context, String) -> String = { ctx: Context, paramName: String ->
        ctx.pathParamAsClass(paramName, String::class.java).get()
    }

    override fun getTypedValue(ctx: Context, paramName: String): Any =
        parseWithException(parser, ctx, paramName, String::class.simpleName)
}

/**
 * Parse route parameter as Integer
 * f.x. /api/{age} is /api/55 when paramName is "age"
 */
internal object RouteParamAsInt : IParameterParser {
    private val parser: (Context, String) -> Int = { ctx: Context, paramName: String ->
        ctx.pathParamAsClass(paramName, Int::class.java).get()
    }

    override fun getTypedValue(ctx: Context, paramName: String): Any =
        parseWithException(parser, ctx, paramName, Int::class.simpleName)
}

/**
 * Parse route parameter as LocalDate
 * f.x. /api/{date} is /api/2022-10-20 when paramName is "date"
 * Javalin Validator only supports primitives. Other types must be handled separately.
 */
internal object RouteParamAsLocalDate : IParameterParser {
    private val parser: (Context, String) -> LocalDate = { ctx: Context, paramName: String ->
        parseToLocalDate(ctx.pathParam(paramName))
    }

    override fun getTypedValue(ctx: Context, paramName: String): Any =
        parseWithException(parser, ctx, paramName, LocalDate::class.simpleName)
}

/**
 * Parse query parameter as String
 * f.x. /api?type=myType when paramName is "type" returns myType
 */
internal object QueryParamAsString : IParameterParser {
    private val parser: (Context, String) -> String = { ctx: Context, paramName: String ->
        ctx.queryParamAsClass(paramName, String::class.java).getOrDefault("")
    }
    override fun getTypedValue(ctx: Context, paramName: String): Any =
        parseWithException(parser, ctx, paramName, String::class.simpleName)
}

/**
 * Parse query parameter as Int
 * f.x. /api?age=55 when paramName is "age" returns 55
 */
internal object QueryParamAsInt : IParameterParser {
    private val parser: (Context, String) -> Int = { ctx: Context, paramName: String ->
        ctx.queryParamAsClass(paramName, Int::class.java).getOrDefault(-1)
    }
    override fun getTypedValue(ctx: Context, paramName: String): Any =
        parseWithException(parser, ctx, paramName, Int::class.simpleName)
}

/**
 * Parse query parameter as LocalDate
 * f.x. /api?date=2022-10-20
 * Javalin Validator only supports primitives. Other types must be handled separately.
 */
internal object QueryParamAsLocalDate : IParameterParser {
    private val parser: (Context, String) -> LocalDate = { ctx: Context, paramName: String ->
        parseToLocalDate(ctx.queryParam(paramName))
    }

    override fun getTypedValue(ctx: Context, paramName: String): Any =
        parseWithException(parser, ctx, paramName, LocalDate::class.simpleName)
}

internal object BodyParamAsString : IParameterParser {
    private val parser: (Context, String) -> Any = { ctx: Context, _: String ->
        ctx.body()
    }
    override fun getTypedValue(ctx: Context, paramName: String): Any =
        parseWithException(parser, ctx, paramName, String::class.simpleName)
}

/**
 * Parse a form parameter as String
 * f.x. jQuery.ajax({data:{strValue:"aValue",intValue:42,dateValue:"2022-10-20",boolValue:true}})
 */
internal object FormParamAsString : IParameterParser {
    private val parser: (Context, String) -> Any = { ctx: Context, paramName: String ->
        ctx.formParamAsClass(paramName, String::class.java).get()
    }
    override fun getTypedValue(ctx: Context, paramName: String): Any =
        parseWithException(parser, ctx, paramName, String::class.simpleName)
}

/**
 * Parse a form parameter as Int
 * f.x. jQuery.ajax({data:{strValue:"aValue",intValue:42,dateValue:"2022-10-20",boolValue:true}})
 */
internal object FormParamAsInt : IParameterParser {
    private val parser: (Context, String) -> Any = { ctx: Context, paramName: String ->
        ctx.formParamAsClass(paramName, Int::class.java).get()
    }

    override fun getTypedValue(ctx: Context, paramName: String): Any =
        parseWithException(parser, ctx, paramName, Int::class.simpleName)
}

/**
 * Parse a form parameter as LocalDate
 * f.x. jQuery.ajax({data:{strValue:"aValue",intValue:42,dateValue:"2022-10-20",boolValue:true}})
 */
internal object FormParamAsLocalDate : IParameterParser {
    private val parser: (Context, String) -> Any = { ctx: Context, paramName: String ->
        parseToLocalDate(ctx.formParam(paramName))
    }

    override fun getTypedValue(ctx: Context, paramName: String): Any =
        parseWithException(parser, ctx, paramName, LocalDate::class.simpleName)
}

/**
 * Parse a form parameter as Boolean
 * f.x. jQuery.ajax({data:{strValue:"aValue",intValue:42,dateValue:"2022-10-20",boolValue:true}})
 */
internal object FormParamAsBoolean : IParameterParser {
    private val parser = { ctx: Context, paramName: String ->
        ctx.formParamAsClass(paramName, Boolean::class.java).get()
    }

    override fun getTypedValue(ctx: Context, paramName: String): Any =
        parseWithException(parser, ctx, paramName, Boolean::class.simpleName)
}

/**
 * Read file content of uploaded file
 */
internal object FileParamAsByteArray : IParameterParser {
    private val parser: (Context, String) -> Any = { ctx: Context, paramName: String ->
        ctx.uploadedFile(paramName)
            ?.content()
            ?.readAllBytes()
            ?: throw Exception("No file content for parameter $paramName")
    }

    override fun getTypedValue(ctx: Context, paramName: String): Any =
        parseWithException(parser, ctx, paramName, ByteArray::class.java.simpleName)
}

/**
 * Read file name of uploaded file
 */
internal object FileParamAsString : IParameterParser {
    private val parser: (Context, String) -> Any = { ctx: Context, paramName: String ->
        ctx.uploadedFile(paramName)
            ?.filename()
            ?: throw Exception("No filename for parameter $paramName")
    }

    override fun getTypedValue(ctx: Context, paramName: String): Any =
        parseWithException(parser, ctx, paramName, String::class.java.simpleName)
}

/**
 * Read a cookie
 */
internal object CookieValueAsString : IParameterParser {
    private val parser: (Context, String) -> Any = { ctx: Context, paramName: String ->
        ctx.cookie(paramName) ?: ""
    }

    override fun getTypedValue(ctx: Context, paramName: String): Any =
        parseWithException(parser, ctx, paramName, String::class.simpleName)
}


/**
 * Parse parameter to LocalDate and leave error message understandable for UI
 */
private fun parseToLocalDate(stringValue: String?): LocalDate =
    stringValue
        ?.let { LocalDate.parse(it) }
        ?: throw Exception("Could not parse '$stringValue' to LocalDate")

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
        throw AnnotationParserException("Could not parse '$paramName' to '$expectedType'")
    }
}
