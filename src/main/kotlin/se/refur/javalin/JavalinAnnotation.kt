package se.refur.javalin

import io.javalin.core.security.RouteRole

/**
 * The purpose of this
 */
object JavalinAnnotation {
    private var availableRoles: List<RouteRole> = emptyList()

    fun setRoles(routeRoles: List<RouteRole>) {
        availableRoles = routeRoles
    }

    fun setRoles(routeRoles: Array<RouteRole>) {
        setRoles(routeRoles.toList())
    }

    internal fun getRole(roleName: String): RouteRole =
        availableRoles.first { it.toString() == roleName }
}
