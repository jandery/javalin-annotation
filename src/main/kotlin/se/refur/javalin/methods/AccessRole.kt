package se.refur.javalin.methods

import io.javalin.core.security.RouteRole

/**
 * The purpose of this
 * TODO: Is this how to do it? RouteRole is interface and cannot be parsed in annotation
 */
internal class AccessRole(private val routeName: String) : RouteRole {

    override fun equals(other: Any?): Boolean =
        routeName == other.toString()

    override fun toString(): String =
        routeName

    override fun hashCode(): Int =
        routeName.hashCode()
}