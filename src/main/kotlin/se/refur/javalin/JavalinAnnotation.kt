package se.refur.javalin

import io.javalin.core.security.RouteRole

/**
 * The purpose of this object is holding settings needed to expose javalin endpoint routes
 */
object JavalinAnnotation {
    private var availableRoles: Map<String, RouteRole> = emptyMap()

    /**
     * Set roles
     * Example:
     * enum class MyRoles : RouteRoles { ADMIN, PUBLIC }
     * JavalinAnnotation.setRoles(MyRoles.values().associateBy { it.name })
     */
    fun setRoles(map: Map<String, RouteRole>) {
        availableRoles = map
    }

    /**
     * Get role for endpoint from String value
     */
    internal fun getRole(roleName: String): RouteRole =
        availableRoles[roleName]
            ?: throw Exception("No role matching '$roleName'")
}
