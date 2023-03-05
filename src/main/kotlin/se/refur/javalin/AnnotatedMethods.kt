package se.refur.javalin

import io.javalin.http.ContentType
import io.javalin.http.HandlerType

/**
 * The purpose of this annotation is to register a web page endpoint
 * Function annotated with this is expected to return the rendering data map for template,
 *      Map<String,Any>
 * @property type Type of endpoint
 * @property path Route path for endpoint
 * @property templatePath path for Freemarker template
 * @property accessRole role that should be able to access endpoint
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Page(
    val type: HandlerType,
    val path: String,
    val templatePath: String,
    val accessRole: String = ""
)

/**
 * The purpose of this annotation is to register an API endpoint
 * Return for functions annotated with this is expected to return String
 * @property type Type of endpoint
 * @property path Route path for endpoint
 * @property accessRole role that should be able to access endpoint
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Api(
    val type: HandlerType,
    val path: String,
    val accessRole: String = ""
)

/**
 * The purpose of this annotation is to register an API endpoint that should set one or more cookies
 * Functions annotated with this is expected to return a map of cookie name to cookie value
 *      Map<String, String>
 * @property type Type of endpoint
 * @property path Route path for endpoint
 * @property accessRole role that should be able to access endpoint
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiCookie(
    val type: HandlerType,
    val path: String,
    val accessRole: String = ""
)

/**
 * The purpose of annotation is to register a file download endpoint
 * Return for functions annotated with this is expected to return ByteArray
 * @property type Type of endpoint
 * @property path Route path for endpoint
 * @property contentType content type of file to download
 * @property downloadAs default file name for download
 * @property accessRole role that should be able to access endpoint
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Download(
    val type: HandlerType,
    val path: String,
    val contentType: ContentType,
    val downloadAs: String,
    val accessRole: String = ""
)

/**
 * The purpose of annotation is to register a file upload endpoint
 * // TODO: Under construction
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Upload(
    val path: String,
    val accessRole: String = ""
)

