package se.refur.javalin.methods

import io.javalin.http.Context
import java.util.concurrent.CompletableFuture

/**
 * Due to strange failure when calling API with jQuery.ajax
 * response ends up in .fail even though status is reported as HTTP_STATUS.OK (200)
 *
 * The purpose of this is to handle responses of all types
 * Context.result(response)     will fail for data class instances
 * Context.json(response)       will fail for String/Int/Booleans
 * This seems to solve both of them
 */
internal fun Context.primitiveOrJson(response: Any): Context =
    this.status(200)
        .future(CompletableFuture.completedFuture(response))
