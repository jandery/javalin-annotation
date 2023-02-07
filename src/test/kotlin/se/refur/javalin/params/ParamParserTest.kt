package se.refur.javalin.params

import io.javalin.http.Context
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ParamParserTest {

    @Test
    fun routeParamAsDate_validDateAsString_isLocalDate() {
        val ctx = mockk<Context>().also {
            every { it.pathParam("date") } returns "2020-10-20"
        }
        val localDate = RouteParamAsLocalDate.getTypedValue(ctx, "date")
        assertThat(localDate).isInstanceOf(LocalDate::class.java)
    }

    @Test
    fun routeParamAsDate_invalidDateAsString_exceptionIsThrown() {
        val ctx = mockk<Context>().also {
            every { it.pathParam("date") } returns "julgran"
        }
        assertThatThrownBy {
            RouteParamAsLocalDate.getTypedValue(ctx, "date")
        }
            .isExactlyInstanceOf(Exception::class.java)
            .hasMessage("Could not parse 'date' to 'LocalDate'")
    }

}