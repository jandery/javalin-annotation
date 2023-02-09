package se.refur.javalin.methods

import io.javalin.core.security.RouteRole
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class AccessRoleTest {
    private enum class MockedAccess : RouteRole {
        ADMIN, TEST
    }

    @Test
    fun accessRoleCanBeComparedToEnum_matchingRoles_isEqual() {
        val adminAccess = AccessRole("ADMIN")
        assertThat(adminAccess).isEqualTo(MockedAccess.ADMIN)
    }

    @Test
    fun accessRoleCanBeComparedToEnum_doNotMatch_isNotEqual() {
        val adminAccess = AccessRole("ADMIN")
        assertThat(adminAccess).isNotEqualTo(MockedAccess.TEST)
    }
}