package se.refur.javalin

import io.javalin.security.RouteRole


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
     * @param mapOfRoles map of "RoleName" to Role : RouteRole
     */
    fun setRoles(mapOfRoles: Map<String, RouteRole>) {
        availableRoles = mapOfRoles
    }

    /**
     * Set roles
     * Example:
     * enum class MyRoles : RouteRoles { ADMIN, PUBLIC }
     * JavalinAnnotation.setRoles(MyRoles.values())
     * @param listOfRoles map of Role : RouteRole
     */
    fun setRoles(listOfRoles: List<RouteRole>) {
        availableRoles = listOfRoles.associateBy { it.toString() }
    }

    /**
     * Get role for endpoint from String value
     */
    internal fun getRole(roleName: String): RouteRole =
        availableRoles[roleName]
            ?: throw Exception("No role matching '$roleName'")
}
