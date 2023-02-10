package se.refur.javalin

import io.javalin.core.security.RouteRole

/**
 * The purpose of this
 */
object JavalinAnnotation {
    private var availableRoles: Map<String, RouteRole> = emptyMap()

    fun setRoles(map: Map<String, RouteRole>) {
        availableRoles = map
    }

    internal fun getRole(roleName: String): RouteRole =
        availableRoles[roleName]
            ?: throw Exception("No role matching '$roleName'")
}
