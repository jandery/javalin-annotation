package se.refur.javalin

import io.javalin.core.security.RouteRole
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JavalinAnnotationTest {

    private enum class MyRoles : RouteRole { ADMIN, PUBLIC }

    private val rolesAsMap = MyRoles.values().associateBy { it.name }

    @Test
    fun setRole_emptyList_exception() {
        JavalinAnnotation.setRoles(emptyList())
        Assertions.assertThatThrownBy {
            JavalinAnnotation.getRole("NON_EXISTING")
        }
            .isExactlyInstanceOf(Exception::class.java)
            .hasMessage("No role matching 'NON_EXISTING'")
    }
    @Test
    fun setRole_emptyMap_exception() {
        JavalinAnnotation.setRoles(emptyMap())
        Assertions.assertThatThrownBy {
            JavalinAnnotation.getRole("NON_EXISTING")
        }
            .isExactlyInstanceOf(Exception::class.java)
            .hasMessage("No role matching 'NON_EXISTING'")
    }

    @Test
    fun getRole_noMatchingRole_exception() {
        JavalinAnnotation.setRoles(rolesAsMap)
        Assertions.assertThatThrownBy {
            JavalinAnnotation.getRole("NON_EXISTING")
        }
            .isExactlyInstanceOf(Exception::class.java)
            .hasMessage("No role matching 'NON_EXISTING'")
    }

    @Test
    fun setRole_setAsMap_ADMIN() {
        JavalinAnnotation.setRoles(rolesAsMap)
        val role = JavalinAnnotation.getRole("ADMIN")
        assertThat(role).isEqualTo(MyRoles.ADMIN)
    }

    @Test
    fun setRole_setAsList_ADMIN() {
        JavalinAnnotation.setRoles(MyRoles.values().toList())
        val role = JavalinAnnotation.getRole("ADMIN")
        assertThat(role).isEqualTo(MyRoles.ADMIN)
    }
}